package controller;

import java.util.HashMap;
import java.util.Map;


public class MASConfiguration {

    public static String DB_PATH = System.getenv("AGADE_NEO4J_DATA");
    //public static String DB_PATH = PropertyLoader.getProperty("Neo4J-Data");
    private static MASConfiguration instance;

    private MASConfiguration() {
    }

    public static synchronized MASConfiguration getInstance() {
        if (MASConfiguration.instance == null) {
            MASConfiguration.instance = new MASConfiguration();
        }
        return MASConfiguration.instance;
    }

    public Map<String, String> getDBConfiguration() {
        Map<String, String> config = new HashMap<>();
        config.put("dbms.pagecache.memory", "4048M");
        config.put("dump_configuration", "true");
        config.put("use_memory_mapped_buffers", "true");
        config.put("dbms.security.auth_enabled", "false");
        return config;
    }

    /**
     *
     * @param streetType find more information at https://wiki.openstreetmap.org/wiki/Key:highway
     * @return a lambda value used for calculating variable costs
     */
    public double getLambdaForStreetType(String streetType){
        switch (streetType){
            // ---- ROADS ----
            case "motorway":
                return 0.01;
            case "trunk":
                return 0.01;
            case "primary":
                return 0.01;
            case "secondary":
                return 0.01;
            case "tertiary":
                return 0.01;
            case "unclassified":
                return 0.01;
            case "residential":
                return 0.01;
            case "service":
                return 0.01;
            // ---- LINK ROADS ----
            case "motorway_link":
                return 0.01;
            case "trunk_link":
                return 0.01;
            case "primary_link":
                return 0.01;
            case "secondary_link":
                return 0.01;
            case "tertiary_link":
                return 0.01;
            // ---- SPECIAL ROAD TYPES ----
            case "living_street":
                return 0.01;
            case "pedestrian":
                return 0.01;
            case "track":
                return 0.01;
            case "bus_guideway":
                return 0.01;
            case "escape":
                return 0.01;
            case "raceway":
                return 0.01;
            case "road":
                return 0.01;
            default:
                return 0.01;
        }
    }

}
