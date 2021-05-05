package controllers.journeyPlanners;

import constants.Constants;
import jadexMAS.agents.TrafficParticipantBDI;
import model.ModeChangeCost;
import model.ModeUtility;
import model.frontend.StartEnd;
import model.journey.Journey;
import model.ontologyPojos.Preference;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import rmi.model.FinalDestination;
import rmi.model.Vehicle;
import utility.MathUtil;
import utility.OntoUtilBasics;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class PureTravelJourneyPlanner implements IJourneyPlanner {

    private Map<Vehicle, ModeUtility> modeUtilities;
    private List<ModeChangeCost> modeChangeCosts;
    private TrafficParticipantBDI agent;
    private Map<Vehicle, Integer> individualModeUtilityValues;
    private List<String> activeRules;

    final double maxBikeDistanceInMeters = 5000;
    final double maxWalkingDistanceInMeters = 2;
    final int lenghtOfStayInTicksPerDestination = 0; //ticks

    public PureTravelJourneyPlanner() {
        initModeUtilities();
        initModeChangeCosts();
        individualModeUtilityValues = new HashMap<>();
        this.activeRules = new ArrayList<>();
    }

    @Override
    public Journey generateJourneys(TrafficParticipantBDI agent) {
        this.agent = agent;
        List<FinalDestination> possibleDestinations = agent.getPossibleDestinations();

        if (!agent.isFromPopuplation()) {
            OntoUtilBasics ontoUtilBasics = agent.getOntoUtilBasics();

            //Infer Agent Preferences
            agent.prepareReasoning();
            OWLOntology infOnt = agent.getOntologyLoader().getInferredOntology(agent.getReasoner());
            OWLNamedIndividual myself = ontoUtilBasics.getMyselfInd(infOnt);

            Set<OWLDataPropertyAssertionAxiom> inferredDPFromMyself = ontoUtilBasics.getInferredDataPropertiesFromIndividual(infOnt, myself);
            Set<OWLObjectPropertyAssertionAxiom> inferredOPFromMyself = ontoUtilBasics.getInferredObjectPropertiesFromIndividual(infOnt, myself);

            //document activeRules
            for (OWLDataPropertyAssertionAxiom ax : inferredDPFromMyself) {
                String prop = ax.getProperty().asOWLDataProperty().getIRI().getRemainder().get();
                if (prop.equals("hasActiveRule")) {
                    this.activeRules.add(ax.getObject().getLiteral());
                }
            }

            //get Travel Preferences
            List<Preference> travelPreferences = new ArrayList<>();
            for (OWLObjectPropertyAssertionAxiom ax : inferredOPFromMyself) {
                Set<OWLNamedIndividual> individualsInObjectProperty = ax.getSimplified().getIndividualsInSignature();
                for (OWLNamedIndividual ind : individualsInObjectProperty) {
                    String indName = ind.getIRI().getShortForm();
                    if (!indName.equals(Constants.ontINDMyself)) {
                        if (ax.getProperty().asOWLObjectProperty().getIRI().getRemainder().get().equals("hasPreference")) {
                            Map<Integer, Double> scores = this.parseScoresFromInd(ontoUtilBasics, infOnt, ind, "hasScoreCat");
                            Map<Integer, Double> probabilities = this.determineProbabilities(scores);
                            int preferenceValue = this.determinePreferenceValue(probabilities);
                            Preference preference = new Preference(indName, ind, scores, probabilities, preferenceValue);
                            travelPreferences.add(preference);
                        }
                    }
                }
            }

            //decision preference critria
            Map<String, Integer> decisionFactors = new HashMap<>();
            for (Preference travPref : travelPreferences) {
                decisionFactors.put(travPref.getName(), travPref.getPreferenceValue());
            }
            agent.setDecisionFactors(decisionFactors);
        } else {

        }

        //determine mode utility values
        for (Vehicle mode : this.modeUtilities.keySet()) {
            int modeUtility = 0;
            modeUtility = modeUtility + getPrefVal("Flexibility") * modeUtilities.get(mode).getFlexibility();
            modeUtility = modeUtility + getPrefVal("Time") * modeUtilities.get(mode).getTime();
            modeUtility = modeUtility + getPrefVal("Reliability") * modeUtilities.get(mode).getReliability();
            modeUtility = modeUtility + getPrefVal("Privacy") * modeUtilities.get(mode).getPrivacy();
            modeUtility = modeUtility + getPrefVal("Environmental_Impact") * modeUtilities.get(mode).getEnvironmentalImpact();
            modeUtility = modeUtility + getPrefVal("Monetary_Costs") * modeUtilities.get(mode).getMonetaryCosts();
            modeUtility = modeUtility + getPrefVal("Convenience") * modeUtilities.get(mode).getConvenience();
            individualModeUtilityValues.put(mode, modeUtility);
        }

        Journey journey = new Journey();
        journey.setTrips(agent.getTrips());

        List<Vehicle> transportationModes = new ArrayList<>();
        Vehicle vehicle = selectVehicle(agent.getTrips().get(0), Vehicle.None);
        transportationModes.add(vehicle);
        journey.setTransportationMode(transportationModes);
        journey.setTotalUtilityScore(individualModeUtilityValues.get(vehicle));

        return journey;
    }


    @Override
    public Vehicle selectVehicle(StartEnd trip, Vehicle currentVehicle) {
        return new ArrayList<>(selectVehicleWithUtility(trip, currentVehicle).keySet()).get(0);
    }

    @Override
    public int getLengthOfStayInTicksPerDestination() {
        return lenghtOfStayInTicksPerDestination;
    }

    public Map<Vehicle, Double> selectVehicleWithUtility(StartEnd trip, Vehicle currentVehicle) {
        double estimatedDistance = MathUtil.distFrom(trip.getStart().getLat(), trip.getStart().getLon(), trip.getEnd().getLat(), trip.getEnd().getLon());
        Map<Vehicle, Integer> modeUtilityValsWithModeChangeCost = getModeUtilityValsWithModeChangeCost(currentVehicle);

        //limit selectable vehicles based on distance
        if (estimatedDistance > maxWalkingDistanceInMeters) {
            modeUtilityValsWithModeChangeCost.remove(Vehicle.FEET);
        }
        if (estimatedDistance > maxBikeDistanceInMeters) {
            modeUtilityValsWithModeChangeCost.remove(Vehicle.BIKE);
        }

        int maxModeUtility = Integer.MIN_VALUE;
        int indexOfMaxModeUtility = 0;
        for (int i = 0; i < modeUtilityValsWithModeChangeCost.values().size(); i++) {
            int val = new ArrayList<>(modeUtilityValsWithModeChangeCost.values()).get(i);
            if (val > maxModeUtility) {
                maxModeUtility = val;
                indexOfMaxModeUtility = i;
            }
        }
        Vehicle preferredVehicle = (Vehicle) new ArrayList(modeUtilityValsWithModeChangeCost.keySet()).get(indexOfMaxModeUtility);

        Map<Vehicle, Double> preferredVehicleMapWithUtility = new HashMap<>();
        preferredVehicleMapWithUtility.put(preferredVehicle, Double.valueOf(maxModeUtility));
        return preferredVehicleMapWithUtility;
    }


    public Map<Integer, Double> parseScoresFromInd(OntoUtilBasics ontoUtilBasics, OWLOntology ontology, OWLNamedIndividual ind, String dpName) {
        Map<Integer, Double> scoresFromInd = new HashMap<>();

        Set<OWLDataPropertyAssertionAxiom> inferredDPFromInd = ontoUtilBasics.getInferredDataPropertiesFromIndividual(ontology, ind);
        for (OWLDataPropertyAssertionAxiom ax : inferredDPFromInd) {
            int key = 0;
            String currentDPName = ax.getProperty().asOWLDataProperty().getIRI().getRemainder().get();
            if (currentDPName.equals(dpName + "1")) {
                key = 1;
            } else if (currentDPName.equals(dpName + "2")) {
                key = 2;
            } else if (currentDPName.equals(dpName + "3")) {
                key = 3;
            } else if (currentDPName.equals(dpName + "4")) {
                key = 4;
            } else if (currentDPName.equals(dpName + "5")) {
                key = 5;
            }

            if (currentDPName.contains(dpName)) {
                double dpValue = Double.valueOf(ax.getObject().getLiteral());
                if (scoresFromInd.keySet().contains(key)) {
                    dpValue = dpValue + scoresFromInd.get(key);
                }
                scoresFromInd.put(key, dpValue);
            }
        }

        return scoresFromInd;

    }

    public Map<Integer, Double> determineProbabilities(Map<Integer, Double> scores) {
        Map<Integer, Double> probsFromInd = new HashMap<>();

        double sum = 0;
        for (double val : scores.values()) {
            sum = sum + val;
        }

        for (Integer key : scores.keySet()) {
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            if (sum != 0d) {
                double val = scores.get(key) / sum;
                double roundedVal = Double.parseDouble(twoDForm.format(val));
                probsFromInd.put(key, roundedVal);
            }
        }

        Map<Integer, Double> scaledProbs = new HashMap<>();
        for (int i = 0; i < probsFromInd.keySet().size(); i++) {
            int key = i + 1;
            if (i == 0) {
                scaledProbs.put(key, probsFromInd.get(key));
            } else {
                double val = probsFromInd.get(key) + scaledProbs.get(i);
                scaledProbs.put(key, val);
            }
        }

        return scaledProbs;
    }

    public int determinePreferenceValue(Map<Integer, Double> scaledProbs) {

        double rand = Math.random();
        int score = -1;

        for (int key : scaledProbs.keySet()) {
            double cumulativeProbability = scaledProbs.get(key);
            if (rand <= cumulativeProbability) {
                score = key;
                break;
            }
        }

        return score;
    }

    private void initModeUtilities() {
        Map<Vehicle, ModeUtility> modeUtilityMap = new HashMap<>();

        ModeUtility carUtility = new ModeUtility(Vehicle.CAR, 4, 4, 3, 4, 4, 0, 1, 4);
        modeUtilityMap.put(Vehicle.CAR, carUtility);

        ModeUtility bikeUtility = new ModeUtility(Vehicle.BIKE, 2, 2, 4, 4, 2, 3, 3, 0);
        modeUtilityMap.put(Vehicle.BIKE, bikeUtility);

        ModeUtility feetUtility = new ModeUtility(Vehicle.FEET, 2, 0, 4, 4, 2, 4, 4, 1);
        modeUtilityMap.put(Vehicle.FEET, feetUtility);

        ModeUtility ptUtility = new ModeUtility(Vehicle.PUBLICTRANSPORT, 1, 2, 1, 0, 1, 2, 2, 3);
        modeUtilityMap.put(Vehicle.PUBLICTRANSPORT, ptUtility);

        this.modeUtilities = modeUtilityMap;

    }

    private void initModeChangeCosts() {
        List<ModeChangeCost> modeChangeCostList = new ArrayList<>();

        //From Car
        modeChangeCostList.add(new ModeChangeCost(Vehicle.CAR, Vehicle.CAR, 0));
        modeChangeCostList.add(new ModeChangeCost(Vehicle.CAR, Vehicle.BIKE, 4));
        modeChangeCostList.add(new ModeChangeCost(Vehicle.CAR, Vehicle.FEET, 1));
        modeChangeCostList.add(new ModeChangeCost(Vehicle.CAR, Vehicle.PUBLICTRANSPORT, 1));

        //From BIKE
        modeChangeCostList.add(new ModeChangeCost(Vehicle.BIKE, Vehicle.CAR, 5));
        modeChangeCostList.add(new ModeChangeCost(Vehicle.BIKE, Vehicle.BIKE, 0));
        modeChangeCostList.add(new ModeChangeCost(Vehicle.BIKE, Vehicle.FEET, 1));
        modeChangeCostList.add(new ModeChangeCost(Vehicle.BIKE, Vehicle.PUBLICTRANSPORT, 2));

        //From Feet
        modeChangeCostList.add(new ModeChangeCost(Vehicle.FEET, Vehicle.CAR, 2));
        modeChangeCostList.add(new ModeChangeCost(Vehicle.FEET, Vehicle.BIKE, 0));
        modeChangeCostList.add(new ModeChangeCost(Vehicle.FEET, Vehicle.FEET, 0));
        modeChangeCostList.add(new ModeChangeCost(Vehicle.FEET, Vehicle.PUBLICTRANSPORT, 1));

        //From PT
        modeChangeCostList.add(new ModeChangeCost(Vehicle.PUBLICTRANSPORT, Vehicle.CAR, 5));
        modeChangeCostList.add(new ModeChangeCost(Vehicle.PUBLICTRANSPORT, Vehicle.BIKE, 3));
        modeChangeCostList.add(new ModeChangeCost(Vehicle.PUBLICTRANSPORT, Vehicle.FEET, 1));
        modeChangeCostList.add(new ModeChangeCost(Vehicle.PUBLICTRANSPORT, Vehicle.PUBLICTRANSPORT, 0));

        this.modeChangeCosts = modeChangeCostList;

    }

    private Map<Vehicle, Integer> getModeUtilityValsWithModeChangeCost(Vehicle currentVehicle) {
        Map<Vehicle, Integer> mUtilityWithModeChangeCost = new HashMap<>();
        int modeChangeCost = 0;
        int modeUtility = 0;

        for (Vehicle mode : this.individualModeUtilityValues.keySet()) {
            Vehicle newVehicle = mode;
            if (!currentVehicle.equals(Vehicle.None)) {
                modeChangeCost = this.modeChangeCosts.stream().filter(cost -> (cost.getCurrentMode().equals(currentVehicle) && cost.getNewMode().equals(newVehicle))).collect(Collectors.toList()).get(0).getChangeCost();
            }
            modeUtility = this.individualModeUtilityValues.get(mode) - (7 * modeChangeCost);
            mUtilityWithModeChangeCost.put(mode, modeUtility);
        }

        return mUtilityWithModeChangeCost;
    }

    private int getPrefVal(String prefName) {
        return this.agent.getDecisionFactors().get(prefName);
    }


    public Map<Vehicle, ModeUtility> getModeUtilities() {
        return modeUtilities;
    }

    public void setModeUtilities(Map<Vehicle, ModeUtility> modeUtilities) {
        this.modeUtilities = modeUtilities;
    }

    public List<ModeChangeCost> getModeChangeCosts() {
        return modeChangeCosts;
    }

    public void setModeChangeCosts(List<ModeChangeCost> modeChangeCosts) {
        this.modeChangeCosts = modeChangeCosts;
    }

    @Override
    public List<String> getActiveRules() {
        return activeRules;
    }

    public void setActiveRules(List<String> activeRules) {
        this.activeRules = activeRules;
    }
}
