package model.properties;

import constants.Constants;

import java.io.*;
import java.util.Properties;

/**
 * Created by Thomas on 13.04.2015.
 */
public class PropertyDAO {

    public static final String PROP_PATH = "farrenkopf/gui_simulation_config.properties";
    private static final PropertyDAO INSTANCE = new PropertyDAO();
    private static final String SIMULATION_NAME = "simulationName";
    private static final String NUMBER_OF_AGENTS = "numberOfAgents";
    private static final String NUMBER_OF_ROUNDS = "numberOfRounds";
    private static final String ADL_ONTOLOGY_LOCATION = "adlOntologyLocation";
    private static final String SDL_ONTOLOGY_LOCATION = "sdlOntologyLocation";
    private static final String IDL_ONTOLOGY_LOCATIONS = "idlOntologyLocations";
    private static final String AGENT_CLASS_FILES_LOCATION = "agentClassFilesLocation";
    private static final String AGENT_VISUALISATION = "agentVisualisation";
    private static final String PAUSE_BETWEEN_TWO_ROUNDS_IN_SEC = "pauseBetweenTwoRoundsInSec";
    private static final String SIMULATION_RESULTS_SAVE_FOLDER = "simulationResultsSaveFolder";
    private static final String PROTEGE_DIRECTORY = "protegeDirectory";
    private static final String opinionLeadershipValue = "opinionLeaderShipValue";
    private static final String hubGammaValue = "hubGammaValue";
    private static final PropertyDTO DEFAULT_DTO = new PropertyDTO();
    //Properties
    private Properties prop = new Properties();
    private PropertyDTO propertyDTO;

    private PropertyDAO() {

    }

    public static PropertyDAO getInstance() {
        return INSTANCE;
    }

    public void save(PropertyDTO propertyDTO) {
        OutputStream output;

        prop.setProperty(SIMULATION_NAME, propertyDTO.getSimulationName());
        prop.setProperty(NUMBER_OF_AGENTS, String.valueOf(propertyDTO.getNumberOfAgents()));
        prop.setProperty(NUMBER_OF_ROUNDS, String.valueOf(propertyDTO.getNumberOfRounds()));
        prop.setProperty(ADL_ONTOLOGY_LOCATION, propertyDTO.getAdlOntologyLocation());
        prop.setProperty(SDL_ONTOLOGY_LOCATION, propertyDTO.getSdlOntologyLocation());
        //        prop.setProperty("idlOntologyLocation", idlOntologyTextField.getText());
        prop.setProperty(AGENT_VISUALISATION, String.valueOf(propertyDTO.isAgentVisualisation()));
        prop.setProperty(PAUSE_BETWEEN_TWO_ROUNDS_IN_SEC,
                String.valueOf(propertyDTO.getPauseBetweenTwoRoundsInSec()));
        prop.setProperty(SIMULATION_RESULTS_SAVE_FOLDER,
                propertyDTO.getSimulationResultsSaveFolder());
        prop.setProperty(PROTEGE_DIRECTORY, propertyDTO.getProtegeDirectory());


/*
        StringBuffer idlOntologyLocationsBuffer = new StringBuffer();
        for (int i = 0; i < propertyDTO.getIdlOntologyLocations().length; i++) {
            idlOntologyLocationsBuffer.append(propertyDTO.getIdlOntologyLocations()[i]);
            idlOntologyLocationsBuffer.append(";");
        }
        prop.setProperty(IDL_ONTOLOGY_LOCATIONS, idlOntologyLocationsBuffer.toString());

        StringBuffer agentClassFilesBuffer = new StringBuffer();
        for (String agentClassFile : propertyDTO.getAgentClassFiles()){
            agentClassFilesBuffer.append(agentClassFile);
            agentClassFilesBuffer.append(";");
        }
        prop.setProperty(AGENT_CLASS_FILES_LOCATION, agentClassFilesBuffer.toString());

*/


        try {
            output = new FileOutputStream(System.getProperty("user.dir") + "/AGADE/src/main/resources/" + PROP_PATH);
            prop.store(output, null);
        } catch (IOException e) {
            System.out.println("Could not save property file!");
            e.printStackTrace();
        }
    }

    public PropertyDTO load() {
        //Check if PropertyDTO is already loaded. If yes, then return the existing object.
        if (propertyDTO != null)
            return propertyDTO;
        else
            propertyDTO = new PropertyDTO();


        try {
            InputStream resourceAsStream = PropertyDAO.class.getClassLoader().getResourceAsStream(PROP_PATH);
            if (resourceAsStream != null) {
                prop.load(resourceAsStream);
            } else {
                return DEFAULT_DTO;
            }

        } catch (FileNotFoundException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            return DEFAULT_DTO;
        } catch (IOException e) {
            e.printStackTrace();
        }

        propertyDTO.setSimulationName(prop.getProperty(SIMULATION_NAME));
        propertyDTO.setAdlOntologyLocation(prop.getProperty(ADL_ONTOLOGY_LOCATION));
        propertyDTO.setSdlOntologyLocation(prop.getProperty(SDL_ONTOLOGY_LOCATION));
        propertyDTO.setNumberOfAgents(Integer.parseInt(prop.getProperty(NUMBER_OF_AGENTS)));
        propertyDTO.setNumberOfRounds(Integer.parseInt(prop.getProperty(NUMBER_OF_ROUNDS)));
        propertyDTO.setPauseBetweenTwoRoundsInSec(
                Integer.parseInt(prop.getProperty(PAUSE_BETWEEN_TWO_ROUNDS_IN_SEC)));
        propertyDTO.setProtegeDirectory(prop.getProperty(PROTEGE_DIRECTORY));
        propertyDTO
                .setSimulationResultsSaveFolder(prop.getProperty(SIMULATION_RESULTS_SAVE_FOLDER));
        propertyDTO.setAgentVisualisation(
                Boolean.parseBoolean(prop.getProperty(AGENT_VISUALISATION)));
        //Split up the property into a String[] variable type
        String idlOntologyLocations = prop.getProperty(IDL_ONTOLOGY_LOCATIONS, null);
        if (idlOntologyLocations != null)
            propertyDTO.setIdlOntologyLocations(idlOntologyLocations.split(";"));
        String agentClassFiles = prop.getProperty(AGENT_CLASS_FILES_LOCATION, null);
        if (agentClassFiles != null)
            propertyDTO.setAgentClassFiles(agentClassFiles.split(";"));

        if (Constants.jarMode) {
            propertyDTO.setAdlOntologyLocation("farrenkopf/ontologies/bioFuel/AbstractDomainLayer.owl");
            propertyDTO.setSdlOntologyLocation("farrenkopf/ontologies/bioFuel/BioFuelDomainLayer.owl");
            //propertyDTO.setSdlOntologyLocation("ontologies/opinionLeadership/PhonemarketDomainLayer.owl");
        }

        return propertyDTO;
    }
}
