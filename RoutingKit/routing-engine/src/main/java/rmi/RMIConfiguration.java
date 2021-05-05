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
    public static final String IP_ADRESSE = System.getenv("AGADE_RE_RMI_ADDR_SERVER");
    //public static final String IP_ADRESSE = "0.0.0.0";
    //public static final String IP_ADRESSE = "routing-engine"; fixme: use this address when running both AGADE Backend and RoutingKit in Docker
    /**
     * The constant PORT.
     */
    public static final int FIXED_SOCKET_PORT = 1098;
    /**
     * The constant URL_NEO4J.
     */
    public static final String URL_NEO4J = System.getenv("AGADE_NEO4J_ADDR");
    //public static final String URL_NEO4J = "bolt://localhost:7687";
    //public static final String URL_NEO4J = "bolt://neo4j:7687";
    /**
     * The constant USER_NEO4J.
     */
    public static final String USER_NEO4J = "neo4j";
    /**
     * The constant PASS_NEO4J.
     */
    public static final String PASS_NEO4J = "1234";


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
