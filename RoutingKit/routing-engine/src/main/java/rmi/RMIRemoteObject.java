package rmi;

import neo4jAccessor.Neo4JQueryFactory;
import org.neo4j.driver.internal.InternalRelationship;
import org.neo4j.driver.internal.value.PathValue;
import org.neo4j.driver.v1.types.Node;
import rmi.model.*;
import util.DistanceCalcUtil;
import util.MathUtil;
import util.PathUtil;
import util.SystemConfiguration;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/**
 * The type Rmi remote object.
 *
 * @author Joshua Prim, Jannik Enenkel, Jannik Geyer
 */
public class RMIRemoteObject extends UnicastRemoteObject implements RMIInterface {

    // private static final long serialVersionUID = 7945042323666412162L;
    private int tickLengthInSeconds;
    private List<Long> blockedStreets = new ArrayList<>();
    private Map<String, Integer> modifiedSpeedMap = new HashMap<>();
    private Neo4JQueryFactory neo4JQueryFactory;

    /**
     * Instantiates a new Rmi remote object.
     *
     * @throws RemoteException the remote exception
     */
    public RMIRemoteObject() throws RemoteException {
        this.neo4JQueryFactory = Neo4JQueryFactory.getInstance();
    }


    @Override
    public String ping() throws RemoteException {
        try { //for some reason if simulation is too fast connection to routing kit is refused. try not to create connections all at once
            long rand = (long) (Math.random() * 1000);
            if ((rand % 2) == 0) {
                Thread.sleep(rand);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "pong";
    }

    @Override
    public boolean nextTick(int tickLengthInSeconds, HashMap<Long, Integer> roadInfo, List<TrafficActionObject> trafficActions) throws RemoteException {
        this.tickLengthInSeconds = tickLengthInSeconds;
        this.blockedStreets.clear();
        this.modifiedSpeedMap.clear();
        // traffic actions
        if (trafficActions != null) {
            for (TrafficActionObject trafficAction : trafficActions) {
                if (trafficAction.isBlock()) {
                    this.blockedStreets.addAll(blockStreet(trafficAction.getLocations()));
                } else {
                    this.modifiedSpeedMap.putAll(changeMaxSpeed(trafficAction.getLocations(), trafficAction.getMaxspeed()));
                }
            }
        }

        // traffic jam
        if (roadInfo != null) {
            return neo4JQueryFactory.updateNumberOfCarsOnRoads(roadInfo);
        } else {
            return true;
        }
    }

    // Mehtod, called by Agade in order to route an Agent.
    @Override
    public RoutingInformation routeAgent(RoutingInformation routingInfo, int currentTick) throws RemoteException {
        MathUtil mathUtil = new MathUtil();
        PathUtil pathUtil = new PathUtil();
        System.out.println("Agent " + routingInfo.getAgentId() + ": NextTick ->");


        // If statement. If the attribute routingInfo.getWaitUntil() is not set to -1 one, an agents will use a train, in order to arrive.
        // The Attribute waitUntil reefers to a tick that was set in the initialization process
     /*   if (routingInfo.getCurrentTick() < routingInfo.getWaitUntil()) {
            System.out.println("Agent " + routingInfo.getAgentId() + ": ############ Waiting");
            routingInfo.setMovements(new ArrayList<>());
            AgentMovement agentMovement = new AgentMovement(routingInfo.getCurrentLocation(), routingInfo.getCurrentLocation(), 60);
            routingInfo.getMovements().add(agentMovement);
            routingInfo.setDrivenMeters(0);
            if (!routingInfo.getRouteHasChanged().containsKey(currentTick))
                routingInfo.getRouteHasChanged().put(currentTick, new RouteChangeBean(false, routingInfo.getCurrentLocation(), routingInfo.getVehicle()));
            return routingInfo;
        } */


        // An Agent does not have a final Destination if it is its first iteration.
        if (routingInfo.getFinalDestination() == null) {
            FinalDestination finalDestination = SystemConfiguration.getInstance().findDestination(routingInfo.getTargetLocation());
            routingInfo.setFinalDestination(finalDestination);
            routingInfo.setTempFinalDestination(finalDestination);
            routingInfo.getPossibleFinalDestinations().addAll(SystemConfiguration.getInstance().getFinalDestinations());

            // If the agent is a bike rider, the following codesections checks if the finalDestination set before
            // is actually reachable by bike. If not an internal Destination will be calculated which is basically
            //  a Location that is reachable by bike and close to the finalDestination of the agent.
            if (routingInfo.getVehicle() == Vehicle.BIKE) {

                List<Location> possibleBikeTargetLocation = neo4JQueryFactory.findPossibleBikeTargetLocation(routingInfo, blockedStreets, modifiedSpeedMap);

                double fastest = Double.MAX_VALUE;
                Location nearestLocationToFinalDestination = null;

                for (Location location : possibleBikeTargetLocation) {
                    double timeInSeconds = calculatefastestWayToDestination(location, finalDestination.getLocation());
                    if (fastest > timeInSeconds) {
                        fastest = timeInSeconds;
                        nearestLocationToFinalDestination = location;
                    }
                }

                if (!(finalDestination.getLocation().getLat() == nearestLocationToFinalDestination.getLat() && finalDestination.getLocation().getLon() == nearestLocationToFinalDestination.getLon())) {

                    routingInfo.setTargetLocation(nearestLocationToFinalDestination);
                    InternalDestination internalDestination = new InternalDestination();
                    internalDestination.setCapacity(Integer.MIN_VALUE);
                    internalDestination.setCurrentlyWaiting(Integer.MIN_VALUE);
                    internalDestination.setLocation(nearestLocationToFinalDestination);

                    SystemConfiguration.getInstance().getInternalDestinations().add(internalDestination);
                }

            }

        }

        if (routingInfo.getNodeIdFrom() == 0) {
            int nodeId = neo4JQueryFactory.findNodeNearestToLocation(routingInfo.getCurrentLocation());
            routingInfo.setNodeIdFrom(nodeId);
            routingInfo.setNodeIdTo(nodeId); // this is necessary for initialization
        }
        if (routingInfo.getNodeIdTarget() == 0) {
            int nodeId = neo4JQueryFactory.findNodeNearestToLocation(routingInfo.getTargetLocation());
            routingInfo.setNodeIdTarget(nodeId);
        }

        // The previousRouteIds are needed in order to determine wheter the agent had a routeChange or not.
        // The List of Route Ids represent a complete path calculated by the Routing Engine. In order to determine
        // wheter the route has changed or not the currentRoute that will be calculated in the next lines of code
        // are getting compared to the previousRouteIds Array
        ArrayList<Long> prevRouteIds = routingInfo.getPreviousRoute();

        routingInfo.setMovements(new ArrayList<>());
        String routingPreference = routingInfo.getRoutingPreference().name();

        PathValue path = null;
        path = (PathValue) neo4JQueryFactory.findPath(routingInfo.getVehicle(), routingInfo.getNodeIdFrom(), routingInfo.getNodeIdTarget(), routingPreference, this.blockedStreets, this.modifiedSpeedMap, routingInfo.getHateFactorCarsOnSameRoad());

        ArrayList<InternalRelationship> route;
        ArrayList<Long> routeIds = new ArrayList<>();
        boolean routeHasChanged = false;

        ArrayList<Node> nodes;
        if (path != null) {
            route = pathUtil.getRelationshipsFromPath(path);
            nodes = pathUtil.getNodesFromPath(path);

            route.forEach(routeObj -> routeIds.add(routeObj.id()));
            routingInfo.setPreviousRoute(routeIds);
        } else {
            // that means current position and target position are either identical
            // or that close to each other that there is no meaningful route
            // so let agent terminate.
            AgentMovement agentMovement = new AgentMovement(routingInfo.getCurrentLocation(), routingInfo.getTargetLocation(), 0);
            routingInfo.getMovements().add(agentMovement);
            routingInfo.setCurrentLocation(routingInfo.getTargetLocation());

            if (!routingInfo.getRouteHasChanged().containsKey(currentTick))
                routingInfo.getRouteHasChanged().put(currentTick, new RouteChangeBean(routeHasChanged, routingInfo.getCurrentLocation(), routingInfo.getVehicle()));

            return routingInfo;
        }

        // code that determines whether an agent had a routeChange or not
        if (prevRouteIds != null) {
            if (prevRouteIds.size() > 0) {
                boolean hasChanged = compareMovementsOfAgent(prevRouteIds, routeIds);
                routingInfo.getRouteHasChanged().put(currentTick, new RouteChangeBean(hasChanged, routingInfo.getCurrentLocation(), routingInfo.getVehicle()));

            } else {
                routingInfo.getRouteHasChanged().put(currentTick, new RouteChangeBean(true, routingInfo.getCurrentLocation(), routingInfo.getVehicle()));

            }
        }
        if (!routingInfo.getRouteHasChanged().containsKey(currentTick))
            routingInfo.getRouteHasChanged().put(currentTick, new RouteChangeBean(routeHasChanged, routingInfo.getCurrentLocation(), routingInfo.getVehicle()));

        // Runs into Size = 0, if remaining distance is to short to find a route
        // -> Port Agent to destination, in order to prevent an error
        if (route.size() == 0) {
            routingInfo.setCurrentLocation(routingInfo.getTargetLocation());
        } else {

            routingInfo = mathUtil.calculateMoveDistance(route, nodes, routingInfo, 0, tickLengthInSeconds, modifiedSpeedMap, true);

            //get current relationship by comparing movements with the route array
            ArrayList<AgentMovement> movements = routingInfo.getMovements();


            movements.get(0).setVehicle(routingInfo.getVehicle());


            int lastIndex = movements.size() - 1;
            InternalRelationship currentRealtionship = route.get(lastIndex);

            RelationshipInfoBean currentRelationshipInfoBean = new RelationshipInfoBean(currentRealtionship.id(), currentRealtionship.get("maxSpeed").asDouble(), currentRealtionship.get("distance").asDouble());
            currentRelationshipInfoBean.setRelationshipListIndex(lastIndex);

            List<RelationshipInfoBean> relationshipList = new ArrayList<>();
            for (InternalRelationship internalRelationship : route) {
                relationshipList.add(new RelationshipInfoBean(internalRelationship.id(), internalRelationship.get("maxSpeed").asDouble(), internalRelationship.get("distance").asDouble()));
            }

            routingInfo.setCurrentRelationshipBean(currentRelationshipInfoBean);
            routingInfo.setRelationshipList(relationshipList);

            // if not arrived at target location we must find out the agent's new location
            if ((routingInfo.getTargetLocation().getLat() != routingInfo.getCurrentLocation().getLat()) &
                    (routingInfo.getTargetLocation().getLon() != routingInfo.getCurrentLocation().getLon())) {
                routingInfo = mathUtil.calculateNewAgentPosition(route, nodes, routingInfo);
            }
        }

        // If statement that checks if an agent reached a Destination
        if (routingInfo.getCurrentLocation() == routingInfo.getTargetLocation()) {

            // Method to convert the Location into an actual Destination.
            // The SystemConfigurations class stores all types of Destination and determines which type of a Destination the currentLocation is.
            Destination destination = SystemConfiguration.getInstance().findDestination(routingInfo.getCurrentLocation(), routingInfo);


            /*
            the following sections explains the FinalDestination park finding Algorithm in easy words:

            1. If the agent drives a car:
                a: is there place to park at the finalDestination?
                    true -> park
                            check if it is the agents actual finalDestination
                            - true -> terminate
                            - false -> Change targetLocation to the actual FinalDestination and change the agents vehicle to Feet
                b: Are there any parkingSlots at this FinalDestination?
                    true -> navigate Agent to nearest Parking Slot (Change its RoutingInfo object)
                c: Is there an alternative Final Destination?
                    true -> navigate to the neareast alternative Final Destination (Set is as TempFinalDestination)
                d: Let Agent drive to Dump location

             2. If the agent is walking (using the vehicle FEET)
                true -> Let the agent terminate

             3. if the agent is using a bike
                true -> let the agent terminate
             */
            if (destination instanceof FinalDestination) {
                FinalDestination finalDestination = (FinalDestination) destination;
                // Method that removes the current FinalDestination from the List possibleFinalDestinatios
                // This list stores (at the beginning) all Final Destinations set by the user. It is used to find alternative
                // parking Slots whenever the actual target of an Loaction and its ParkingLocations are full
                routingInfo.getPossibleFinalDestinations().removeIf(finalDest -> finalDest.getName().equals(finalDestination.getName()));
                if (routingInfo.getVehicle() == Vehicle.CAR) {
                    if (destination.getCurrentlyWaiting() < destination.getCapacity()) {
                        synchronized (this) {
                            System.out.println("Agent " + routingInfo.getAgentId() + ": Final Destination reached");
                            destination.setCurrentlyWaiting(destination.getCurrentlyWaiting() + 1);
                        }

                        if (!destination.getName().equals(routingInfo.getFinalDestination().getName())) {
                            System.out.println("Agent " + routingInfo.getAgentId() + ": Walk to intital FinalDestination");
                            routingInfo = routeTo(routingInfo, routingInfo.getFinalDestination(), Vehicle.FEET);
                            routingInfo.getVehicleChangeMap().put(destination.getLocation(), Vehicle.FEET);
                        }
                    } else if (finalDestination.getParkingSlots().size() > 0) {

                        List<ParkingDestination> parkingDestinations = finalDestination
                                .getParkingSlots()
                                .values()
                                .stream()
                                .collect(Collectors.toList());

                        routingInfo.getParkingDestinationList().addAll(parkingDestinations);

                        System.out.println("Agent " + routingInfo.getAgentId() + ": Find Nearest ParkingSlot");
                        routingInfo = findNearestDestination(routingInfo, parkingDestinations, DestinationType.PARKING);
                    } else if (routingInfo.getPossibleFinalDestinations().size() > 0) {
                        System.out.println("Agent " + routingInfo.getAgentId() + ": Find Nearest Final Dest");
                        routingInfo = findNearestDestination(routingInfo, routingInfo.getPossibleFinalDestinations(), DestinationType.FINAL);
                    } else {
                        routingInfo = findNearestDestination(routingInfo, Arrays.asList(SystemConfiguration.getInstance().getDumpDestination()), DestinationType.DUMP);
                        System.out.println("Agent " + routingInfo.getAgentId() + ": Drive to dump");
                    }
                } else if (routingInfo.getVehicle() == Vehicle.FEET) {
                    System.out.println("Agent " + routingInfo.getAgentId() + ": Pedestrain reached destination");
                } else if (routingInfo.getVehicle() == Vehicle.BIKE) {
                    System.out.println("Agent " + routingInfo.getAgentId() + ": Bike Rider reached destination");
                }

            }
             /*
            the following sections explains the InternalDestination park finding Algorithm in easy words:

            Note (Current Status). If an agent reaches an internalDestination, it is always caused by the fact, that he is using a bike and
                had a targetLocation that isn't reachable by bike.

             1. Let Agent walk (change Vehicle to Feet) to its acutal finalDestination
             */
            else if (destination instanceof InternalDestination) {

                InternalDestination internalDestination = (InternalDestination) destination;
                System.out.println("Agent " + routingInfo.getAgentId() + ": Reached Internal Dest. Walk to Final Destination");
                routingInfo = routeTo(routingInfo, routingInfo.getFinalDestination(), Vehicle.FEET);
                routingInfo.getVehicleChangeMap().put(destination.getLocation(), Vehicle.FEET);

            }
            /*
            The parkingDestination section basically follows the same rules as the finalDestination section
             */
            else if (destination instanceof ParkingDestination) {

                ParkingDestination parkingDestination = (ParkingDestination) destination;
                routingInfo.getParkingDestinationList().removeIf(parkingDestFromList ->
                        parkingDestFromList.getName().equals(parkingDestination.getName())
                );
                // routingInfo.getParkingDestinationList().remove(parkingDestination);

                //ParkingDestination parkingDestination = destinationInMemory.getParkingSlots().get(Long.parseLong(destination.getName()));
                if (parkingDestination.getCurrentlyWaiting() < parkingDestination.getCapacity()) {

                    synchronized (this) {
                        parkingDestination.setCurrentlyWaiting(parkingDestination.getCurrentlyWaiting() + 1);
                        System.out.println("Agent " + routingInfo.getAgentId() + ": parking at -> " + parkingDestination.getName());
                    }

                    routingInfo.setSelectedParkingDestination(null);
                    routingInfo.getParkingDestinationList().clear();
                    FinalDestination finalDestinationOfAgent = routingInfo.getFinalDestination();
                    routeTo(routingInfo, finalDestinationOfAgent, Vehicle.FEET);
                    routingInfo.getVehicleChangeMap().put(destination.getLocation(), Vehicle.FEET);
                    System.out.println("Agent " + routingInfo.getAgentId() + ": Walk to FinalDestination");


                } else if (routingInfo.getParkingDestinationList().size() > 0) {
                    System.out.println("Agent " + routingInfo.getAgentId() + ": Find Nearest Parking Dest");
                    routingInfo = findNearestDestination(routingInfo, routingInfo.getParkingDestinationList(), DestinationType.PARKING);
                } else if (routingInfo.getPossibleFinalDestinations().size() > 0) {
                    System.out.println("Agent " + routingInfo.getAgentId() + ": Find Nearest Final Dest");
                    routingInfo = findNearestDestination(routingInfo, routingInfo.getPossibleFinalDestinations(), DestinationType.FINAL);
                } else {
                    routingInfo = findNearestDestination(routingInfo, Arrays.asList(SystemConfiguration.getInstance().getDumpDestination()), DestinationType.DUMP);
                    System.out.println("Agent " + routingInfo.getAgentId() + ": Drive to dump Location");
                }

            }
            /*
            If an Agents arrives at a DumpDestination let him terminate.
             */
            else if (destination instanceof DumpDestination) {
                destination.setCapacity(destination.getCapacity() + 1);
                System.out.println("Agent " + routingInfo.getAgentId() + ": Arrived at Dump");
            }
        }


        return routingInfo;
    }


    /**
     * Helper Method that routes an Agent to a new Destination by altering its routingInfo object.
     *
     * @param routingInformation
     * @param destination
     * @param vehicle
     * @return
     */
    private RoutingInformation routeTo(RoutingInformation routingInformation, Destination destination, Vehicle vehicle) {
        routingInformation.setNodeIdFrom(routingInformation.getNodeIdTarget());
        routingInformation.setTargetLocation(destination.getLocation());
        routingInformation.setNodeIdTarget(neo4JQueryFactory.findNodeNearestToLocation(destination.getLocation()));
        routingInformation.setVehicle(vehicle);

        return routingInformation;
    }


    /**
     * Helper Method in order to find the nearest Destination
     *
     * @param routingInfo  - The current Routing Info object
     * @param destinations - List of the possible Destinations
     * @param type         - determines the destination type. (PARKING | FINAL | DUMP)
     * @return
     */
    private RoutingInformation findNearestDestination(RoutingInformation routingInfo, List<? extends Destination> destinations, DestinationType type) {
        MathUtil mathUtil = new MathUtil();
        PathUtil pathUtil = new PathUtil();

        System.out.println("Agent " + routingInfo.getAgentId() + ": destinations_size = " + destinations.size());

        int oldTargetId = routingInfo.getNodeIdTarget();
        Location oldTargetLocation = routingInfo.getTargetLocation();

        int currentLocationId = neo4JQueryFactory.findNodeNearestToLocation(routingInfo.getCurrentLocation());

        double fastestPathInSeconds = -1;

        Destination newTargetDestination = null;

        /*
            the following sections explains the destination search for parkingDestinations

            Note: The Method getDurationTableBetweenParkingSlots(); fetches a "matrix"-table of all possible parkingDestination connection with its
                durations from point a to point b from the TempFinalDestination.
                Each FinalDestination with parkingslots calculates such a table at the initilization process of the routingkit in order
                to imporive the performance when using a massiv amount of agents.

            1. Are there any parkingSlots to which the agent may drive to?
                true -> find the parkingSlot which is the neareast to the current position.
                false -> dont change the routingInfo object
             */
        if (type.equals(DestinationType.PARKING)) {
            List<ParkingDestination> parkingDestinations = (List<ParkingDestination>) destinations;
            if (!parkingDestinations.isEmpty()) {

                HashMap<String, Double> durationTableBetweenParkingSlots = routingInfo.getTempFinalDestination().getDurationTableBetweenParkingSlots();
                ParkingDestination newTarget = null;
                if (durationTableBetweenParkingSlots.size() > 0) {

                    double fastestPath = -1d;
                    String fastestPathDestId = "";

                    List<ParkingDestination> allowedParkingSlots = routingInfo.getParkingDestinationList();

                    Map<String, Double> reducedMap = durationTableBetweenParkingSlots.entrySet()
                            .stream()
                            .filter(entry -> entry.getKey().startsWith(Integer.toString(currentLocationId)) && containsAllowedParkingSlot(entry.getKey().replace(currentLocationId + "->", ""), allowedParkingSlots))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                    for (Map.Entry<String, Double> entry : reducedMap.entrySet()) {
                        if (entry.getValue() < fastestPath || fastestPath == -1d) {
                            fastestPath = entry.getValue();
                            fastestPathDestId = entry.getKey().replace(currentLocationId + "->", "");
                        }
                    }

                    for (ParkingDestination parkingDestination : parkingDestinations) {
                        if (parkingDestination.getName().equals(fastestPathDestId)) {
                            newTarget = parkingDestination;
                            break;
                        }
                    }
                } else {
                    newTarget = parkingDestinations.get(0);
                }

                routingInfo.setNodeIdFrom(oldTargetId);
                routingInfo.setTargetLocation(newTarget.getLocation());
                routingInfo.setNodeIdTarget(Integer.valueOf(newTarget.getName()));
                newTargetDestination = newTarget;

                // System.out.println("fastestPathDestId = " + fastestPathDestId);
                // System.out.println("fastestPath = " + fastestPath);
            }
        }
        /*
            the following sections explains the destination search for dump and finalDestinations

            In this section the durations that an agent needs to get to a destination are not precalcualted.
            Therefore it simply calculates the times needed to travel from point a to point b from the current Location
            to every destination in the destinationsList and selects the one which has the fastest traveltime.
         */
        else if (type.equals(DestinationType.FINAL) || type.equals(DestinationType.DUMP)) {

            for (Destination destination : destinations) {

                try {
                    int possibleNewTargetId = neo4JQueryFactory.findNodeNearestToLocation(destination.getLocation());
                    if (!routingInfo.getTargetList().contains(destination.getLocation()))
                        if (possibleNewTargetId != oldTargetId) {
                            //if(destination.getCapacity() > destination.getCurrentlyWaiting()){

                            PathValue path = (PathValue) neo4JQueryFactory.findPath(routingInfo.getVehicle(), oldTargetId, possibleNewTargetId, routingInfo.getRoutingPreference().name(), this.blockedStreets, this.modifiedSpeedMap, routingInfo.getHateFactorCarsOnSameRoad());
                            ArrayList<InternalRelationship> route;
                            ArrayList<Long> routeIds = new ArrayList<>();
                            boolean routeHasChanged = false;

                            ArrayList<Node> nodes;
                            if (path != null) {
                                route = pathUtil.getRelationshipsFromPath(path);

                                double secondsInTotal = mathUtil.calculateTravelTimeForCompletePath(route, routingInfo.getSpeedFactor());

                                if (secondsInTotal < fastestPathInSeconds || fastestPathInSeconds == -1) {
                                    fastestPathInSeconds = secondsInTotal;
                                    routingInfo.setNodeIdFrom(oldTargetId);
                                    routingInfo.setTargetLocation(destination.getLocation());
                                    routingInfo.setNodeIdTarget(possibleNewTargetId);
                                    newTargetDestination = destination;
                                }
                            }
                        }
                    // }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }
        }

        if (oldTargetLocation != routingInfo.getTargetLocation()) {
            routingInfo.getTargetList().add(routingInfo.getTargetLocation());
        }

        if (newTargetDestination instanceof ParkingDestination) {
            routingInfo.setSelectedParkingDestination((ParkingDestination) newTargetDestination);
        } else if (newTargetDestination instanceof FinalDestination) {
            routingInfo.setTempFinalDestination((FinalDestination) newTargetDestination);
            routingInfo.getParkingDestinationList().clear();
            routingInfo.setSelectedParkingDestination(null);
        }

        return routingInfo;
    }

    /**
     * RMI Mehtod for AGADE that enables the usage of the desired target time as an agent behaviour.
     * It is called whenever the start tick of an agent is calculated
     *
     * @param start
     * @param end
     * @return
     * @throws RemoteException
     */
    @Override
    public double calculatefastestWayToDestination(Location start, Location end) throws RemoteException {
        MathUtil mathUtil = new MathUtil();



        int startLocId = neo4JQueryFactory.findNodeNearestToLocation(start);
        int endLocId = neo4JQueryFactory.findNodeNearestToLocation(end);

        //TODO: FindPath abstrahieren... e.g. Vehicle wählbar machen
        PathValue path = null;

        path = (PathValue) neo4JQueryFactory.findPath(Vehicle.CAR, startLocId, endLocId, RoutingPreference.DEFAULT.toString(), this.blockedStreets, this.modifiedSpeedMap, 0);

        ArrayList<InternalRelationship> route;
        if (path != null) {
            route = new PathUtil().getRelationshipsFromPath(path);
            double secondsInTotal = mathUtil.calculateTravelTimeForCompletePath(route, 1.0);
            return secondsInTotal;
        }


        return -1d;
    }

    @Override
    public DumpDestination getDumpDestination() throws RemoteException {
        return SystemConfiguration.getInstance().getDumpDestination();
    }

    @Override
    public List<FinalDestination> getFinalDestinations() throws RemoteException {
        return SystemConfiguration.getInstance().getFinalDestinations();
    }

    @Override
    public int getAnzCarsOnSameRoad(List<RelationshipInfoBean> relations, double shareOfLastRelation) throws RemoteException {

        return Neo4JQueryFactory.getInstance().getAnzCarsOnSameRoad(relations, shareOfLastRelation);

    }

    @Override
    public List<BikePath> getBikePathsInBoundingBox(Location locMax, Location locMin) throws RemoteException {
        return neo4JQueryFactory.getBikePathsInBoundingBox(locMax, locMin);
    }


    private boolean containsAllowedParkingSlot(String parkingSlotId, List<ParkingDestination> allowedParkingSlots) {

        Optional<ParkingDestination> searchPD = allowedParkingSlots.stream()
                .filter(parkingDestination -> parkingDestination.getName().equals(parkingSlotId))
                .findFirst();

        return searchPD.isPresent();
    }

    /**
     * First Function that is called in order to initialize the Routing Kit
     * @param destinations
     * @param trafficActions
     * @param dumpDestination
     * @throws RemoteException
     */
    @Override
    public void initRoutingkit(List<FinalDestination> destinations, List<TrafficActionObject> trafficActions, DumpDestination dumpDestination) throws RemoteException {
        SystemConfiguration.getInstance().setFinalDestinations(destinations);
        SystemConfiguration.getInstance().setDumpDestination(dumpDestination);
        SystemConfiguration.getInstance().getParkingDestinations().clear();

        List<Long> blockedStreetIds = new ArrayList<>();
        Map<String, Integer> modifiedStreetSpeed = new HashMap<>();

        if (trafficActions != null) {
            for (TrafficActionObject trafficAction : trafficActions) {
                if (trafficAction.isBlock()) {
                    blockedStreetIds.addAll(blockStreet(trafficAction.getLocations()));
                } else {
                    modifiedStreetSpeed.putAll(changeMaxSpeed(trafficAction.getLocations(), trafficAction.getMaxspeed()));
                }
            }
        }

        SystemConfiguration.getInstance().setBlockedStreetIds(blockedStreetIds);
        SystemConfiguration.getInstance().setModifiedSpeedMap(modifiedStreetSpeed);

        /*
         Executor Service that precalculates the durations between parkingSlots of an Final Destination.
         */
        ExecutorService executor = Executors.newFixedThreadPool(destinations.size());
        for (FinalDestination destination : destinations) {
            executor.execute(() -> {
                System.out.println("Thread-" + Thread.currentThread().getId() + " started");
                Neo4JQueryFactory.getInstance().findNodeNearToLocation(destination);
                new DistanceCalcUtil().calculateDistanceBeetweenParkingSlots(destination);
                System.out.println("Thread-" + Thread.currentThread().getId() + " finished");
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished initialisation");
    }

    private boolean compareMovementsOfAgent(ArrayList<Long> prevRouteIds, ArrayList<Long> routeIds) {

        //  the start index needs to be claculated as the prevRouteIds list contains route Ids that already have passed by the agent.
        int startIndex = prevRouteIds.size() - routeIds.size();

        if (startIndex < 0) return true;

        for (int i = prevRouteIds.size() - routeIds.size(); i < prevRouteIds.size(); i++) {

            if (prevRouteIds.get(i).longValue() != routeIds.get((i - startIndex)).longValue()) {
                return true; //  Route has changed
            }
        }

        return false;
    }

    @Override
    public Location findNodeNearestToLocation(Location location) throws RemoteException {
        int nodeID = neo4JQueryFactory.findNodeNearestToLocation(location);
        return neo4JQueryFactory.getLocationFromNodeID(nodeID);
    }

    @Override
    public List<VelocitySection> getVelocitySectionsInBoundingBox(Location max, Location min) throws RemoteException {
        return neo4JQueryFactory.getVelocitySectionsInBoundingBox(max, min);
    }


    private List<Long> blockStreet(List<Location> locations) throws RemoteException {
        PathUtil pathUtil = new PathUtil();

        List<Long> blocked = new ArrayList<>();
        for (int i = 0; i < locations.size() - 1; i++) {
            int node_start = neo4JQueryFactory.findNodeNearestToLocation(locations.get(i));
            int node_end = neo4JQueryFactory.findNodeNearestToLocation(locations.get(i + 1));
            PathValue path = null;

            path = (PathValue) neo4JQueryFactory.findPath(Vehicle.CAR, node_start, node_end, "Fastest", new ArrayList<>(), new HashMap<>(), 0);

            if (path != null) {
                ArrayList<InternalRelationship> route = pathUtil.getRelationshipsFromPath(path);
                for (InternalRelationship rel : route) {
                    blocked.add(rel.id());

                }
            }

        }
        return blocked;
    }

    private Map<String, Integer> changeMaxSpeed(List<Location> locations, int maxspeed) throws RemoteException {
        PathUtil pathUtil = new PathUtil();

        Map<String, Integer> modified = new HashMap<>();
        for (int i = 0; i < locations.size() - 1; i++) {
            int node_start = neo4JQueryFactory.findNodeNearestToLocation(locations.get(i));
            int node_end = neo4JQueryFactory.findNodeNearestToLocation(locations.get(i + 1));
            PathValue path = null;

            path = (PathValue) neo4JQueryFactory.findPath(Vehicle.CAR, node_start, node_end, "Fastest", new ArrayList<>(), new HashMap<>(), 0);
            if (path != null) {

                ArrayList<InternalRelationship> route = pathUtil.getRelationshipsFromPath(path);
                for (InternalRelationship rel : route) {
                    modified.put(String.valueOf(rel.id()), maxspeed);
                }
            }
        }
        return modified;
    }


}
