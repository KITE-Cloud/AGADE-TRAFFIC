package controllers.journeyPlanners;

import constants.Constants;
import controllers.OntologyLoader;
import controllers.generators.GroceryListGenerator;
import jadexMAS.agents.TrafficParticipantBDI;
import model.ModeChangeCost;
import model.ModeUtility;
import model.frontend.StartEnd;
import model.journey.GroceryShoppingJourney;
import model.journey.Journey;
import model.journey.MinMaxOfJourneysObject;
import model.ontologyPojos.PersonaMapProperties;
import model.ontologyPojos.PersonaProfile;
import model.ontologyPojos.Preference;
import model.ontologyPojos.Supermarket;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import rmi.model.FinalDestination;
import rmi.model.Location;
import rmi.model.Vehicle;
import utility.MathUtil;
import utility.OntoUtilBasics;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class SupermarketJourneyPlannerAdvanced implements IJourneyPlanner {

    private Map<Vehicle, ModeUtility> modeUtilities;
    private List<ModeChangeCost> modeChangeCosts;
    private TrafficParticipantBDI agent;
    private Map<Vehicle, Integer> individualModeUtilityValues;
    private Map<String, Integer> groceryList;
    private List<Supermarket> supermarkets;
    private List<String> activeRules;

    final double maxBikeDistanceInMeters = 5000;
    final double maxWalkingDistanceInMeters = 2000;
    final int lenghtOfStayInTicksPerDestination = 15; //ticks

    public SupermarketJourneyPlannerAdvanced() {
        initModeUtilities();
        initModeChangeCosts();
        individualModeUtilityValues = new HashMap<>();
        this.activeRules = new ArrayList<>();
    }

    @Override
    public Journey generateJourneys(TrafficParticipantBDI agent) {
        this.agent = agent;
        List<FinalDestination> possibleDestinations = agent.getPossibleDestinations();
        Location origin = agent.getTrips().get(0).getStart(); //temporary trips map from extern with only valid startlocation

        if(!agent.isFromPopuplation()){
            PersonaProfile persona = agent.getPersona();
            OntoUtilBasics ontoUtilBasics = agent.getOntoUtilBasics();

            //Infer Agent Preferences
            agent.prepareReasoning();
            //OWLOntology infOnt = agent.getOntologyLoader().getInferredOntology(agent.getReasoner(), manager);
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

            //get Travel & Food Preferences
            List<Preference> travelPreferences = new ArrayList<>();
            List<Preference> foodPreferences = new ArrayList<>();
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
                        } else if (ax.getProperty().asOWLObjectProperty().getIRI().getRemainder().get().equals("hasFoodPreference")) {
                            Map<Integer, Double> scores = this.parseScoresFromInd(ontoUtilBasics, infOnt, ind, "hasScoreCat");
                            Map<Integer, Double> probabilities = this.determineProbabilities(scores);
                            int preferenceValue = this.determinePreferenceValue(probabilities);
                            Preference preference = new Preference(indName, ind, scores, probabilities, preferenceValue);
                            foodPreferences.add(preference);
                        }
                    }
                }
            }

            //get Household Size
            Map<Integer, Double> householdScores = this.parseScoresFromInd(ontoUtilBasics, infOnt, myself, "hasProbabilityHouseholdSize");
            Map<Integer, Double> householdProbabilities = this.determineProbabilities(householdScores);
            int householdPreferenceValue = this.determinePreferenceValue(householdProbabilities);
            Preference householdPref = new Preference("Household", myself, householdScores, householdProbabilities, householdPreferenceValue);

            double preferenceForCraftProducts = 0;
            double aversionForAdditionalTrips = 0;
            for (OWLDataPropertyAssertionAxiom ax : inferredDPFromMyself) {
                String currentDPName = ax.getProperty().asOWLDataProperty().getIRI().getRemainder().get();
                if (currentDPName.equals("hasProbabilityCraftProducts")) {
                    preferenceForCraftProducts = Double.valueOf(ax.getObject().getLiteral());
                } else if (currentDPName.equals("hasAversionForAdditionalTrips")) {
                    aversionForAdditionalTrips = Double.valueOf(ax.getObject().getLiteral());
                }
            }

            //get aversion for additional trips
            double rand = Math.random() * 100;
            int precludesExtraTrip = (rand <= aversionForAdditionalTrips) ? 1 : 0;  //1: true; 0:false

            //get preference for craft product
            rand = Math.random() * 100;
            int prefersCraftProducts = (rand <= preferenceForCraftProducts) ? 1 : 0; ////1: true; 0:false


            //decision preference critria
            Map<String, Integer> decisionFactors = new HashMap<>();
            for (Preference travPref : travelPreferences) {
                decisionFactors.put(travPref.getName(), travPref.getPreferenceValue());
            }
            for (Preference foodPref : foodPreferences) {
                decisionFactors.put(foodPref.getName(), foodPref.getPreferenceValue());
            }
            decisionFactors.put(householdPref.getName(), householdPref.getPreferenceValue());
            decisionFactors.put("precludesExtraTrip", precludesExtraTrip);
            decisionFactors.put("prefersCrafProducts", prefersCraftProducts);
            agent.setDecisionFactors(decisionFactors);

            // agent generate groceryList
            PersonaMapProperties shoppingProperties = persona.getShoppingProperties();
            int personInHousehold = householdPref.getPreferenceValue();
            GroceryListGenerator groceryListGenerator = new GroceryListGenerator();
            int fats = personInHousehold * shoppingProperties.getValues().get(shoppingProperties.getKeys().indexOf("Fats"));
            int dairy = personInHousehold * shoppingProperties.getValues().get(shoppingProperties.getKeys().indexOf("Dairy"));
            int fruit = personInHousehold * shoppingProperties.getValues().get(shoppingProperties.getKeys().indexOf("Fruit"));
            int grains = personInHousehold * shoppingProperties.getValues().get(shoppingProperties.getKeys().indexOf("Grains"));
            int vegetable = personInHousehold * shoppingProperties.getValues().get(shoppingProperties.getKeys().indexOf("Vegetable"));
            int protein = personInHousehold * shoppingProperties.getValues().get(shoppingProperties.getKeys().indexOf("Protein"));

            //key: Product name; Value: amount
            groceryList = groceryListGenerator.generateGroceryList(fats, dairy, fruit, grains, vegetable, protein);
            supermarkets = createSupermarketObjects(possibleDestinations, infOnt);

        }
        else {
            groceryList = new HashMap<>();
            for(String item : agent.getGroceryList()){
                groceryList.put(item, 1);
            }
            supermarkets = createSupermarketObjects(possibleDestinations, agent.getOntologyLoader().getInferredOntology(agent.getReasoner()));
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

        int numMaxSupermarkets = 3;
        int numVisitedSupermarkets = 0;
        List<Supermarket> tmpRemainingSupermarkets = new ArrayList<>(supermarkets);
        Supermarket originSupermarket = new Supermarket();
        for (Supermarket supermarket : tmpRemainingSupermarkets) {
            if (supermarket.getLocation().getLocation().getLat() == origin.getLat() && supermarket.getLocation().getLocation().getLon() == origin.getLon())
                originSupermarket = supermarket;
        }
        tmpRemainingSupermarkets.remove(originSupermarket);

        GroceryShoppingJourney groceryShoppingJourney = new GroceryShoppingJourney();

        Location currenLoc = origin;
        List<String> tmpGroceryList = new ArrayList<>(groceryList.keySet());
        while (numVisitedSupermarkets < numMaxSupermarkets || tmpGroceryList.size() == 0) {
            groceryShoppingJourney.setGroceryList(tmpGroceryList);
            groceryShoppingJourney = selectNextSupermarket(groceryShoppingJourney, tmpRemainingSupermarkets, currenLoc);
            if (groceryShoppingJourney.getGroceryList().size() == tmpGroceryList.size()) {
                //remaining items could not be found at any supermarkets. Shopping journey ends.
                break;
            } else {
                tmpRemainingSupermarkets.removeAll(groceryShoppingJourney.getVisitedSupermarket());
                tmpGroceryList = groceryShoppingJourney.getGroceryList();
                currenLoc = groceryShoppingJourney.getTrips().get(groceryShoppingJourney.getTrips().size() - 1).getEnd();
                numVisitedSupermarkets++;
            }
        }

        //error handling: if no journeys found. stay at home and do not interrupt simulation :D
        if (groceryShoppingJourney.getVisitedSupermarket().size() == 0) {
            groceryShoppingJourney = new GroceryShoppingJourney();
            StartEnd onlyTrip = new StartEnd();
            onlyTrip.setStart(origin);
            onlyTrip.setEnd(origin);
            groceryShoppingJourney.getTrips().add(onlyTrip);
            groceryShoppingJourney.getTransportationMode().add(Vehicle.FEET);
            groceryShoppingJourney.getEstimatedTravelDistances().add(0d);
        } else {
            //add final trip from last supermarket to origin (home) location
            Location lastSupermarket = groceryShoppingJourney.getVisitedSupermarket().get(groceryShoppingJourney.getVisitedSupermarket().size() - 1).getLocation().getLocation();
            StartEnd tripBackHome = new StartEnd();
            tripBackHome.setStart(lastSupermarket);
            tripBackHome.setEnd(origin);
            Vehicle initialVehicle = groceryShoppingJourney.getTransportationMode().get(0);
            double estimatedTravelDistance = MathUtil.distFrom(lastSupermarket.getLat(), lastSupermarket.getLon(), origin.getLat(), origin.getLon());
            groceryShoppingJourney.getTrips().add(tripBackHome);
            groceryShoppingJourney.getTransportationMode().add(initialVehicle);
            groceryShoppingJourney.getEstimatedTravelDistances().add(estimatedTravelDistance);
        }

        return groceryShoppingJourney;
    }

    public GroceryShoppingJourney selectNextSupermarket(GroceryShoppingJourney jo, List<Supermarket> remainingSupermarkets, Location currentLocation) {
        List<GroceryShoppingJourney> possibleJourneys = new ArrayList<>(); //journey , cost

        //evaluate list of possible journeys by utilities and cost
        List<Double> travelDistances = new ArrayList<>();
        List<Double> avgModeUtilites = new ArrayList<>();
        List<Double> avgShoppingUtilites = new ArrayList<>();
        List<Double> supermarketParking = new ArrayList<>();

        for (Supermarket currentSupermarket : remainingSupermarkets) {
            GroceryShoppingJourney currentJourney = new GroceryShoppingJourney(jo.getTrips(), jo.getTransportationMode(), jo.getModeUtility(), jo.getEstimatedTravelDistances(), jo.getShoppingUtility(), jo.getGroceryList(), jo.getVisitedSupermarket()); //create deep copy
            currentJourney = visitSupermarket(currentJourney, currentLocation, currentSupermarket, Vehicle.None);

            double parkindSlots = currentSupermarket.getLocation().getCapacity();
            supermarketParking.add(parkindSlots);
            currentJourney.getParkingSlots().add(parkindSlots);

            double productCoverage = calculateProductCoverage(jo.getGroceryList().size(), currentJourney.getGroceryList().size());
            if (productCoverage > 0) {
                possibleJourneys.add(currentJourney); //journeys to supermarkets where no items are purchased can be skipped
            }
        }

        for (GroceryShoppingJourney j : possibleJourneys) {
            travelDistances.add(j.getTotalTravelDistance());
            avgModeUtilites.add(j.getAvgModeUtility());
            avgShoppingUtilites.add(j.getAvgShoppingUtility());
        }

        Map<String, Double> minMaxTravelDistance = MathUtil.minMaxDoubleInList(travelDistances);
        Map<String, Double> minMaxShoppingUtil = MathUtil.minMaxDoubleInList(avgShoppingUtilites);
        Map<String, Double> minMaxModeUtil = MathUtil.minMaxDoubleInList(avgModeUtilites);
        Map<String, Double> minMaxParkingSlots = MathUtil.minMaxDoubleInList(supermarketParking);

        MinMaxOfJourneysObject minMax = new MinMaxOfJourneysObject(minMaxTravelDistance.get("min"), minMaxTravelDistance.get("max"), minMaxShoppingUtil.get("min"), minMaxShoppingUtil.get("max"), minMaxModeUtil.get("min"), minMaxModeUtil.get("max"), minMaxParkingSlots.get("min"), minMaxParkingSlots.get("max"));

        for (GroceryShoppingJourney journey : possibleJourneys) {
            double productCoverage = calculateProductCoverage(jo.getGroceryList().size(), journey.getGroceryList().size());
            double totalUtilityScore = determineTotalUtilityScoreOfJourney(journey, minMax, productCoverage);
            journey.setTotalUtilityScore(totalUtilityScore);
        }

        //choose journey with totalUtilityScore
        possibleJourneys.sort(GroceryShoppingJourney::compareTo);

        if (possibleJourneys.size() == 0) {
            return jo; //last journey selected
        } else {
            return possibleJourneys.get(possibleJourneys.size() - 1);
        }
    }

    public double determineTotalUtilityScoreOfJourney(GroceryShoppingJourney journey, MinMaxOfJourneysObject minMax, double productCoverage) {
        double totalTravelDistanceInJourney = journey.getTotalTravelDistance();
        double avgModeUtility = journey.getAvgModeUtility();
        double avgShoppingUtility = journey.getAvgShoppingUtility();

        double totalTravelDistanceScaled = MathUtil.minMaxScaling(totalTravelDistanceInJourney, minMax.getMinDistance(), minMax.getMaxDistance());
        double avgModeUtilityScaled = MathUtil.minMaxScaling(avgModeUtility, minMax.getMinModeUtility(), minMax.getMaxModeUtility());
        double avgShoppingUtilityScaled = MathUtil.minMaxScaling(avgShoppingUtility, minMax.getMinShoppingUtility(), minMax.getMaxShoppingUtility());
        double parkingSlotsScaled = MathUtil.minMaxScaling(journey.getParkingSlots().get(journey.getParkingSlots().size() - 1), minMax.getMinParking(), minMax.getMaxParking());

        double coverageImportance;
        if (getPrefVal("precludesExtraTrip") == 1) {
            coverageImportance = 2;
        } else {
            coverageImportance = journey.getVisitedSupermarket().size() * 0.5; //the higher the num of already visited supermarkets the higher the importance of product coverage.
        }

        int lastVehicleIsCar = (journey.getTransportationMode().get(journey.getTransportationMode().size() - 1) == Vehicle.CAR) ? 1 : 0;

        double totalScore = (1 - totalTravelDistanceScaled) + avgModeUtilityScaled + avgShoppingUtilityScaled + (coverageImportance * productCoverage); // + lastVehicleIsCar * parkingSlotsScaled;
        return totalScore;
    }

    public GroceryShoppingJourney visitSupermarket(GroceryShoppingJourney currentJourney, Location currentLocation, Supermarket targetSupermarket, Vehicle currentVehicle) {

        currentJourney.getVisitedSupermarket().add(targetSupermarket);
        StartEnd currentTrip = new StartEnd();
        currentTrip.setStart(currentLocation);
        currentTrip.setEnd(targetSupermarket.getLocation().getLocation());
        currentJourney.getTrips().add(currentTrip);
        double estimatedTravelDistance = MathUtil.distFrom(currentTrip.getStart().getLat(), currentTrip.getStart().getLon(), currentTrip.getEnd().getLat(), currentTrip.getEnd().getLon());

        Map<Vehicle, Double> preferredVehicleWithUtilityMap = selectVehicleWithUtility(currentTrip, currentVehicle);
        Vehicle vehicle = new ArrayList<>(preferredVehicleWithUtilityMap.keySet()).get(0);
        currentJourney.getTransportationMode().add(vehicle);

        //purchased products can be removed from groceryList
        List<String> products = targetSupermarket.getProducts();
        currentJourney.getGroceryList().removeAll(products);

        int foodQualityPref = getPrefVal("Healthy");
        int foodPricePref = getPrefVal("Pricing");
        int foodEcoPref = getPrefVal("Eco-Friendly");
        int foodFairnessPref = getPrefVal("Fairtrade");
        double shoppingUtility = foodPricePref * targetSupermarket.getPriceTendencyScore()
                                + foodQualityPref * targetSupermarket.getProductQualityScore()
                                + foodEcoPref * targetSupermarket.getProductEcoScore()
                                + foodFairnessPref * targetSupermarket.getProductFairnessScore();

        //document travel distance, mode utility, and shopping utility
        currentJourney.getEstimatedTravelDistances().add(estimatedTravelDistance);
        currentJourney.getModeUtility().add(preferredVehicleWithUtilityMap.get(vehicle));
        currentJourney.getShoppingUtility().add(shoppingUtility);

        return currentJourney;
    }

    public double calculateProductCoverage(int sizeOldList, int sizeListAfterVisit) {
        double productCoverage = 0;
        if (sizeOldList != 0) {
            productCoverage = 1 - ((double) sizeListAfterVisit / sizeOldList);
        }
        return productCoverage;
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

    private List<Supermarket> createSupermarketObjects(List<FinalDestination> possibleDestinations, OWLOntology inferredOntology) {
        OntoUtilBasics ontoUtilBasics = agent.getOntoUtilBasics();
        OntologyLoader ontologyLoader = agent.getOntologyLoader();
        OWLOntology ontology = agent.getOntology();
        OWLReasoner reasoner = agent.getReasoner();

        List<Supermarket> supermarkets = new ArrayList<>();
        List<String> supermarketTypes = ontoUtilBasics.owlClassesAsStrings(ontoUtilBasics.getSubclassesBySuperClassName(ontology, reasoner, "PointsOfInterest", false));
        List<FinalDestination> supermarketLocations = possibleDestinations.stream().filter(destination -> supermarketTypes.contains(destination.getLocation().getPoiType())).collect(Collectors.toList());

        for (FinalDestination poi : supermarketLocations) { //for each poi create SupermarketObject
            String poiType = poi.getLocation().getPoiType().toLowerCase();
            //OWLOntology inferredOntology = ontologyLoader.getInferredOntology(reasoner);
            OWLNamedIndividual marketInd = ontoUtilBasics.getIndividualByName(inferredOntology, poiType);
            Set<OWLObjectPropertyAssertionAxiom> inferredObjectProperties = ontoUtilBasics.getInferredObjectPropertiesFromIndividual(inferredOntology, marketInd);

            //collect inferred products from ontology
            Set<String> products = new HashSet();
            String size = "";
            String productQuality = "";
            String priceTendency = "";
            String productEcoFriendliness = "";
            String productFairness = "";
            for (OWLObjectPropertyAssertionAxiom ax : inferredObjectProperties) {
                Set<OWLNamedIndividual> individualsInObjectProperty = ax.getSimplified().getIndividualsInSignature();
                for (OWLNamedIndividual ind : individualsInObjectProperty) {
                    String indName = ind.getIRI().getShortForm();
                    if (!indName.equals(poiType)) {
                        if (ax.getProperty().asOWLObjectProperty().getIRI().getRemainder().get().equals("hasProduct")) {
                            products.add(indName);
                        } else if (ax.getProperty().asOWLObjectProperty().getIRI().getRemainder().get().equals("hasSize")) {
                            size = indName;
                        } else if (ax.getProperty().asOWLObjectProperty().getIRI().getRemainder().get().equals("hasProductQuality")) {
                            productQuality = indName;
                        } else if (ax.getProperty().asOWLObjectProperty().getIRI().getRemainder().get().equals("hasPriceTendency")) {
                            priceTendency = indName;
                        } else if (ax.getProperty().asOWLObjectProperty().getIRI().getRemainder().get().equals("hasProductEcoFriendliness")) {
                            productEcoFriendliness = indName;
                        } else if (ax.getProperty().asOWLObjectProperty().getIRI().getRemainder().get().equals("hasProductFairness")) {
                            productFairness = indName;
                        }
                    }
                }
            }
            List<String> inventory = new ArrayList<>(products);
            //create Supermarket object
            supermarkets.add(new Supermarket(poi, inventory, size, productQuality, priceTendency, productEcoFriendliness, productFairness));
        }

        return supermarkets;
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
                double roundedVal = Double.parseDouble(twoDForm.format(val).replace(",","."));
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

    public Map<String, Integer> getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(Map<String, Integer> groceryList) {
        this.groceryList = groceryList;
    }


    @Override
    public List<String> getActiveRules() {
        return activeRules;
    }

    public void setActiveRules(List<String> activeRules) {
        this.activeRules = activeRules;
    }
}

