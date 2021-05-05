package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import jadexMAS.JadeXPlatformStarter;
import jadexMAS.YellowPages;
import jadexMAS.agents.IAgent;
import jadexMAS.agents.ITrafficParticipant;
import jadexMAS.agents.TrafficParticipantBDI;
import jadexMAS.goals.DirectThePlanGoal;
import model.*;
import model.frontend.*;
import model.ontologyPojos.PersonaMapProperties;
import model.ontologyPojos.PersonaProfile;
import utility.DriverTypeHelper;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Client implements IClient {

    private final JadeXPlatformStarter ps;
    private ExecutorService exe;
    public static int numberOfCreatedAgents;
    private Map<String, DiagramData> statistics;

    private Map<StartEnd, Map<Integer, List<AgentStartBean>>> agentMomentsPerRoute; // Route - <Tick, numAgents>
    private int phase;
    private String simulationID;
    public Future future;
    public FrontendResultWrapper result;
    private List<TravelTimeDetail> travelTimeDetails;
    private List<LocationDetail> locationDetails;

    public static int currentTick = 0;
    public static Boolean simulationResultDone = false;


    private final int TICKLENGTH_IN_SECONDS = 60;

    private static Client instance = null;

    protected Client() {
        this.simulationResultDone = false;
        this.ps = new JadeXPlatformStarter();
        this.exe = Executors.newFixedThreadPool(5000);
        this.currentTick = 0;
        this.agentMomentsPerRoute = new HashMap<>();
        this.numberOfCreatedAgents = 0;
        this.statistics = new HashMap<>();
        this.statistics.put("Created Agents Per Tick", new DiagramData("Created Agents Per Tick"));
        this.statistics.put("Number Of Moving Agents Per Tick", new DiagramData("Number Of Moving Agents Per Tick"));
        this.phase = 1;
        this.simulationID = UUID.randomUUID().toString();
        this.travelTimeDetails = new ArrayList<>();
        this.locationDetails = new ArrayList<>();

        exe.execute(new Runnable() {
            @Override
            public void run() {
                Configurations simConfig = ConfigurationHandler.getInstance().getConfigurations();
                simConfig.setLastTickInSimulation(0);
                System.out.println("Client started");
                Map<AgentPropertiesEnum, Object> agentProperties = new HashMap<>();
                ps.deployAgent("Director", "jadexMAS/agents/TrafficDirectorBDI.class", agentProperties);
            }
        });
    }

    public void setFuture(Future future) {
        this.future =
                future;
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public void continueSimulation(){
        this.phase++;
        this.exe = Executors.newFixedThreadPool(5000);
        YellowPages.getDirector().getComponentFeature(IBDIAgentFeature.class).dispatchTopLevelGoal(new DirectThePlanGoal()).get();
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    public static void resetInstance() {
        if (instance != null){
            instance.shutdownExecutorService();
        }
        instance = null;
    }

    @Override
    public void equipParticipatingAgentsWithOntologyPlan() {
        exe.execute(new Runnable() {

            int agentSize = YellowPages.getAllAgents().size();

            @Override
            public void run() {
                //System.out.println("in equip " + agentSize);
                if (agentSize > 0) {
                    ArrayList<Boolean> results = new ArrayList<>();
                    for (IAgent ia : YellowPages.getAllAgents()) {

                        if (ia instanceof ITrafficParticipant) {
                            if (((ITrafficParticipant) ia).getOrigin() == ((ITrafficParticipant) ia).getCurrentLocation()) {
                                //System.out.println(ia);

                                //Execute plan directly
                                //IFuture<Boolean> future = ia.runPlan(
                                //Constants.plnEquipParticipantsWithOntologyPlan,
                                //new Future<Boolean>().getClass());

                                //Set goal to execute specific plan

                                IFuture<Boolean> future = ia.setGoal("EquipAgentsWithOntologyGoal");
                                future.addResultListener(new IResultListener<Boolean>() {
                                    @Override
                                    public void resultAvailable(Boolean result) {
                                        synchronized (results) {
                                            results.add(result);
                                            if (results.size() == agentSize) {
                                                try {
                                                    //System.out.println("Jeder Agent hat eine Ontology bekommen!");

                                                    synchronized (YellowPages.getDirector()) {
                                                        YellowPages.getDirector().notifyAll();
                                                    }
                                                    //SERVER.equipParticipatingAgentsWithOntologyPlanFinished(ADDRESS);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void exceptionOccurred(Exception exception) {
                                        exception.printStackTrace();
                                    }
                                });
                            }
                        }
                    }
                } else {
                    synchronized (YellowPages.getDirector()) {
                        YellowPages.getDirector().notifyAll();
                    }
                }
            }
        });
    }

    @Override
    public void flushReasoners() {
        exe.execute(new Runnable() {
            int agentSize = YellowPages.getAllAgents().size();
            ArrayList<Void> results = new ArrayList<>();

            @Override
            public void run() {
                // synchronise all jadex.agents
                //System.out.println(agentSize);
                if (agentSize > 0) {
                    for (IAgent ag : YellowPages.getAllAgents()) {

                        IFuture<Void> future = ag.synchroniseReasoner();

                        future.addResultListener(new IResultListener<Void>() {
                            @Override
                            public void resultAvailable(Void result) {
                                synchronized (results) {
                                    results.add(result);
                                    if (results.size() == agentSize) {

                                        //System.out.println("Jeder Agent hat Reasoner geflusht!");
                                        synchronized (YellowPages.getDirector()) {
                                            YellowPages.getDirector().notifyAll();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void exceptionOccurred(Exception exception) {
                                exception.printStackTrace();
                            }
                        });
                    }
                } else {
                    synchronized (YellowPages.getDirector()) {
                        YellowPages.getDirector().notifyAll();
                    }
                }
            }
        });

    }

    public void planAgentJourneys() {
        exe.execute(new Runnable() {
            int agentSize = YellowPages.getAllAgents().size();

            ArrayList<Boolean> results = new ArrayList<>();

            @Override
            public void run() {
                if (agentSize > 0) {
                    for (IAgent p : YellowPages.getAllAgents()) {
                        //Set goal to execute specific plan
                        IFuture<Boolean> future = p.setGoal("PlanJourneyGoal");
                        future.addResultListener(new IResultListener<Boolean>() {
                            @Override
                            public void resultAvailable(Boolean result) {
                                synchronized (results) {
                                    results.add(result);
                                    if (results.size() == agentSize) {
                                        try {
                                            synchronized (YellowPages.getDirector()) {
                                                YellowPages.getDirector().notifyAll();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void exceptionOccurred(Exception exception) {
                                exception.printStackTrace();
                            }
                        });
                    }
                } else {
                    synchronized (YellowPages.getDirector()) {
                        YellowPages.getDirector().notifyAll();
                    }
                }
            }
        });
    }


    @Override
    public void processActingPhase() {
        exe.execute(new Runnable() {
            //final int agentSize = PropertyDAO.getInstance().load().getNumberOfAgents();
            int agentSize = YellowPages.getAllAgents().size();

            ArrayList<Boolean> results = new ArrayList<>();

            @Override
            public void run() {
                //System.out.println(agentSize);
                if (agentSize > 0) {
                    int numAgentsMovingAtTick = 0;
                    for (IAgent p : YellowPages.getAllAgents()) {


                        // statistics : Number Of Moving Agents Per Tick
                        if (p instanceof TrafficParticipantBDI){
                            boolean arrivedAtDestination = ((TrafficParticipantBDI) p).getArrivedAtDestination();
                            if (!arrivedAtDestination){
                                numAgentsMovingAtTick++;
                            }
                        }

                        //Set goal to execute specific plan
                        IFuture<Boolean> future = p.setGoal("ProcessActingPhaseGoal");

                        future.addResultListener(new IResultListener<Boolean>() {
                            @Override
                            public void resultAvailable(Boolean result) {
                                synchronized (results) {
                                    results.add(result);

                                    if (results.size() == agentSize) {
                                        try {
                                            //System.out.println("Jeder Agent hat seine Aktion durchgeführt!");
                                            synchronized (YellowPages.getDirector()) {
                                                YellowPages.getDirector().notifyAll();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void exceptionOccurred(Exception exception) {
                                exception.printStackTrace();
                            }
                        });
                    }

                    // statistics : Number Of Moving Agents Per Tick
                    DiagramData numMovingAgentsStatistic = statistics.get("Number Of Moving Agents Per Tick");
                    numMovingAgentsStatistic.getxCoordinates().add((double) currentTick);
                    numMovingAgentsStatistic.getyCoordinates().add((double) numAgentsMovingAtTick);
                }else {
                    synchronized (YellowPages.getDirector()) {
                        YellowPages.getDirector().notifyAll();
                    }
                }
            }
        });
    }

    @Override
    public void shutdownExecutorService() {
        exe.shutdown();
        System.out.println("Executor service shut down");
    }

    @Override
    public boolean executorFinished() {
        return exe.isTerminated();
        //return isPaused;
    }

    @Override
    public void createAgentsAtTick(int tick){
        exe.execute(new Runnable() {
            @Override
            public void run() {
                Configurations simConfig = ConfigurationHandler.getInstance().getConfigurations();
                Map<Double, Double> tmpStatMap = new TreeMap<>();
                int numTicksToSimulate = (simConfig.getSimulationTime() * 60 * 60) / getTICKLENGTH_IN_SECONDS();

                if(simConfig.isFromPopulation()){

                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        Population populationConfig = mapper.readValue( simConfig.getPopulationConfig(), Population.class);
                        List<PopAgentPojo> popAgents = populationConfig.getAgents();
                        Map<Integer, List<PopAgentPojo>> popAgentsAtTick = new TreeMap<>();


                        for(PopAgentPojo popAgent: popAgents){
                            int birthTick = popAgent.getBirthTick();

                            if (!popAgentsAtTick.containsKey(birthTick)) {
                                List<PopAgentPojo> popAgentPojoListAtTick = new ArrayList<>();
                                popAgentPojoListAtTick.add(popAgent);
                                popAgentsAtTick.put(birthTick, popAgentPojoListAtTick);
                            } else {
                                popAgentsAtTick.get(birthTick).add(popAgent);
                            }

                            //statistical values
                            if (!tmpStatMap.containsKey((double) birthTick)) {

                                tmpStatMap.put((double) birthTick, 1d);
                            } else {
                                tmpStatMap.put((double) birthTick, tmpStatMap.get((double) birthTick) + 1);
                            }
                        }

                        if (popAgentsAtTick.containsKey(tick)) {
                            List<PopAgentPojo> agentsForThisTick = popAgentsAtTick.get(tick);
                            for(PopAgentPojo popAgent : agentsForThisTick){
                                String agentName = "TrafficParticipant" + System.currentTimeMillis();
                                String classFile = "jadexMAS/agents/TrafficParticipantBDI.class";
                                Map<AgentPropertiesEnum, Object> agentProperties =  new HashMap<>();

                                for(MapEntryAdapterObject adapterObject: popAgent.getAgentProperties()){
                                    switch (adapterObject.getKey()) {
                                        case "persona":
                                            PersonaProfile popPersona = new PersonaProfile();
                                            Map<String, Object> popPersonaMap = (Map<String, Object>) adapterObject.getValue();
                                            popPersona.setId( (String) popPersonaMap.get("id"));
                                            popPersona.setPic ((String) popPersonaMap.get("pic"));
                                            popPersona.setName ((String) popPersonaMap.get("name"));
                                            popPersona.setOccupation ((String) popPersonaMap.get("occupation"));
                                            popPersona.setAge ((List<String>) popPersonaMap.get("age"));
                                            popPersona.setDescription((String) popPersonaMap.get("description"));
                                            popPersona.setGender ((String) popPersonaMap.get("gender"));
                                            popPersona.setMaritalStatus ((String) popPersonaMap.get("maritalStatus"));
                                            popPersona.setEducation((String) popPersonaMap.get("education"));
                                            popPersona.setPercentage ((int) popPersonaMap.get("percentage"));

                                            PersonaMapProperties popPersonaHealthProperties = new PersonaMapProperties();
                                            popPersonaHealthProperties.setKeys( ((Map<String, List>) popPersonaMap.get("healthProperties")).get("keys"));
                                            popPersonaHealthProperties.setValues( ((Map<String, List>) popPersonaMap.get("healthProperties")).get("values"));
                                            popPersona.setHealthProperties(popPersonaHealthProperties);

                                            PersonaMapProperties popPersonaShoppingProperties = new PersonaMapProperties();
                                            popPersonaShoppingProperties.setKeys(((Map<String, List>) popPersonaMap.get("shoppingProperties")).get("keys"));
                                            popPersonaShoppingProperties.setValues(((Map<String, List>) popPersonaMap.get("shoppingProperties")).get("values"));
                                            popPersona.setShoppingProperties(popPersonaShoppingProperties);

                                            agentProperties.put(AgentPropertiesEnum.persona, popPersona);
                                            break;
                                        case "actualStartTick":
                                            agentProperties.put(AgentPropertiesEnum.actualStartTick, adapterObject.getValue());
                                            break;
                                        case "isActivityModelled":
                                            agentProperties.put(AgentPropertiesEnum.isActivityModelled, adapterObject.getValue());
                                            break;
                                        case "intendedArrivalTick":
                                            agentProperties.put(AgentPropertiesEnum.intendedArrivalTick, adapterObject.getValue());
                                            break;
                                        case "birthTick":
                                            agentProperties.put(AgentPropertiesEnum.birthTick, adapterObject.getValue());
                                            break;
                                        case "intendedStartTick":
                                            agentProperties.put(AgentPropertiesEnum.intendedStartTick, adapterObject.getValue());
                                            break;
                                        case "expectedArrivalTick":
                                            agentProperties.put(AgentPropertiesEnum.expectedArrivalTick, adapterObject.getValue());
                                            break;
                                        case "id":
                                            agentProperties.put(AgentPropertiesEnum.id, adapterObject.getValue());
                                            break;
                                    }
                                }

                                agentProperties.put(AgentPropertiesEnum.possibleDestinations, simConfig.getFinalDestinations());
                                List<StartEnd> trips = new ArrayList();
                                StartEnd start = new StartEnd();
                                start.setStart(popAgent.getOrigin());
                                trips.add(start);
                                agentProperties.put(AgentPropertiesEnum.trips, trips);

                                Map<String, Integer> decisionFactors = new HashMap<>();
                                for( MapEntryAdapter dfAdapter : popAgent.getDecisionFactors()){
                                    decisionFactors.put(dfAdapter.getKey(), dfAdapter.getValue().intValue());
                                }
                                agentProperties.put(AgentPropertiesEnum.decisionFactors, decisionFactors);
                                agentProperties.put(AgentPropertiesEnum.fromPopulation, simConfig.isFromPopulation());
                                agentProperties.put(AgentPropertiesEnum.groceryList, popAgent.getGroceryList());

                                ps.deployAgent(agentName, classFile, agentProperties);
                                numberOfCreatedAgents++;
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    // reset when one hour has passed
                    if (currentTick % 60 == 0) {
                        agentMomentsPerRoute = new HashMap<>();
                    }
                    boolean isActivityDemandModelling = simConfig.getAgentGeneratorType().equals("Activity");

                    // distinguish between OD and Activity based agent creation
                    if (agentMomentsPerRoute.isEmpty()) {// fill agentMoments
                        if (isActivityDemandModelling) {
                            agentMomentsPerRoute = DriverTypeHelper.generateAgentMomentsPerStartLocation(simConfig, currentTick);
                        } else {// OD Matrix
                            // fill agentMoments
                            agentMomentsPerRoute = DriverTypeHelper.generateAgentMomentsPerRoute(simConfig, currentTick);
                        }
                    }

                    for (Map.Entry<StartEnd, Map<Integer, List<AgentStartBean>>> entry : agentMomentsPerRoute.entrySet()) {
                        if (entry.getValue().containsKey(tick)) {
                            for (int i = 0; i < entry.getValue().get(tick).size(); i++) {
                                List<StartEnd> trips = new ArrayList();
                                List<AgentStartBean> agentStartBeans = entry.getValue().get(tick);
                                String agentName = "TrafficParticipant" + System.currentTimeMillis();
                                String classFile = "jadexMAS/agents/TrafficParticipantBDI.class";
                                Map<AgentPropertiesEnum, Object> agentProperties = new HashMap<>();
                                trips.add(entry.getKey());

                                agentProperties.put(AgentPropertiesEnum.trips, trips);
                                agentProperties.put(AgentPropertiesEnum.birthTick, tick);
                                agentProperties.put(AgentPropertiesEnum.id, numberOfCreatedAgents);
                                agentProperties.put(AgentPropertiesEnum.actualStartTick, agentStartBeans.get(i).getActualStartTick());
                                agentProperties.put(AgentPropertiesEnum.intendedStartTick, agentStartBeans.get(i).getIntendedStartTick());
                                agentProperties.put(AgentPropertiesEnum.expectedArrivalTick, agentStartBeans.get(i).getExpectedArrivalTick());
                                agentProperties.put(AgentPropertiesEnum.intendedArrivalTick, agentStartBeans.get(i).getDesiredArrivalTick());
                                agentProperties.put(AgentPropertiesEnum.isActivityModelled, isActivityDemandModelling);
                                agentProperties.put(AgentPropertiesEnum.possibleDestinations, simConfig.getFinalDestinations());

                                PersonaProfile agentPersona = DriverTypeHelper.determinePersonaProfile(simConfig);
                                agentProperties.put(AgentPropertiesEnum.persona, agentPersona);
                                agentProperties.put(AgentPropertiesEnum.fromPopulation, simConfig.isFromPopulation());

                                ps.deployAgent(agentName, classFile, agentProperties);
                                numberOfCreatedAgents++;
                            }
                        }
                    }

                    // statistics : Create Agents Per Tick
                    for (int i = currentTick; i <= numTicksToSimulate + currentTick; i++) {
                        for (Map.Entry<StartEnd, Map<Integer, List<AgentStartBean>>> entry : agentMomentsPerRoute.entrySet()) {
                            if (tmpStatMap.keySet().contains((double) i)) {
                                if (entry.getValue().containsKey(i)) {
                                    tmpStatMap.put((double) i, tmpStatMap.get((double) i) + entry.getValue().get(i).size());
                                }
                            } else {
                                if (entry.getValue().containsKey(i)) {
                                    tmpStatMap.put((double) i, (double) entry.getValue().get(i).size());
                                }
                            }
                        }
                    }
                }

                DiagramData createdAgentsPerTick = statistics.get("Created Agents Per Tick");
                createdAgentsPerTick.getxCoordinates().addAll(new ArrayList<>(tmpStatMap.keySet()));
                createdAgentsPerTick.getyCoordinates().addAll(new ArrayList<>(tmpStatMap.values()));

                setCurrentTick(tick);
                synchronized (YellowPages.getDirector()) {
                    YellowPages.getDirector().notifyAll();
                }
            }
        });
    }


    @Override
    public void pauseAgents(){
        exe.execute(new Runnable() {
            int agentSize = YellowPages.getAllAgents().size();

            ArrayList<Boolean> results = new ArrayList<>();

            @Override
            public void run() {
                if (agentSize > 0) {
                    for (IAgent p : YellowPages.getAllAgents()) {
                        //Set goal to execute specific plan
                        IFuture<Boolean> future = p.setGoal("PauseMovingGoal");

                        future.addResultListener(new IResultListener<Boolean>() {
                            @Override
                            public void resultAvailable(Boolean result) {
                                synchronized (results) {
                                    results.add(result);
                                    if (results.size() == agentSize) {
                                        try {
                                            synchronized (YellowPages.getDirector()) {
                                                YellowPages.getDirector().notifyAll();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void exceptionOccurred(Exception exception) {
                                exception.printStackTrace();
                            }
                        });
                    }
                } else {
                    synchronized (YellowPages.getDirector()) {
                        YellowPages.getDirector().notifyAll();
                    }
                }
            }
        });

    }

    public int getTICKLENGTH_IN_SECONDS() {
        return TICKLENGTH_IN_SECONDS;
    }

    public Map<String, DiagramData> getStatistics() {
        return statistics;
    }

    public String getSimulationID() {
        return simulationID;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }



    public List<TravelTimeDetail> getTravelTimeDetails() {
        return travelTimeDetails;
    }

    public void setTravelTimeDetails(List<TravelTimeDetail> travelTimeDetails) {
        this.travelTimeDetails = travelTimeDetails;
    }

    public List<LocationDetail> getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(List<LocationDetail> locationDetails) {
        this.locationDetails = locationDetails;
    }

}

