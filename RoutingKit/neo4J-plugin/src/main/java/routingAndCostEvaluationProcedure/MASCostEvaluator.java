package routingAndCostEvaluationProcedure;

import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.List;
import java.util.Map;

public class MASCostEvaluator implements CostEvaluator<Double> {

    public static final double MAXSPEED_BIKE = 18d, MAXSPEED_FEET = 3.6d;

    private long hateFactorCarsOnSameRoad;
    private String routingPreference;
    private List<Long> blockedStreets;
    private Map<Long, Integer> modifiedSpeedMap;
    private String vehicle;

    public MASCostEvaluator(String vehicle, String routingPreference, List<Long> blockedStreets, Map<Long, Integer> modifiedSpeedMap, long hateFactorCarsOnSameRoad){
        this.routingPreference = routingPreference;
        this.vehicle = vehicle;
        this.blockedStreets = blockedStreets;
        this.modifiedSpeedMap = modifiedSpeedMap;
        this.hateFactorCarsOnSameRoad = hateFactorCarsOnSameRoad;
    }

    /**
     * Method to calculate the costs for a specific path using the amount of cars and capacity on the edges that
     * are part of the path.
     * Currently the assumed reduction of speed is analogue to the implementation of the local cost calculation
     * in the Multiagentsystem.
     */
    @Override
    public Double getCost(Relationship relationship, Direction direction) {
        double costs;
        if (
                (relationship.getProperty("streetType").equals("step") && vehicle.equals("CAR")) ||
                (relationship.getProperty("streetType").equals("cycleway") && vehicle.equals("CAR")) ||
                (relationship.getProperty("streetType").equals("footway") && vehicle.equals("CAR")) ||
                (relationship.getProperty("streetType").equals("pedestrian") && vehicle.equals("CAR")) ||
                (relationship.getProperty("streetType").equals("track") && vehicle.equals("CAR")) ||
                (relationship.getProperty("streetType").equals("path") && vehicle.equals("CAR")) ||
                blockedStreets.contains(relationship.getId()) ||
                (!(boolean) relationship.getProperty("bicycleAllowed") && vehicle.equals("BIKE"))||
                (relationship.getProperty("streetType").equals("motorway") && vehicle.equals("Pedestrian"))
        )

//                (relationship.getProperty("streetType").equals("step") && vehicle.equals("BIKE"))
//                (relationship.getProperty("streetType").equals("footway") && vehicle.equals("BIKE")) ||
//                (relationship.getProperty("streetType").equals("pedestrian") && vehicle.equals("BIKE"))
         {
            costs = Double.MAX_VALUE;
        } else {
            Node startNode = relationship.getStartNode();
            Node endNode = relationship.getEndNode();
            double trafficUnitValue = (double) relationship.getProperty("lambda"); // this represents the marginal costs
            double meters = calculateMeters(startNode, endNode);
            double timeForRoad = calculateTimeForOneTraveller(vehicle, relationship, meters);
            long carsOnThisRoad = Long.valueOf(relationship.getProperty("anzCars").toString());

            costs = WorldCalculator.calculateTimeForRoad(routingPreference, timeForRoad, trafficUnitValue, carsOnThisRoad, hateFactorCarsOnSameRoad);
        }
        return costs;
    }

    /**
     * Analogue implementation to the time for one car calculation model at the client application
     */
    private double calculateTimeForOneTraveller(String vehicle, Relationship relationship, double meters) {
        double maxspeed;
        long id = relationship.getId();
        if (modifiedSpeedMap.containsKey(id)){
            maxspeed = Integer.parseInt(String.valueOf(modifiedSpeedMap.get(id)));
        } else {
            maxspeed = (double) (relationship.getProperty("maxSpeed"));
        }
        if (vehicle.equals("BIKE")){
            maxspeed = MAXSPEED_BIKE;
        } else if (vehicle.equals("FEET")) {
            maxspeed = MAXSPEED_FEET;
        }
        return WorldCalculator.calculateTimeForOneCar(meters, maxspeed);
    }

    /**
     * Calculate length of a certain edge that.
     */
    private double calculateMeters(Node node, Node goal) {
        double lat1 = (Double) node.getProperty("lat");
        double lat2 = (Double) goal.getProperty("lat");
        double lng1 = (Double) node.getProperty("lon");
        double lng2 = (Double) goal.getProperty("lon");
        return (double) WorldCalculator.distFrom(lat1, lng1, lat2, lng2);
    }
}
