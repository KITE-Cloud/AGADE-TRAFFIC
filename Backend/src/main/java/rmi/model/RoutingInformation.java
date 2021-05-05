package rmi.model;

import utility.AgentIndexer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Joshua Prim, Jannik Enenkel, Jannik Geyer
 *
 * the information that is passed between an AGADE agent and the routing component
 */
public class RoutingInformation implements Serializable {

    private static final long serialVersionUID = 1L;


    private double speedFactor;
    private int agentId;

    // necessary information for both ROUTING and AGADE
    private Location currentLocation; // the location of the agent at the moment
    private Location targetLocation; // where the agent wants to go
    private RoutingPreference routingPreference; // the criteria on how to choose the route
    private long relationshipId; // the road the agent is on
    private ArrayList<AgentMovement> movements; // agent movements for visualization
    private Vehicle vehicle;

    // information just for ROUTING
    private double drivenMeters; // the progress on the current road
    private int nodeIdFrom, nodeIdTo; // the nodes the current road is between
    private int nodeIdTarget; // the nearest node to the target location

    // information for validation
    private HashMap<Integer, RouteChangeBean> routeHasChanged;
    private HashMap<Location, Vehicle> vehicleChangeMap;
    private ArrayList<Long> previousRouteIds;
    private ArrayList<Location> targetList;
    private List<ParkingDestination> parkingDestinationList;
    private ParkingDestination selectedParkingDestination;
    private boolean parked;
    private FinalDestination finalDestination;
    private FinalDestination tempFinalDestination;
    private List<FinalDestination> possibleFinalDestinations;
    private DumpDestination dumpDestination;
    private int currentTick;
    private int hateFactorCarsOnSameRoad;
    private List<RelationshipInfoBean> relationshipList;
    private RelationshipInfoBean currentRelationshipBean;
    private int waitUntil;

    public RoutingInformation(Vehicle vehicle, Location currentLocation, Location targetLocation, RoutingPreference routingPreference){
        this.vehicle = vehicle;
        this.currentLocation = currentLocation;
        this.targetLocation = targetLocation;
        this.routingPreference = routingPreference;
        this.speedFactor = 1.0;
        this.routeHasChanged = new HashMap<>();
        this.vehicleChangeMap = new HashMap<>();
        this.previousRouteIds = new ArrayList<>();
        this.targetList = new ArrayList<>();
        this.targetList.add(targetLocation);
        this.agentId = AgentIndexer.getIndex();
        this.parkingDestinationList = new ArrayList<>();
        this.possibleFinalDestinations = new ArrayList<>();
        this.relationshipList = new ArrayList<>();
        this.currentRelationshipBean = null;
        this.waitUntil = -1;
    }

    public RoutingInformation(Location currentLocation, Location targetLocation, RoutingPreference routingPreference, Double speedFactor){
        this.currentLocation = currentLocation;
        this.targetLocation = targetLocation;
        this.routingPreference = routingPreference;
        this.speedFactor = speedFactor;
    }

    public double getSpeedFactor() {
        return speedFactor;
    }

    public void setSpeedFactor(double speedFactor) {
        this.speedFactor = speedFactor;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Location targetLocation) {
        this.targetLocation = targetLocation;
    }

    public long getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(long relationshipId) {
        this.relationshipId = relationshipId;
    }

    public RoutingPreference getRoutingPreference() {
        return routingPreference;
    }

    public void setRoutingPreference(RoutingPreference routingPreference) {
        this.routingPreference = routingPreference;
    }

    public double getDrivenMeters() {
        return drivenMeters;
    }

    public void setDrivenMeters(double drivenMeters) {
        this.drivenMeters = drivenMeters;
    }

    public int getNodeIdFrom() {
        return nodeIdFrom;
    }

    public void setNodeIdFrom(int nodeIdFrom) {
        this.nodeIdFrom = nodeIdFrom;
    }

    public int getNodeIdTo() {
        return nodeIdTo;
    }

    public void setNodeIdTo(int nodeIdTo) {
        this.nodeIdTo = nodeIdTo;
    }

    public int getNodeIdTarget() {
        return nodeIdTarget;
    }

    public void setNodeIdTarget(int nodeIdTarget) {
        this.nodeIdTarget = nodeIdTarget;
    }

    public ArrayList<AgentMovement> getMovements() {
        return movements;
    }

    public void setMovements(ArrayList<AgentMovement> movements) {
        this.movements = movements;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public HashMap<Integer, RouteChangeBean> getRouteHasChanged() {
        return routeHasChanged;
    }

    public void setRouteHasChanged(HashMap<Integer, RouteChangeBean> routeHasChanged) {
        this.routeHasChanged = routeHasChanged;
    }

    public ArrayList<Long> getPreviousRoute() {
        return previousRouteIds;
    }

    public void setPreviousRoute(ArrayList<Long> previousRouteIds) {
        this.previousRouteIds = previousRouteIds;
    }

    public ArrayList<Location> getTargetList() {
        return targetList;
    }

    public void setTargetList(ArrayList<Location> targetList) {
        this.targetList = targetList;
    }

    public List<ParkingDestination> getParkingDestinationList() {
        return parkingDestinationList;
    }

    public void setParkingDestinationList(List<ParkingDestination> parkingDestinationList) {
        this.parkingDestinationList = parkingDestinationList;
    }


    public ParkingDestination getSelectedParkingDestination() {
        return selectedParkingDestination;
    }

    public void setSelectedParkingDestination(ParkingDestination selectedParkingDestination) {
        this.selectedParkingDestination = selectedParkingDestination;
    }

    public boolean isParked() {
        return parked;
    }

    public void setParked(boolean parked) {
        this.parked = parked;
    }

    public FinalDestination getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(FinalDestination finalDestination) {
        this.finalDestination = finalDestination;
    }

    public FinalDestination getTempFinalDestination() {
        return tempFinalDestination;
    }

    public void setTempFinalDestination(FinalDestination tempFinalDestination) {
        this.tempFinalDestination = tempFinalDestination;
    }

    public List<FinalDestination> getPossibleFinalDestinations() {
        return possibleFinalDestinations;
    }

    public void setPossibleFinalDestinations(List<FinalDestination> possibleFinalDestinations) {
        this.possibleFinalDestinations = possibleFinalDestinations;
    }

    public DumpDestination getDumpDestination() {
        return dumpDestination;
    }

    public void setDumpDestination(DumpDestination dumpDestination) {
        this.dumpDestination = dumpDestination;
    }


    public HashMap<Location, Vehicle> getVehicleChangeMap() {
        return vehicleChangeMap;
    }

    public void setVehicleChangeMap(HashMap<Location, Vehicle> vehicleChangeMap) {
        this.vehicleChangeMap = vehicleChangeMap;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }


    public int getHateFactorCarsOnSameRoad() {
        return hateFactorCarsOnSameRoad;
    }

    public void setHateFactorCarsOnSameRoad(int hateFactorCarsOnSameRoad) {
        this.hateFactorCarsOnSameRoad = hateFactorCarsOnSameRoad;
    }

    public List<RelationshipInfoBean> getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(List<RelationshipInfoBean> relationshipList) {
        this.relationshipList = relationshipList;
    }

    public RelationshipInfoBean getCurrentRelationshipBean() {
        return currentRelationshipBean;
    }

    public void setCurrentRelationshipBean(RelationshipInfoBean currentRelationshipBean) {
        this.currentRelationshipBean = currentRelationshipBean;
    }

    public int getWaitUntil() {
        return waitUntil;
    }

    public void setWaitUntil(int waitUntil) {
        this.waitUntil = waitUntil;
    }
}