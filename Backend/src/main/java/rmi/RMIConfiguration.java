package rmi;

/**
 * The type Rmi configuration.
 *
 * Here you can make the configurations for the RMI server.
 *
 * @author Joshua Prim, Jannik Enenkel, Jannik Geyer
 *
 */
public class RMIConfiguration {

    /**
     * The constant IP_ADRESSE.
     */
    public static final String IP_ADRESSE = System.getenv("AGADE_RE_RMI_ADDR_CLIENT");
    //public static final String IP_ADRESSE = "127.0.0.1";
    //public static final String IP_ADRESSE = "routing-engine"; fixme: use this address when running both AGADE Backend and RoutingKit in Docker

    private static RMIConfiguration instance;

    private RMIConfiguration(){}

    /**
     * Get instance rmi configuration.
     *
     * @return the rmi configuration
     */
    public static synchronized RMIConfiguration getInstance(){
        if (RMIConfiguration.instance == null){
            RMIConfiguration.instance = new RMIConfiguration();
        }
        return RMIConfiguration.instance;
    }
}
