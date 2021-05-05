package rmi;

import rmi.model.*;
import controllers.Client;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;

/**
 * The type Rmi client.
 *
 * @author Joshua Prim, Jannik Enenkel, Jannik Geyer
 */
public class RMIClient {

    private static RMIClient rmiClient = new RMIClient();

    private RMIInterface RmiObject;

    private RMIConfiguration config;

    private RMIClient() {

      this.config = RMIConfiguration.getInstance();

        // These parameters can later be read out of a config-file
        String ipAddress = config.IP_ADRESSE;
        int port = Registry.REGISTRY_PORT;

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(ipAddress, port);
            this.RmiObject = (RMIInterface) registry.lookup("RMIServer");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static RMIClient getInstance () {
        return rmiClient;
    }

    /**
     * Ping Methode zum testen des RMI Server
     *
     */
    public void ping(){
        try {
            System.out.println("Das vom Server zurückgegebene Ergebnis: " + this.RmiObject.ping());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean nextTick(int tickLengthInSeconds, HashMap<Long, Integer> roadInfo, List<TrafficActionObject> trafficActions){
        try {
            this.RmiObject.nextTick(tickLengthInSeconds, roadInfo, trafficActions);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return true;
    }

    public double calculatefastestWayToDestination(Location start, Location end) {

        try {
            return this.RmiObject.calculatefastestWayToDestination(start, end);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public DumpDestination getDumpDestination() {
        try {
            return this.RmiObject.getDumpDestination();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FinalDestination> getFinalDestinations() {
        try {
            return this.RmiObject.getFinalDestinations();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getAnzCarsOnSameRoad(List<RelationshipInfoBean> relations, double shareOfLastRelation) {
        try {
            return this.RmiObject.getAnzCarsOnSameRoad(relations,shareOfLastRelation);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public void initRoutingKit(List<FinalDestination> destinations, List<TrafficActionObject> trafficActionObjects, DumpDestination dumpDestination){
        try {
            this.RmiObject.initRoutingkit(destinations, trafficActionObjects, dumpDestination);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public RoutingInformation routeAgent(RoutingInformation routingInfo){
        //long start = System.currentTimeMillis();
        try {
            routingInfo.setCurrentTick(Client.getInstance().getCurrentTick());
            RoutingInformation routingInformation = this.RmiObject.routeAgent(routingInfo, Client.getInstance().getCurrentTick());
            return routingInformation;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        //System.out.println("Time"+ (System.currentTimeMillis()-start));
        return null;
    }

    public Location findNodeNearestToLocation(Location location) {
        try {
            return this.RmiObject.findNodeNearestToLocation(location);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<VelocitySection> getVelocitySectionsInBoundingBox(Location max, Location min) {
        try {
            return this.RmiObject.getVelocitySectionsInBoundingBox(max, min);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<BikePath> getBikePathsInBoundingBox(Location locMax, Location locMin) {
        try {
            return this.RmiObject.getBikePathsInBoundingBox(locMax, locMin);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
