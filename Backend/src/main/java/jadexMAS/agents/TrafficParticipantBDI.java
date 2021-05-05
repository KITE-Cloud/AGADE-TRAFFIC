package jadexMAS.agents;

// import com.sun.tools.internal.jxc.ap.Const;

import constants.Constants;
import constants.TrafficConstants;
import controllers.Client;
import controllers.SimulationResultBuilder;
import controllers.TrafficJamDataHandler;
import controllers.converter.RoutingInformationToFrontendAgentConverter;
import controllers.journeyPlanners.*;
import exceptions.PlanNotFoundException;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.AgentFeature;
import jadexMAS.goals.DriveToGoal;
import jadexMAS.goals.PauseMovingGoal;
import jadexMAS.goals.PlanJourneyGoal;
import model.AgentPropertiesEnum;
import model.MapEntryAdapter;
import model.MapEntryAdapterObject;
import model.frontend.PopAgentPojo;
import model.frontend.StartEnd;
import model.journey.Journey;
import model.ontologyPojos.PersonaProfile;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.parameters.Imports;
import rmi.RMIClient;
import rmi.model.*;
import utility.MathUtil;
import utility.OntoUtilBasics;

import java.util.*;

@Agent
public class TrafficParticipantBDI extends AbstractOWLAgent implements ITrafficParticipant {
    @AgentFeature
    protected IExecutionFeature execFeature;
    @Belief
    RMIClient routingRMI;
    @Belief
    protected RoutingInformation currentRoutingInformation;
    protected ArrayList<AgentMovement> movementHistory;
    @Belief
    protected Location origin;
    @Belief
    protected Location destination;
    @Belief
    protected Location currentLocation;
    @Belief
    protected boolean arrivedAtDestination;
    @Belief
    protected int birthTick;
    @Belief
    protected int id;
    @Belief
    protected Map<String, Object> agentProperties;
    @Belief
    protected Vehicle currentVehicle;
    @Belief
    protected float travelDistance;
    @Belief
    protected int carsOnSameRoad; // frontend - später
    @Belief
    protected int hateFactorCarsOnRoad;
    @Belief
    protected int calculatedTravelDuration; // 1 tick = 1 minute -- backend
    protected List<StartEnd> trips;
    protected int currentJourneyIndex;
    protected boolean isActivityModelled;
    protected List<FinalDestination> possibleDestinations;
    protected PersonaProfile persona;
    protected Journey journey;
    IJourneyPlanner journeyPlanner;
    private boolean initialJourneyPlanningDone;
    private Map<String, Integer> decisionFactors;
    private boolean fromPopuplation;
    List<String> groceryList;


    @AgentBody //Basically Constructor
    public void body() {

        this.ontoUtilBasics = new OntoUtilBasics();
        this.agentProperties = getAgent().getExternalAccess().getArguments().get();
        this.birthTick = (int) getAgent().getExternalAccess().getArguments().get().get(AgentPropertiesEnum.birthTick.name());
        this.id = (int) getAgent().getExternalAccess().getArguments().get().get(AgentPropertiesEnum.id.name());
        this.arrivedAtDestination = false;
        this.routingRMI = RMIClient.getInstance();
        this.movementHistory = new ArrayList<>();
        this.persona = (PersonaProfile) getAgent().getExternalAccess().getArguments().get().get(AgentPropertiesEnum.persona.name());
        this.isActivityModelled = (boolean) getAgent().getExternalAccess().getArguments().get().get(AgentPropertiesEnum.isActivityModelled.name());
        this.possibleDestinations = (List<FinalDestination>) getAgent().getExternalAccess().getArguments().get().get(AgentPropertiesEnum.possibleDestinations.name());
        this.trips = (List<StartEnd>) getAgent().getExternalAccess().getArguments().get().get(AgentPropertiesEnum.trips.name()); //in case of activity modelling: temporary trips map from extern with only valid startlocation
        this.currentJourneyIndex = 0;
        this.initialJourneyPlanningDone = false;

        this.currentVehicle = Vehicle.None;
        this.fromPopuplation = (boolean) getAgent().getExternalAccess().getArguments().get().get(AgentPropertiesEnum.fromPopulation.name());
        this.groceryList = (fromPopuplation) ?  ( (List<String>) getAgent().getExternalAccess().getArguments().get().get(AgentPropertiesEnum.groceryList.name())) : new ArrayList<>();
        this.decisionFactors = (fromPopuplation) ?  ( (Map<String, Integer>) getAgent().getExternalAccess().getArguments().get().get(AgentPropertiesEnum.decisionFactors.name())) : new HashMap<>();

        //defaulValues - currently not used
        this.carsOnSameRoad = 0;
        this.hateFactorCarsOnRoad = 0;
    }

    @Override
    public <T> T runPlan(String plan, Class<T> type) {

        try {
            return super.runPlan(plan, type);
        } catch (PlanNotFoundException pnfe) {
            // if no plan was found in generic class of specific agent, the following jadex.plans are checked
        }

        if (plan.equalsIgnoreCase(Constants.plnAnswerRequestForAcquaintancePlan)) {
            return (T) agent.getComponentFeature(IBDIAgentFeature.class).adoptPlan(Constants.plnAnswerRequestForAcquaintancePlan).get();
        }
        if (plan.equalsIgnoreCase(Constants.plnAskForCurrentProductInformationPlan)) {
            return (T) agent.getComponentFeature(IBDIAgentFeature.class).adoptPlan(Constants.plnAskForCurrentProductInformationPlan).get();
        }
        if (plan.equalsIgnoreCase(Constants.plnFollowPersonalPreferencesPlan)) {
            return (T) agent.getComponentFeature(IBDIAgentFeature.class).adoptPlan(Constants.plnFollowPersonalPreferencesPlan).get();
        }
        if (plan.equalsIgnoreCase(Constants.plnFollowSocialAffiliationPlan)) {
            return (T) agent.getComponentFeature(IBDIAgentFeature.class).adoptPlan(Constants.plnFollowSocialAffiliationPlan).get();
        }
        if (plan.equalsIgnoreCase(Constants.plnGoShoppingPlan)) {
            return (T) agent.getComponentFeature(IBDIAgentFeature.class).adoptPlan(Constants.plnGoShoppingPlan).get();
        }
        if (plan.equalsIgnoreCase(Constants.plnAskForRecommendationPlan)) {
            return (T) agent.getComponentFeature(IBDIAgentFeature.class).adoptPlan(Constants.plnAskForRecommendationPlan).get();
        }
        if (plan.equalsIgnoreCase("DriveToPlan")) {
            return (T) agent.getComponentFeature(IBDIAgentFeature.class).adoptPlan("DriveToPlan").get();
        }
        throw new PlanNotFoundException(
                "Plan not found: " + plan + ", agent: " + getAgentName());
    }


    @Plan(trigger = @Trigger(goals = PlanJourneyGoal.class))
    public IFuture<Boolean> PlanJourney() {
        if (!this.initialJourneyPlanningDone) {
            //in case of activity modelling destinations for trips need to be determined
            // this.journeyPlanner = (isActivityModelled) ? new SupermarketJourneyPlanner() : new DefaultJourneyPlanner();
            this.journeyPlanner = (isActivityModelled) ? new SupermarketJourneyPlannerAdvanced() : new PureTravelJourneyPlanner();
            this.journey = journeyPlanner.generateJourneys(this);
            this.trips = journey.getTrips();

            this.currentVehicle = journey.getTransportationMode().get(currentJourneyIndex);
            this.origin = trips.get(currentJourneyIndex).getStart();
            this.destination = trips.get(currentJourneyIndex).getEnd();
            this.currentLocation = origin;
            this.initialJourneyPlanningDone = true;
            //fixme: RoutingPreference always default: make use (multiple utility functions) or remove routingPreference completely (only one super utility function)
            this.currentRoutingInformation = new RoutingInformation(this.currentVehicle, this.currentLocation, this.destination, RoutingPreference.DEFAULT);

            if(isActivityModelled){
                this.groceryList = new ArrayList<>(((SupermarketJourneyPlannerAdvanced)journeyPlanner).getGroceryList().keySet());
            }
        }


        return new Future<Boolean>(true);
    }

    /**
     * Is only called once, drectly after the initialization of the agent.
     * Performs a reasoning in order to determine which vehicle the agent should use.
     *
     * @return
     */
  /*  @Plan(trigger = @Trigger(goals = SelectVehicleGoal.class))
    public IFuture<Boolean> SelectVehicle() {
        long initialTimestamp = System.currentTimeMillis();
//        System.out.println("Hello, I'am agent " + agent.getComponentIdentifier().getLocalName());
        // only do it once
        if (this.vehicle == null) {

            //    System.out.println("call performReasonin from SelectVehicle");

            prepareReasoning();
            OWLOntology infOnt = ontologyLoader.getInferredOntology(this.reasoner);

            OWLNamedIndividual myself = null;
            Set<OWLNamedIndividual> individuals = ontology.getIndividualsInSignature();
            for (OWLNamedIndividual owlIndividual : individuals) {
                if (owlIndividual.getIRI().getShortForm().equals(Constants.ontINDMyself)) {
                    myself = owlIndividual;
                }
            }

            Set<OWLObjectPropertyAssertionAxiom> objectProperties = infOnt.getObjectPropertyAssertionAxioms(myself);
            HashMap<String, Integer> vehicleCounterMap = new HashMap<>();
            for (OWLObjectPropertyAssertionAxiom ax : objectProperties) {
                String prop = ax.getProperty().asOWLObjectProperty().getIRI().getRemainder().get();

                if (prop.equals("prefersVehicle")) {
                    String vehicle = ax.getObject().asOWLNamedIndividual().getIRI().getRemainder().get();

                    if (!vehicleCounterMap.containsKey(vehicle))
                        vehicleCounterMap.put(vehicle, 0);

                    vehicleCounterMap.put(vehicle, vehicleCounterMap.get(vehicle) + 1);
                } else if (prop.equals("aversesVehicle")) {
                    String vehicle = ax.getObject().asOWLNamedIndividual().getIRI().getRemainder().get();
                    if (!vehicleCounterMap.containsKey(vehicle))
                        vehicleCounterMap.put(vehicle, 0);

                    vehicleCounterMap.put(vehicle, vehicleCounterMap.get(vehicle) - 1);
                }
            }

            int highestCount = Integer.MIN_VALUE;
            String selectedVehicle = "";
            for (String vehicleKey : vehicleCounterMap.keySet()) {
                if (vehicleCounterMap.get(vehicleKey) > highestCount) {
                    highestCount = vehicleCounterMap.get(vehicleKey);
                    selectedVehicle = vehicleKey;
                }
            }

            //fixme reasoning of vehicle not working this.vehicle = Vehicle.valueOf(selectedVehicle.replace("Vehicle_", "").toUpperCase());
            this.vehicle = Vehicle.CAR;

            // if the vehicle of an Agent is set to Publictransport a DataGenerator is used to get a TimeTable for trains and find an appropriate
            // train to use. As it is just a simulated process, the agent will not actually use the train. He will just be "teleported"
            // to the trainstaion in Friedberg and its property waitUntil will be set to the tick in which the train should arrive
            // at the trainstation, according to the timetable.
            // If there is no fitting connection (a train that arrives before his intendedArrivalTick) the agent will always use the car to
            // get to his destination.
            if(vehicle == Vehicle.PUBLICTRANSPORT){

                Map<String, ArrayList<TimeTableBean>> publicTransportTimeTable = DataGenerator.getPublicTransportTimeTable();
                if(publicTransportTimeTable.containsKey(currentRoutingInformation.getCurrentLocation().getLocationName())){

                    // find next possible connection
                    ArrayList<TimeTableBean> timeTableBeans = publicTransportTimeTable.get(currentRoutingInformation.getCurrentLocation().getLocationName());

                    int smallestDifference = Integer.MAX_VALUE;
                    TimeTableBean smallestDifferenceTimeTableBean = null;

                    for (TimeTableBean timeTableBean : timeTableBeans) {

                        int intendedArrivalTick = (Integer) agentProperties.get(AgentPropertiesEnum.intendedArrivalTick.name());

                        if(timeTableBean.getArrivalTick() < intendedArrivalTick){
                            int diff = timeTableBean.getArrivalTick() - intendedArrivalTick;
                            if(diff < smallestDifference){
                                smallestDifference = diff;
                                smallestDifferenceTimeTableBean = timeTableBean;
                            }
                        }
                    }

                    if(smallestDifferenceTimeTableBean != null){
                        currentRoutingInformation.setCurrentLocation(new Location(50.3326805, 8.7605256));
                        currentRoutingInformation.setWaitUntil(smallestDifferenceTimeTableBean.getArrivalTick());
                        currentRoutingInformation.setVehicle(Vehicle.FEET);

                    }else {
                        currentRoutingInformation.setVehicle(Vehicle.CAR);
                    }
                }
            }
        }
        //long timestamp = System.currentTimeMillis();
        // System.out.println(agent.getComponentIdentifier().getLocalName() + " Timeconsumption: " + (timestamp-initialTimestamp) );
        return new Future<Boolean>(true);
    }*/

    //replacing selectVehicleGoal with simple selectVehicle Method
    /*public Vehicle selectVehicle(StartEnd trip) {
        double estimatedTravelDistance = MathUtil.distFrom(this.origin.getLat(), this.origin.getLon(), this.destination.getLat(), this.destination.getLon());
        return this.journeyPlanner.selectVehicle(trip, this.currentVehicle, estimatedTravelDistance);
    }*/
    @Plan(trigger = @Trigger(goals = DriveToGoal.class))
    public IFuture<Boolean> DriveToPlan() {

        if (this.arrivedAtDestination) {
            //System.out.println("Agent ist bereits am Ziel angekommen!");
            return new Future<Boolean>(false);
        }

        //Fixme: reasoning wird nur beim erst tick des agent bisher ausgeführt? Vllt muss das mit ins drive to goal rein
        List<RelationshipInfoBean> relationshipList = this.currentRoutingInformation.getRelationshipList();
        RelationshipInfoBean currentRelationshipBean = this.currentRoutingInformation.getCurrentRelationshipBean();


        //int i = calculateCarsOnSameRoad(relationshipList, currentRelationshipBean); //fixme: why is calculated value not used?

        //fixme currently not necessary -> change once cost a dynamically dependend on on ocurring travel aspects.
        //prepareReasoning();
        //OWLOntology infOnt = ontologyLoader.getInferredOntology(this.reasoner, this.manager);

     /*  try { //for some reason if simulation is too fast connection to routing kit is refused. try not to create connections all at once
            long rand = (long) (Math.random() * 1000);
            if ((rand % 2) == 0) {
                Thread.sleep(rand);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        if (this.currentRoutingInformation.getVehicle() == null) {
            this.currentRoutingInformation.setVehicle(currentVehicle);
        }

        //System.out.println("Agent führt Plan aus, Agent's origin: "+this.origin.getLat()+" , "+this.origin.getLon()+"   "+this.origin.getLocationName());
        //System.out.println("Agent's destination: "+this.destination.getLat()+" , "+this.destination.getLon()+"   "+this.destination.getLocationName());

        //avoid rmi routingkit request when waiting
        int currentTick = Client.getInstance().getCurrentTick();
        if (currentTick < this.currentRoutingInformation.getWaitUntil()) {
            //System.out.println("Agent " + routingInfo.getAgentId() + ": ############ Waiting");
            this.currentRoutingInformation.setMovements(new ArrayList<>());
            AgentMovement agentMovement = new AgentMovement(this.currentRoutingInformation.getCurrentLocation(), this.currentRoutingInformation.getCurrentLocation(), 60);
            this.currentRoutingInformation.getMovements().add(agentMovement);
            this.currentRoutingInformation.setDrivenMeters(0);
            if (!this.currentRoutingInformation.getRouteHasChanged().containsKey(currentTick))
                this.currentRoutingInformation.getRouteHasChanged().put(currentTick, new RouteChangeBean(false, this.currentRoutingInformation.getCurrentLocation(), this.currentRoutingInformation.getVehicle()));
        }else{
            this.currentRoutingInformation = routingRMI.routeAgent(this.currentRoutingInformation);
        }


        // Todo: für Agade (ChangedRouteMap), get Rules if return True

        HashMap<Integer, RouteChangeBean> routeHasChanged = this.currentRoutingInformation.getRouteHasChanged();

        //replace with commented code below once reasoning makes sense for every tick
        //int currentTick = currentRoutingInformation.getCurrentTick();
        List<String> activeRules = new ArrayList<>();
        if (currentTick == birthTick) {
            routeHasChanged.get(currentTick).getActiveRules().addAll(this.journeyPlanner.getActiveRules());
        } else {
            routeHasChanged.get(currentTick).getActiveRules().addAll(activeRules);
        }


       /* int currentTick = currentRoutingInformation.getCurrentTick();
        OWLNamedIndividual myself = null;
        // if the route of the agent has changed in the current Tick, all active rule that have been fired by the onology will be saved in the
        // routingInformation object itself
        if (routeHasChanged.get(currentTick).getHasChanged()) {
            myself = ontoUtilBasics.getMyselfInd(infOnt);

            Set<OWLDataPropertyAssertionAxiom> dataPropertyAssertionAxioms = infOnt.getDataPropertyAssertionAxioms(myself);
            List<String> activeRules = new ArrayList<>();
            //SWRLRuleEngine swrlRuleEngine = ontologyLoader.getSWRLRuleEngine(ontology);
            for (OWLDataPropertyAssertionAxiom ax : dataPropertyAssertionAxioms) {
                String prop = ax.getProperty().asOWLDataProperty().getIRI().getRemainder().get();
                if (prop.equals("hasActiveRule")) {
                    activeRules.add(ax.getObject().getLiteral());
                }
            }
            routeHasChanged.get(currentTick).getActiveRules().addAll(activeRules);
        }*/

        // Add all movements that were made in the last iteration in the movementHistory.
        movementHistory.addAll(currentRoutingInformation.getMovements());

        TrafficJamDataHandler.getInstance().registerRoadInfo(currentRoutingInformation.getRelationshipId());

        // If the condifition is true, destination of trip has been reached"
        if (currentRoutingInformation.getCurrentLocation().getLat() == currentRoutingInformation.getTargetLocation().getLat()
                && currentRoutingInformation.getCurrentLocation().getLon() == currentRoutingInformation.getTargetLocation().getLon()
                && currentRoutingInformation.getTargetLocation().getLocationName() != null) {
            //falls am Ziel, RoutingInformation aus Backend in Simulation für Frontend schreiben.
            if (this.currentJourneyIndex != trips.size() - 1) { //check if there are more trips left, if so set next destination
                this.currentJourneyIndex++;
                this.destination = trips.get(currentJourneyIndex).getEnd();
                this.currentVehicle = journey.getTransportationMode().get(currentJourneyIndex);

                //create new RoutingInformationObject for calculating new targets and copying relevant insight information from old object
                RoutingInformation newRoutingInformation = new RoutingInformation(this.currentVehicle, currentRoutingInformation.getCurrentLocation(), this.destination, RoutingPreference.DEFAULT);
                newRoutingInformation.setAgentId(currentRoutingInformation.getAgentId());
                newRoutingInformation.setRelationshipId(currentRoutingInformation.getRelationshipId());
                newRoutingInformation.setMovements(currentRoutingInformation.getMovements());
                newRoutingInformation.setDrivenMeters(currentRoutingInformation.getDrivenMeters());
                newRoutingInformation.setRouteHasChanged(currentRoutingInformation.getRouteHasChanged());
                newRoutingInformation.setVehicleChangeMap(currentRoutingInformation.getVehicleChangeMap());
                newRoutingInformation.setPreviousRoute(currentRoutingInformation.getPreviousRoute());
                newRoutingInformation.setCurrentTick(currentRoutingInformation.getCurrentTick());
                newRoutingInformation.setCurrentRelationshipBean(currentRoutingInformation.getCurrentRelationshipBean());

                int ticksWaitUntil = currentRoutingInformation.getCurrentTick() + journeyPlanner.getLengthOfStayInTicksPerDestination();
                newRoutingInformation.setWaitUntil(ticksWaitUntil);
                this.currentRoutingInformation = newRoutingInformation;

            } else {
                this.arrivedAtDestination = true;
                this.ontologyLoader.dispose();
            }
        }


        return new Future<Boolean>(true);
    }

    @Plan(trigger = @Trigger(goals = PauseMovingGoal.class))
    public IFuture<Boolean> PauseMovingPlan() {
        prepAgentForSimResult();

        return new Future<Boolean>(true);
    }

    @SuppressWarnings("Duplicates")
    public void prepareReasoning() {
        OWLNamedIndividual myself = ontoUtilBasics.getMyselfInd(getOntology());
        Set<OWLDataProperty> dataProperties = getOntology().getDataPropertiesInSignature(Imports.INCLUDED);

        for (OWLDataProperty prop : dataProperties) {
            if (prop.getIRI().getShortForm().equals("hasArrivedValue")) {
                OWLDataPropertyAssertionAxiom axiom = getDataFactory().getOWLDataPropertyAssertionAxiom(prop, myself, arrivedAtDestination);
                getManager().addAxiom(getOntology(), axiom);
            }

            //persona properties
            else if (prop.getIRI().getShortForm().equals("hasName")) {
                OWLDataPropertyAssertionAxiom axiom = getDataFactory().getOWLDataPropertyAssertionAxiom(prop, myself, this.persona.getName());
                getManager().addAxiom(getOntology(), axiom);
            } else if (prop.getIRI().getShortForm().equals("hasGender")) {
                OWLDataPropertyAssertionAxiom axiom = getDataFactory().getOWLDataPropertyAssertionAxiom(prop, myself, this.persona.getGender());
                getManager().addAxiom(getOntology(), axiom);
            } else if (prop.getIRI().getShortForm().equals("hasEducation")) {
                OWLDataPropertyAssertionAxiom axiom = getDataFactory().getOWLDataPropertyAssertionAxiom(prop, myself, persona.getEducation());
                getManager().addAxiom(getOntology(), axiom);
            } else if (prop.getIRI().getShortForm().equals("hasMaritalStatus")) {
                OWLDataPropertyAssertionAxiom axiom = getDataFactory().getOWLDataPropertyAssertionAxiom(prop, myself, persona.getMaritalStatus());
                getManager().addAxiom(getOntology(), axiom);
            } else if (prop.getIRI().getShortForm().equals("hasAge")) {
                List<String> ageClasses = persona.getAge();
                for (String ageClass : ageClasses) {
                    OWLDataPropertyAssertionAxiom axiom = getDataFactory().getOWLDataPropertyAssertionAxiom(prop, myself, ageClass);
                    getManager().addAxiom(getOntology(), axiom);
                }
            } else if (prop.getIRI().getShortForm().equals("hasVision")) {
                OWLDataPropertyAssertionAxiom axiom = getDataFactory().getOWLDataPropertyAssertionAxiom(prop, myself, persona.getHealthProperties().getValues().get(persona.getHealthProperties().getKeys().indexOf("Vision")));
                getManager().addAxiom(getOntology(), axiom);

            } else if (prop.getIRI().getShortForm().equals("hasHearing")) {
                OWLDataPropertyAssertionAxiom axiom = getDataFactory().getOWLDataPropertyAssertionAxiom(prop, myself, persona.getHealthProperties().getValues().get(persona.getHealthProperties().getKeys().indexOf("Hearing")));
                getManager().addAxiom(getOntology(), axiom);

            } else if (prop.getIRI().getShortForm().equals("hasCognition")) {
                OWLDataPropertyAssertionAxiom axiom = getDataFactory().getOWLDataPropertyAssertionAxiom(prop, myself, persona.getHealthProperties().getValues().get(persona.getHealthProperties().getKeys().indexOf("Cognition")));
                getManager().addAxiom(getOntology(), axiom);
            } else if (prop.getIRI().getShortForm().equals("hasPhysicalAgility")) {
                OWLDataPropertyAssertionAxiom axiom = getDataFactory().getOWLDataPropertyAssertionAxiom(prop, myself, persona.getHealthProperties().getValues().get(persona.getHealthProperties().getKeys().indexOf("Physical_Agility")));
                getManager().addAxiom(getOntology(), axiom);
            }
        }

        synchroniseReasoner();
    }

    public void prepAgentForSimResult() {
        //RoutingInformation usually only has the movements of the current step, so this workaround is needed to inject the full history before converting to frontend-Agent-Datatype
        if (this.currentRoutingInformation == null || movementHistory == null) {
            System.out.println("pause goal nullpointer");
            // this.currentRoutingInformation = new RoutingInformation(this.vehicle, this.currentLocation, this.destination, RoutingPreference.valueOf(((String) this.agentProperties.get(mindSet.name())).toUpperCase()));
            //fixme: routing preference currently Default
            this.currentRoutingInformation = new RoutingInformation(this.currentVehicle, this.currentLocation, this.destination, RoutingPreference.DEFAULT);

        }
        this.currentRoutingInformation.setMovements(movementHistory);

        if (this.currentRoutingInformation.getMovements().size() > 0) {
            double totalTravelDistance = 0;
            for (AgentMovement agentMovement : this.currentRoutingInformation.getMovements()) {
                Location from = agentMovement.getFrom();
                Location to = agentMovement.getTo();
                totalTravelDistance = totalTravelDistance + MathUtil.distFrom(from.getLat(), from.getLon(), to.getLat(), to.getLon());
            }
            this.journey.setTotalTravelDistance(totalTravelDistance);

            List<MapEntryAdapter> dfs = new ArrayList<>();
            for (String key : this.decisionFactors.keySet()) {
                MapEntryAdapter entry = new MapEntryAdapter(key, (double) this.decisionFactors.get(key));
                dfs.add(entry);
            }

            List<MapEntryAdapterObject> agentProps = new ArrayList<>();
            for (String key : this.agentProperties.keySet()) {
                if(!key.equals(AgentPropertiesEnum.decisionFactors.name())){
                    MapEntryAdapterObject entry = new MapEntryAdapterObject(key, this.agentProperties.get(key));
                    agentProps.add(entry);
                }

            }

            SimulationResultBuilder.getInstance().addPopAgent(new PopAgentPojo(this.id, this.origin, this.birthTick, this.persona.getName(), agentProps, dfs, this.groceryList));
            SimulationResultBuilder.getInstance().addAgent(RoutingInformationToFrontendAgentConverter.routingInformationFromBackendToFrontendAgent(this.currentRoutingInformation, this.birthTick, this.id, this.origin, this.destination, persona.getName(), this.journey));
        }

    }

    @Override
    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }

    @Override
    public void setCurrentVehicle(Vehicle currentVehicle) {
        this.currentVehicle = currentVehicle;
    }

    private int calculateCarsOnSameRoad(List<RelationshipInfoBean> relationshipList, RelationshipInfoBean currentRelationshipBean) {

        if (currentRelationshipBean == null) return 0;
        List<RelationshipInfoBean> neededRelations = new ArrayList<>();
        double metersInTotal = 0;
        double surplus = 0;
        double shareOfLastRelation = 0;

        for (int i = currentRelationshipBean.getRelationshipListIndex(); i < relationshipList.size(); i++) {
            metersInTotal += relationshipList.get(i).getDistance();
            neededRelations.add(relationshipList.get(i));
            if (metersInTotal > TrafficConstants.CARS_ON_SAME_ROAD_DISTANCE) {
                surplus = metersInTotal - TrafficConstants.CARS_ON_SAME_ROAD_DISTANCE;
                shareOfLastRelation = 1 - (surplus / relationshipList.get(i).getDistance());

                int anzCarsOnSameRoad = RMIClient.getInstance().getAnzCarsOnSameRoad(neededRelations, shareOfLastRelation);
                carsOnSameRoad = anzCarsOnSameRoad;
                break;
            }
        }
        return 0;
    }

    public PersonaProfile getPersona() {
        return persona;
    }

    public void setPersona(PersonaProfile persona) {
        this.persona = persona;
    }

    public List<StartEnd> getTrips() {
        return trips;
    }

    public void setTrips(List<StartEnd> trips) {
        this.trips = trips;
    }

    public List<FinalDestination> getPossibleDestinations() {
        return possibleDestinations;
    }

    public void setPossibleDestinations(List<FinalDestination> possibleDestinations) {
        this.possibleDestinations = possibleDestinations;
    }

    public Map<String, Object> getAgentProperties() {
        return agentProperties;
    }

    public void setAgentProperties(Map<String, Object> agentProperties) {
        this.agentProperties = agentProperties;
    }

    @Override
    public IFuture<Boolean> processCalculationPhasePlan() {
        return null;
    }

    @Override
    public IFuture<Boolean> processSocializationPhasePlan() {
        return null;
    }

    @Override
    public void setUpAgent() {
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    @Belief
    public void setOrigin(Location location) {
        this.origin = location;
    }

    @Override
    @Belief
    public Location getOrigin() {
        return this.origin;
    }

    @Override
    @Belief
    public void setDestination(Location location) {
        this.destination = location;
    }

    @Override
    @Belief
    public Location getDestination() {
        return destination;
    }

    @Override
    @Belief
    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    @Override
    @Belief
    public Location getCurrentLocation() {
        return currentLocation;
    }

    @Override
    @Belief
    public void setArrivedAtDestination(boolean bool) {
        this.arrivedAtDestination = bool;
    }

    @Override
    @Belief
    public boolean getArrivedAtDestination() {
        return this.arrivedAtDestination;
    }

    @Override
    @Belief
    public int getBirthTick() {
        return birthTick;
    }

    @Override
    @Belief
    public void setBirthTick(int birthTick) {
        this.birthTick = birthTick;
    }

    public float getTravelDistance() {
        return travelDistance;
    }

    public void setTravelDistance(float travelDistance) {
        this.travelDistance = travelDistance;
    }

    public Map<String, Integer> getDecisionFactors() {
        return decisionFactors;
    }

    public void setDecisionFactors(Map<String, Integer> decisionFactors) {
        this.decisionFactors = decisionFactors;
    }

    public boolean isFromPopuplation() {
        return fromPopuplation;
    }

    public void setFromPopuplation(boolean fromPopuplation) {
        this.fromPopuplation = fromPopuplation;
    }

    public List<String> getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(List<String> groceryList) {
        this.groceryList = groceryList;
    }
}
