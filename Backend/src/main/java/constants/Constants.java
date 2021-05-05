package constants;

import jadex.bridge.service.types.cms.IComponentManagementService;

import java.io.File;
import java.util.Properties;

public class Constants {

    public static final String GUI_TITLE = "AGADE";
    public static final boolean autoPlay = false;
    public static int socialNetworkAlgorithm;

    // = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * =
    // O N T O L O G Y C O N S T A N T S
    // = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * =

    public static final String ontINDMyself = "myself";
    public static final String ontDPCurrentRound = "currentRound";
    public static final String ontDPHasPriority = "hasPriority";
    public static final String ontOPHasBrand = "hasBrand";
    // = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * =
    // P L A N C O N S T A N T S
    // = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * = * =
    public static final String plnAnswerRequestForAcquaintancePlan =
        "answerRequestForAcquaintancePlan";
    public static final String plnAskForCurrentProductInformationPlan =
        "askForCurrentProductInformationPlan";
    public static final String plnAskForRecommendationPlan = "askForRecommendationPlan";
    public static final String plnProcessCalculationPhasePlan = "processCalculationPhasePlan";
    public static final String plnEquipParticipantsWithOntologyPlan =
        "equipParticipantsWithOntologyPlan";
    public static final String plnRoundFinishedPlan =
            "roundFinishedPlan";
    public static final String plnFollowPersonalPreferencesPlan = "followPersonalPreferencesPlan";
    public static final String plnFollowSocialAffiliationPlan = "followSocialAffiliationPlan";
    public static final String plnGoShoppingPlan = "goShoppingPlan";
    public static final String plnProcessActingPhasePlan = "processActingPhasePlan";
    public static final String plnProcessSocializationPhasePlan = "processSocializationPhasePlan";


    /**
     * The IRI (International Resource Identifier) of the ontology every simulation participant is equipped with at the beginning. Value is read from
     * resources.simulation_config.model.properties.
     */
    public static final String ONTO_IRI = "http://www.thm.de/mnd/wbm#";
    public static int socialNetworkGamma;
    public static boolean jarMode;
    // = ! = ! = ! = ! = ! = ! = ! = ! = ! = ! = ! = ! = ! =
    // D A N G E R D A N G E R D A N G E R
    // only use when you're pretty damn sure know what you're doing....
    public static IComponentManagementService cms;
    /**
     * Folder where the start up ontologies for each participant can be found. Values are read from resources.simulation_config.model.properties.
     */
    public static String adlOntoLocation;
    public static String sdlOntoLocation;

    /**
     * Folder where the default socialMatrix is stored
     */
    public static String defaultSocialNetworkConfig;
    /**
     * Folder where the particpants will save their respective ontologies. Value is read from resources.simulation_config.model.properties.
     */
    public static String ontoSave;
    /**
     * Folder where the charts will be saved after a simulation is completed
     */
    public static String chartSave_directory;
    /**
     * Folder where the jadex.agents will save their log file
     */
    public static String log_directory;
    /**
     * Folder where temporary ontologies will be saved
     */
    public static String temporary_ontology;
    /**
     * Program Folder of the protege ontology editor
     */
    public static String protege_directory;
    /**
     * Program Folder of the RacerPro
     */
    public static String racerPro_directory;
    /**
     * Reasoner to use during the simulation. Value is read from resources.simulation_config.model.properties.
     */
    public static String setReasoner;
    public static Integer directTheGameTimeOutAfter;
    public static Integer numberOfRounds;
    public static Integer numberOfParticipants;
    public static Integer numberOfSellers;
    public static Integer clientNumber;
    public static String serverAddress;
    public static int serverPort;
    public static String clientAddress;
    public static int clientPort;
    /**
     * Specifies the way how the mobile phone are distributed initially
     * 0=none 1=yes 2=just hubs
     */
    public static Integer equipAgentsWithItemsMode;
    /**
     * Specifies the way the social network of the current simulation will be created. Current Values are a) random and b) adjacencyMatrix.
     */
    public static String socialNetworkCreationType;

    // Prefixes for SPARQL-QUERIES
    public static String prefixes;
    // Variables of Testee


    /**
     * Defines the initial percentage of participating consumer jadex.agents each seller agent is known by.
     */
    public static Double initialBrandAwareness;
    /**
     * What percentage of jadex.agents initially knowing a seller possess a handy from that seller?
     */
    public static Double initialConversionRate;
    /**
     * Specifies the gamma value influencing whether an agent is a hub or not
     */
    public static double hubGamma;
    public static double hubOpinionValue;
    /**
     * Specifies the probability value creating an indirected edge between two nodes when using Barabasi social matrix algorithm
     */
    public static float probOfIndirectEdge;
    /**
     * Specifies the upper bound of the visualisation of jadex.agents by JUNG
     */
    public static boolean agentVisualisation;
    /**
     * Determines the necessary acquaintance value for directly knowing the handy possessed by an acquaintance
     */
    public static Integer informationAcquaintanceBarrier;


    static {
        try {
            Properties p = new Properties();
            p.load(Constants.class.getClassLoader()
                    .getResourceAsStream("farrenkopf/simulation_config.properties"));
            //            ONTO_IRI = p.getProperty("iri");
            adlOntoLocation = p.getProperty("adl_ontology_location");
            sdlOntoLocation = p.getProperty("sdl_ontology_location");


            // SPARQL Query Prefix
            prefixes = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                    + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
                    + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
                    + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + "PREFIX pm: <"
                    + ONTO_IRI + ">\n";
            racerPro_directory = p.getProperty("racerPro");
            ontoSave = p.getProperty("ontology_save");
            chartSave_directory = p.getProperty("chart_save_directory");
            log_directory = p.getProperty("agent_log_directory");
            setReasoner = p.getProperty("reasoner");
            initialBrandAwareness = Double.parseDouble(p.getProperty("brand_awareness"));
            initialConversionRate = Double.parseDouble(p.getProperty("initial_conversion_rate"));
            equipAgentsWithItemsMode = Integer.parseInt(p.getProperty("equip_agents_with_items_mode"));
            socialNetworkGamma = Integer.parseInt(p.getProperty("socialNetworkGamma"));
            hubGamma = Double.parseDouble(p.getProperty("hubGamma"));
            hubOpinionValue = Double.parseDouble(p.getProperty("hubOpinionValue"));

            probOfIndirectEdge = Float.parseFloat(p.getProperty("probability_of_indirect_edge"));
            informationAcquaintanceBarrier =
                    Integer.parseInt(p.getProperty("information_acquaintance_barrier"));
            directTheGameTimeOutAfter =
                    Integer.parseInt(p.getProperty("direct_the_game_time_out_after")) * 1000;
            agentVisualisation =
                    Boolean.parseBoolean(p.getProperty("agentVisualisation"));
            numberOfRounds = Integer.parseInt(p.getProperty("number_of_rounds"));
            numberOfParticipants = Integer.parseInt(p.getProperty("number_of_participants"));
            numberOfSellers = Integer.parseInt(p.getProperty("number_of_sellers"));
            socialNetworkAlgorithm = Integer.parseInt(p.getProperty("socialNetworkAlgorithm"));
            serverAddress = p.getProperty("serverAddress");
            serverPort = Integer.parseInt(p.getProperty("serverPort"));
            clientAddress = p.getProperty("clientAddress");
            clientPort = Integer.parseInt(p.getProperty("clientPort"));
            clientNumber = Integer.parseInt(p.getProperty("clientNumber"));
            defaultSocialNetworkConfig = p.getProperty("defaultSocialNetworkConfig");
            socialNetworkCreationType = p.getProperty("socialNetworkCreationType");
            jarMode = Boolean.valueOf(p.getProperty("jarMode"));
            // Create directory for saving temporary ontologies
            // directories can be specified in the resources.simulation_config.model.properties file
            // if the temporary_ontology property is omitted, the temporary
            // ontologies will be stored
            // in a phoneMarket folder in the user home directory
            temporary_ontology = p.getProperty("temporary_ontology");
            if (temporary_ontology == null) {
                // System.out.println("Test");
                String user_home = System.getProperty("user.home");
                temporary_ontology =
                        user_home + File.separator + ".phoneMarket" + File.separator + "tmp";
            }
            File f = new File(temporary_ontology);
            // System.out.println(temporary_ontology);
            if (!f.exists()) {
                f.mkdirs();
            }
            protege_directory = p.getProperty("protege_directory");
            // fis.close();
            // p.setProperty("iri",
            // "http://www.thm.de/mnd/wbm/phonemarket#BLUB");
            // FileOutputStream fos = new FileOutputStream(new
            // File(file.toURI()));
            // p.store(fos, "Header");
            // fos.close();
            // System.out.println("SAVED");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Deprecated
    public static String[] listFilesForFolder(String path, String rule) {
        File folder = new File(path);
        StringBuilder stringBuilder = new StringBuilder();
        for (File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                if (fileEntry.getName().contains(rule))
                    stringBuilder.append(path + "/" + fileEntry.getName() + ",");
            }
        }
        if (stringBuilder.length() == 0) {
            throw new RuntimeException("No ontology containing " + rule + " found!");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString().split(",");
    }
}
