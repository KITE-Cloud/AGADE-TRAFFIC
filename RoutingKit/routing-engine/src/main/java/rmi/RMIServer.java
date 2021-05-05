package rmi;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.RMISocketFactory;

/**
 * The type Rmi server.
 *
 * @author Joshua Prim, Jannik Enenkel, Jannik Geyer
 */
public class RMIServer {


    private static RMIConfiguration config;

    /**
     * Instantiates a new Rmi server.
     */
    public RMIServer() {
        System.setProperty("java.security.policy", "AllPermission.policy");
        this.config = RMIConfiguration.getInstance();
    }

    /**
     * Main.
     *
     * @param args the args
     * @throws RemoteException       the remote exception
     * @throws AlreadyBoundException the already bound exception
     */
    public static void main(String args[])  {

        try {
            RMISocketFactory.setSocketFactory(new FixedPortRMISocketFactory());
            System.out.println("RMIConfiguration.IP_ADRESSE = " + RMIConfiguration.IP_ADRESSE);
            System.setProperty("java.rmi.server.hostname",RMIConfiguration.IP_ADRESSE);
            System.setProperty("java.rmi.hostname", RMIConfiguration.IP_ADRESSE);

            Registry registry = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            RMIRemoteObject rmiRemoteObject = new RMIRemoteObject();
            registry.rebind("RMIServer", rmiRemoteObject);
            System.out.println("Routing engine started!");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
