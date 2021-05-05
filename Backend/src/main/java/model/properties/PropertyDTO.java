package model.properties;

import utility.DateUtil;

import java.io.File;
import java.util.Map;

/**
 * Created by Thomas on 13.04.2015.
 */
public class PropertyDTO {

    private static final String START_DATE = DateUtil.getCurrentFormattedDate();
    private String simulationName;
    private int numberOfAgents;
    private int numberOfRounds;
    private String adlOntologyLocation;
    private String sdlOntologyLocation;
    private String[] idlOntologyLocations;
    private boolean agentVisualisation;
    private int pauseBetweenTwoRoundsInSec;
    private String simulationResultsSaveFolder;
    private String protegeDirectory;
    private String[] agentClassFiles;
    private Map<String, Map<String, Integer>> agentIDLAssociation;

    public PropertyDTO() {
        simulationName = "";
        numberOfAgents = 50;
        numberOfRounds = 100;
        adlOntologyLocation = "";
        sdlOntologyLocation = "";
        agentVisualisation = true;
        pauseBetweenTwoRoundsInSec = 1;
        simulationResultsSaveFolder = "";
        protegeDirectory = "";
    }

    public PropertyDTO(String simulationName, int numberOfAgents, int numberOfRounds,
                       String adlOntologyLocation,
                       String sdlOntologyLocation, boolean agentVisualisation, int pauseBetweenTwoRoundsInSec,
                       String simulationResultsSaveFolder,
                       String protegeDirectory) {
        this.simulationName = simulationName;
        this.numberOfAgents = numberOfAgents;
        this.numberOfRounds = numberOfRounds;
        this.adlOntologyLocation = adlOntologyLocation;
        this.sdlOntologyLocation = sdlOntologyLocation;
        this.agentVisualisation = agentVisualisation;
        this.pauseBetweenTwoRoundsInSec = pauseBetweenTwoRoundsInSec;
        this.simulationResultsSaveFolder = simulationResultsSaveFolder;
        this.protegeDirectory = protegeDirectory;
    }

    public static String getStartDate() {
        return START_DATE;
    }

    public Map<String, Map<String, Integer>> getAgentIDLAssociation() {
        return agentIDLAssociation;
    }

    public void setAgentIDLAssociation(Map<String, Map<String, Integer>> agentIDLAssociation) {
        this.agentIDLAssociation = agentIDLAssociation;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public void setSimulationName(String simulationName) {
        this.simulationName = simulationName;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public String getAdlOntologyLocation() {
        return adlOntologyLocation;
    }

    public void setAdlOntologyLocation(String adlOntologyLocation) {
        this.adlOntologyLocation = adlOntologyLocation;
    }

    public String getSdlOntologyLocation() {
        return sdlOntologyLocation;
    }

    public void setSdlOntologyLocation(String sdlOntologyLocation) {
        this.sdlOntologyLocation = sdlOntologyLocation;
    }

    public boolean isAgentVisualisation() {
        return agentVisualisation;
    }

    public void setAgentVisualisation(boolean agentVisualisation) {
        this.agentVisualisation = agentVisualisation;
    }

    public int getPauseBetweenTwoRoundsInSec() {
        return pauseBetweenTwoRoundsInSec;
    }

    public void setPauseBetweenTwoRoundsInSec(int pauseBetweenTwoRoundsInSec) {
        this.pauseBetweenTwoRoundsInSec = pauseBetweenTwoRoundsInSec;
    }

    public String getSimulationResultsSaveFolder() {
        return simulationResultsSaveFolder;
    }

    public void setSimulationResultsSaveFolder(String simulationResultsSaveFolder) {
        this.simulationResultsSaveFolder = simulationResultsSaveFolder;
    }

    public String[] getIdlOntologyLocations() {
        return idlOntologyLocations;
    }

    public void setIdlOntologyLocations(File[] idlOntologyLocations) {
        String fileArray[] = new String[idlOntologyLocations.length];
        for (int i = 0; i < idlOntologyLocations.length; i++) {
            fileArray[i] = idlOntologyLocations[i].getAbsolutePath();
        }
        this.idlOntologyLocations = fileArray;
    }

    public void setIdlOntologyLocations(String[] idlOntologyLocations) {
        this.idlOntologyLocations = idlOntologyLocations;
    }

    public String[] getAgentClassFiles() {
        return agentClassFiles;
    }

    public void setAgentClassFiles(File[] agentClassFiles) {
        String fileArray[] = new String[agentClassFiles.length];
        for (int i = 0; i < agentClassFiles.length; i++) {
            fileArray[i] = agentClassFiles[i].getAbsolutePath();
        }
        this.agentClassFiles = fileArray;
    }

    public void setAgentClassFiles(String[] agentClassFiles) {
        this.agentClassFiles = agentClassFiles;
    }

    public String getProtegeDirectory() {
        return protegeDirectory;
    }

    public void setProtegeDirectory(String protegeDirectory) {
        this.protegeDirectory = protegeDirectory;
    }
}
