package util;

import neo4jAccessor.Neo4JQueryFactory;
import org.neo4j.driver.internal.InternalRelationship;
import org.neo4j.driver.internal.value.PathValue;
import org.neo4j.driver.v1.types.Node;
import rmi.RMIRemoteObject;
import rmi.model.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DistanceCalcUtil {

    PathUtil pathUtil = new PathUtil();
    MathUtil mathUtil = new MathUtil();
    Neo4JQueryFactory neo4JQueryFactory = Neo4JQueryFactory.getInstance();

    private void calculateDurationForNodes(Location from, Location to, HashMap<String, Double> durationTable) {

        int parkingDestFromNodeId = neo4JQueryFactory.findNodeNearestToLocation(from),
                parkingDestToNodeId = neo4JQueryFactory.findNodeNearestToLocation(to);

        if (parkingDestFromNodeId == parkingDestToNodeId) {
            return; // Same Location -> return
        }

        SystemConfiguration configuration = SystemConfiguration.getInstance();
        PathValue path = null;

        path = (PathValue) neo4JQueryFactory.findPath(Vehicle.CAR, parkingDestFromNodeId, parkingDestToNodeId, RoutingPreference.DEFAULT.toString(), configuration.getBlockedStreetIds(), configuration.getModifiedSpeedMap(), 0);

        ArrayList<InternalRelationship> route;
        ArrayList<Long> routeIds = new ArrayList<>();
        boolean routeHasChanged = false;

        ArrayList<Node> nodes;
        if (path != null) {
            try {
                route = pathUtil.getRelationshipsFromPath(path);
                double completeDuration = mathUtil.calculateTravelTimeForCompletePath(route, 1.0);

                durationTable.put(parkingDestFromNodeId + "->" + parkingDestToNodeId, completeDuration);

            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }


    }


    /**
     * iterates through the parking Slots of a FinalDestination object and calculates the duration between every parkingSlot to every parkingSlot
     *
     * @param destination
     */
    public void calculateDistanceBeetweenParkingSlots(FinalDestination destination) {

        HashMap<Long, ParkingDestination> parkingSlots = destination.getParkingSlots();

        HashMap<String, Double> durationTable = destination.getDurationTableBetweenParkingSlots(); // key  = "From(Long)->To(Long)"

        for (Map.Entry<Long, ParkingDestination> parkingDestFrom : parkingSlots.entrySet()) {

            calculateDurationForNodes(destination.getLocation(), parkingDestFrom.getValue().getLocation(), durationTable);

            for (Map.Entry<Long, ParkingDestination> parkingDestTo : parkingSlots.entrySet()) {

                if (parkingDestFrom.getKey() == parkingDestTo.getKey()) {
                    continue;
                }

                calculateDurationForNodes(parkingDestFrom.getValue().getLocation(), parkingDestTo.getValue().getLocation(), durationTable);
            }
        }
    }

}
