package controllers;

import model.*;
import model.frontend.*;
import model.statisticalData.Statistics;
import rmi.RMIClient;
import rmi.model.*;
import rmi.model.DumpDestination;
import rmi.model.FinalDestination;

import java.util.*;

@SuppressWarnings("Duplicates")
public class SimulationResultBuilder {


    private List<FrontendAgent> synchronizedList;

    private List<PopAgentPojo> synchronizedListPopAgent;

    private int numberOfAgents;

    private static SimulationResultBuilder ourInstance;

    public static SimulationResultBuilder getInstance() {
        if (ourInstance == null)
            ourInstance = new SimulationResultBuilder();
        return ourInstance;
    }

    public static void resetInstance() {
        ourInstance = null;
    }

    private SimulationResultBuilder() {
        this.synchronizedList = Collections.synchronizedList(new ArrayList());
        this.synchronizedListPopAgent = Collections.synchronizedList(new ArrayList());
        this.numberOfAgents = 0;
    }



    private final String DEST_TYPE_FINAL = "FINAL", DEST_TYPE_DUMP = "DUMP";

    public FrontendResultWrapper finalizeSimulation() {
        Client client = Client.getInstance();
        Simulation simulation = new Simulation(client.getSimulationID());
        simulation.setAgents(synchronizedList);
        simulation.setCurrentTick(Client.getInstance().getCurrentTick());

        Statistics statistics = new Statistics();
        statistics.setCreatedAgentsPerTick(Client.getInstance().getStatistics().get("Created Agents Per Tick"));
        statistics.setNumberOfMovingAgentsPerTick(Client.getInstance().getStatistics().get("Number Of Moving Agents Per Tick"));
        simulation.setStatisticalKPIs(statistics);
        simulation.setLocationDetails(Client.getInstance().getLocationDetails());
        simulation.setTravelTimeDetails(Client.getInstance().getTravelTimeDetails());


        Configurations configurations = ConfigurationHandler.getInstance().getConfigurations();
        simulation.setConfigurations(configurations);

        HashMap<String, ArrayList<RouteChangeBean>> mergedRouteChanges = new HashMap<>();


        for (FrontendAgent agent : synchronizedList) {
            Location origin = agent.getOrigin();
            Location destination = agent.getDestination();

            String mapKey = origin.getLocationName()+";"+destination.getLocationName();
            if(!mergedRouteChanges.containsKey(mapKey)){
                mergedRouteChanges.put(mapKey, new ArrayList<>());
            }

            HashMap<Integer, RouteChangeBean> routeHasChanged = agent.getRouteHasChanged();

            for (RouteChangeBean routeChangeBean : routeHasChanged.values()) {
                if(routeChangeBean.getHasChanged()){
                    mergedRouteChanges.get(mapKey).add(routeChangeBean);
                }
            }
        }

        // Destination Statistics
        DumpDestination dumpDestination = RMIClient.getInstance().getDumpDestination();
        List<FinalDestination> finalDestinations = RMIClient.getInstance().getFinalDestinations();

        for (FinalDestination finalDestination : finalDestinations) {
            Client.getInstance().getLocationDetails().add(generateLocationDetail(finalDestination, simulation, DEST_TYPE_FINAL));
        }

        Client.getInstance().getLocationDetails().add(generateLocationDetail(dumpDestination, simulation, DEST_TYPE_DUMP));

        // travel time statistics
        for (StartEnd startEnd : configurations.getDestinationMatrix()) {
            Location from = startEnd.getStart();
            Location to = startEnd.getEnd();

            //filter jadex.agents that really with same start end
            Map<String, List<FrontendAgent>> relevantAgents = new HashMap<>();
            for (FrontendAgent agent : synchronizedList) {
                if (agent.getBirthTick() > configurations.getLastTickInSimulation()
                        && agent.getOrigin().equals(from)
                        && agent.getDestination().equals(to)) {
                    if (!relevantAgents.containsKey(agent.getPersonaProfile())) {
                        List<FrontendAgent> agentList = new ArrayList<>();
                        agentList.add(agent);
                        relevantAgents.put(agent.getPersonaProfile(), agentList);
                    } else {
                        relevantAgents.get(agent.getPersonaProfile()).add(agent);
                    }
                }
            }

            for (Map.Entry<String, List<FrontendAgent>> entry : relevantAgents.entrySet()) {
                int num_agents = entry.getValue().size();
                float avg_time = 0;
                for (FrontendAgent a : entry.getValue()) {
                    avg_time += a.getTotalTravelTime();
                }
                avg_time /= num_agents;

                // write to BEAN
                TravelTimeDetail timeDetailBean = new TravelTimeDetail();
                timeDetailBean.setSimId(simulation.getSimulationID());
                timeDetailBean.setPhase(client.getPhase());
                timeDetailBean.setAvgTravelTime(avg_time);
                timeDetailBean.setDriverType(entry.getKey());
                timeDetailBean.setNumberAgents(num_agents);
                timeDetailBean.setLocationFrom(new Location(from.getLat(), from.getLon()));
                timeDetailBean.setLocationTo(new Location(to.getLat(), to.getLon()));

                Client.getInstance().getTravelTimeDetails().add(timeDetailBean);
            }
        }

        return new FrontendResultWrapper(simulation, new Population(getSynchronizedListPopAgent()));
    }


    private LocationDetail generateLocationDetail(Destination destination, Simulation simulation, String destinationType) {

        LocationDetail locationDetail = new LocationDetail();
        locationDetail.setSimId(Client.getInstance().getSimulationID());
        locationDetail.setDirectParkingSlots(destination.getCapacity());
        locationDetail.setDirectParkedCars(destination.getCurrentlyWaiting());

        if (destinationType.equals(DEST_TYPE_FINAL)) {
            FinalDestination finalDestination = (FinalDestination) destination;
            HashMap<Long, ParkingDestination> parkingSlots = finalDestination.getParkingSlots();
            int surroundingParkingSlots = 0;
            int surroundingParkedCars = 0;
            for (ParkingDestination parkingDestination : parkingSlots.values()) {
                surroundingParkedCars += parkingDestination.getCurrentlyWaiting();
                surroundingParkingSlots += parkingDestination.getCapacity();
            }
            locationDetail.setSurroundingParkingSlots(surroundingParkingSlots);
            locationDetail.setSurroundingParkedCars(surroundingParkedCars);
            locationDetail.setParkingArea(finalDestination.getAreaDiameterForParking());
        }

        locationDetail.setLocation(destination.getLocation());
        locationDetail.setLocationType(destinationType);

        return locationDetail;

    }

    public void registerAgent() {
        this.numberOfAgents++;
    }

    public List<FrontendAgent> getSynchronizedList() {
        return synchronizedList;
    }

    public void addAgent(FrontendAgent agent) {
        this.synchronizedList.add(agent);
    }

    public List<PopAgentPojo> getSynchronizedListPopAgent() {
        return synchronizedListPopAgent;
    }

    public void addPopAgent(PopAgentPojo agent) {
        this.synchronizedListPopAgent.add(agent);
    }


}
