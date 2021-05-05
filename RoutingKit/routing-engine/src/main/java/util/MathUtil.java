package util;

import neo4jAccessor.Neo4JQueryFactory;
import org.neo4j.driver.internal.InternalRelationship;
import rmi.model.AgentMovement;
import rmi.model.Location;
import rmi.model.RoutingInformation;
import org.neo4j.driver.v1.types.Node;
import rmi.model.Vehicle;
;

import java.util.*;

/**
 * @author Joshua Prim, Jannik Enenkel, Jannik Geyer
 */

@SuppressWarnings("Duplicates")
public class MathUtil {

    private Neo4JQueryFactory neo4JQueryFactory;

    public MathUtil() {
        neo4JQueryFactory = Neo4JQueryFactory.getInstance();
    }



    public double calculateTravelTimeForCompletePath(ArrayList<InternalRelationship> completeRoute, double speedFactor) {

        double secondsInTotal = 0;

        for (InternalRelationship routePart : completeRoute) {
            double maxSpeed = routePart.get("maxSpeed").asDouble();
            double distance = routePart.get("distance").asDouble();
            int anzCars = routePart.get("anzCars").asInt();
            double lambda = routePart.get("lambda").asDouble();

            if (maxSpeed == -1) {
                maxSpeed = 50; //TODO: In Neo4j verbessern. ES darf keine MaxSpeed Default Werte mit -1 geben. Negative Zahlen zerstören das Routing.
            }
            double secondsNeededForRoutePart = calculateSecondsForNMeter(maxSpeed, distance, anzCars, lambda, speedFactor);
            secondsInTotal += secondsNeededForRoutePart;
        }


        return secondsInTotal;
    }


    /**
     * Diese Funktion berechnet die Meter, welche in N Sekunden zurückgelegt werden.
     *  @param speedLimit in km/H
     * @param tickSpeed in seconds
     * @param speedFactor user behaviour -> 1.5 means faster driver than usual. < 1 -> slower
     */
    private double calculateMeterPerNSeconds(Double speedLimit, Double tickSpeed, Double speedFactor){
        return (speedLimit * 1000 * speedFactor) / 3600 * tickSpeed;
    }


    /**
     * Diese Funktiuon berechnet die Meter, welche in N Sekunden zurückgelegt werden.
     *  @param speedLimit in km/H
     * @param distanceLimit in meter
     * @param speedFactor user behaviour value around 1
     */
    private double calculateSecondsForNMeter(double speedLimit, double distanceLimit, int anzCarsOnRoad, double lambda, Double speedFactor){
        return (distanceLimit / ((speedLimit * 1000 * speedFactor) / 3600)) + (anzCarsOnRoad * lambda);
    }


    /**
     * calculates the movement of the agent
     */
    public RoutingInformation calculateMoveDistance(ArrayList<InternalRelationship> route,
                                                    ArrayList<Node> nodes,
                                                    RoutingInformation routingInformation,
                                                    int currentRouteIndex,
                                                    double remainingTime,
                                                    Map<String, Integer> modifiedSpeedMap,
                                                    boolean initRun){

        double drivenDistance = routingInformation.getDrivenMeters();

        if (route.size() == 0) {
            System.out.println("");
        }

        InternalRelationship currentRelationship = route.get(currentRouteIndex);
        Map<String, Object> properties = currentRelationship.asMap();
        double distanceLimit = (double) properties.get("distance");
        double speedLimit;
        if (modifiedSpeedMap.containsKey(String.valueOf(currentRelationship.id()))){
            speedLimit = modifiedSpeedMap.get(String.valueOf(currentRelationship.id()));
        } else {
            speedLimit = (double) properties.get("maxSpeed");
        }
        if (routingInformation.getVehicle().equals(Vehicle.BIKE)){
            speedLimit = 18d;
        } else if (routingInformation.getVehicle().equals(Vehicle.FEET)){
            speedLimit = 3.6d;
        }

        int anzCarsOnRoad = Math.toIntExact((long) properties.get("anzCars"));
        double lambda = (double) properties.get("lambda");

        if(initRun){
            distanceLimit = distanceLimit - drivenDistance;
        }

        double tempSeconds = calculateSecondsForNMeter(speedLimit, distanceLimit, anzCarsOnRoad, lambda, routingInformation.getSpeedFactor());

        if(remainingTime >= tempSeconds){

            AgentMovement agentMovement = createAgentMovement(currentRelationship, nodes, tempSeconds);
            if (initRun) {
                agentMovement.setFrom(routingInformation.getCurrentLocation());
            }
            routingInformation.getMovements().add(agentMovement);

            remainingTime -= tempSeconds;
            currentRouteIndex++;
            Location newCurrent = neo4JQueryFactory.getLocationFromNodeID(Math.toIntExact(currentRelationship.endNodeId()));
            routingInformation.setCurrentLocation(newCurrent);
            if (currentRouteIndex < route.size()){
                return calculateMoveDistance(route, nodes, routingInformation, currentRouteIndex, remainingTime, modifiedSpeedMap,false);
            } else { // destination reached
                routingInformation.setCurrentLocation(routingInformation.getTargetLocation());
                return routingInformation;
            }

        } else {

            double drivenMeterOnEdge = calculateMeterPerNSeconds(speedLimit, remainingTime, routingInformation.getSpeedFactor());

            if(currentRouteIndex == 0){ // if agent didn't leave road he started at
                routingInformation.setDrivenMeters(routingInformation.getDrivenMeters() + drivenMeterOnEdge);
            } else {
                routingInformation.setDrivenMeters(drivenMeterOnEdge);
            }

            AgentMovement agentMovement = createAgentMovement(routingInformation.getCurrentLocation(), currentRelationship, nodes, remainingTime);
            routingInformation.getMovements().add(agentMovement);

            routingInformation.setRelationshipId(currentRelationship.id());
            routingInformation.setNodeIdFrom(Math.toIntExact(currentRelationship.startNodeId()));
            routingInformation.setNodeIdTo(Math.toIntExact(currentRelationship.endNodeId()));

            return routingInformation;

        }

    }



    /**
     * calculates the new lat and lon position of an agent after the movement
     * @param routingInformation
     */
    public RoutingInformation calculateNewAgentPosition(ArrayList<InternalRelationship> route,
                                                        ArrayList<Node> nodes,
                                                        RoutingInformation routingInformation){

        int nodeIdFrom = routingInformation.getNodeIdFrom();
        int nodeIdTo = routingInformation.getNodeIdTo();
        double latFrom = 0, latTo = 0, lonFrom = 0, lonTo = 0;

        for (Node n : nodes){
            if (n.id()==nodeIdFrom){
                Map<String, Object> nodeProperties = n.asMap();
                latFrom = (double) nodeProperties.get("lat");
                lonFrom = (double) nodeProperties.get("lon");
            }
            if (n.id()==nodeIdTo){
                Map<String, Object> nodeProperties = n.asMap();
                latTo = (double) nodeProperties.get("lat");
                lonTo = (double) nodeProperties.get("lon");
            }
        }

        double drivenDistance = routingInformation.getDrivenMeters();
        double distanceBetweenNodes = 0d;
        for (InternalRelationship relationship : route){
            if (relationship.id() == routingInformation.getRelationshipId()){
                distanceBetweenNodes = (double) relationship.asMap().get("distance");
                break;
            }
        }

        double drivenDistanceInPercent = drivenDistance / distanceBetweenNodes;

        double lonDiff = lonTo - lonFrom;
        double newLon = lonFrom + lonDiff * drivenDistanceInPercent;

        double latDiff = latTo - latFrom;
        double newLat = latFrom + latDiff * drivenDistanceInPercent;

        /*long temp = (long)(newLon*100000.0);
        newLon = ((double)temp)/100000.0;
        long temp2 = (long)(newLat*100000.0);
        newLat = ((double)temp2)/100000.0;*/

        Location currentLocation = new Location(newLat, newLon);

        routingInformation.setCurrentLocation(currentLocation);

        ArrayList<AgentMovement> movements = routingInformation.getMovements();
        movements.get(movements.size() - 1).setTo(currentLocation);

        return routingInformation;
    }


    private AgentMovement createAgentMovement(InternalRelationship relationship, ArrayList<Node> nodes, double duration){
        long nodeFrom = relationship.startNodeId();
        long nodeTo = relationship.endNodeId();
        Location from = null;
        Location to = null;

        Long streetPartId = relationship.id();

        for (Node n : nodes){
            if (n.id()==nodeFrom){
                Map<String, Object> nodeProperties = n.asMap();
                from = new Location((double) nodeProperties.get("lat"), (double) nodeProperties.get("lon"));
            }
            if (n.id()==nodeTo){
                Map<String, Object> nodeProperties = n.asMap();
                to = new Location((double) nodeProperties.get("lat"), (double) nodeProperties.get("lon"));
            }
        }
        return new AgentMovement(from, to, duration, from, to, streetPartId);
    }

    private AgentMovement createAgentMovement(Location from, InternalRelationship relationship, ArrayList<Node> nodes, double duration){
        long nodeTo = relationship.endNodeId();
        long nodeFrom = relationship.startNodeId();

        Location edgeStart = null;
        Location to = null;


        Long streetPartId = relationship.id();


        for (Node n : nodes){
            if (n.id()==nodeFrom){
                Map<String, Object> nodeProperties = n.asMap();
                edgeStart = new Location((double) nodeProperties.get("lat"), (double) nodeProperties.get("lon"));
            }
            if (n.id()==nodeTo){
                Map<String, Object> nodeProperties = n.asMap();
                to = new Location((double) nodeProperties.get("lat"), (double) nodeProperties.get("lon"));
            }
        }
        return new AgentMovement(from, to, duration, edgeStart, to, streetPartId);
    }

}