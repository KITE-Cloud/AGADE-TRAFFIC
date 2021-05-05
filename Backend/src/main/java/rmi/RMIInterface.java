package rmi;

import rmi.model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface RMIInterface extends Remote {
    String ping() throws RemoteException;

    boolean nextTick(int tickLengthInSeconds, HashMap<Long, Integer> roadInfo, List<TrafficActionObject> trafficActions) throws RemoteException;

    RoutingInformation routeAgent(RoutingInformation routingInfo, int currentTick) throws RemoteException;

    void initRoutingkit(List<FinalDestination> destinations, List<TrafficActionObject> trafficActionObjects, DumpDestination dumpDestination) throws RemoteException;

    // ArrayList<Relationship> getRelationshipsFromPath(PathValue path) throws RemoteException;

    Location findNodeNearestToLocation(Location location) throws RemoteException;

    List<VelocitySection> getVelocitySectionsInBoundingBox(Location max, Location min) throws RemoteException;

    double calculatefastestWayToDestination(Location start, Location end) throws RemoteException;

    DumpDestination getDumpDestination() throws RemoteException;

    List<FinalDestination> getFinalDestinations() throws RemoteException;

    int getAnzCarsOnSameRoad(List<RelationshipInfoBean> relations, double shareOfLastRelation) throws RemoteException;

    List<BikePath> getBikePathsInBoundingBox(Location locMax, Location locMin) throws RemoteException;

}
