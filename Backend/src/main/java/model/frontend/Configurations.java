package model.frontend;


import model.DumpDestination;
import model.ontologyPojos.PersonaProfile;
import rmi.model.FinalDestination;
import rmi.model.TrafficActionObject;

import java.io.Serializable;
import java.util.List;

public class Configurations implements Serializable {
    private static final long serialVersionUID = 8164582347558451536L;
    private List<StartEnd> destinationMatrix;
    private int simulationTime;
    private List<TrafficActionObject> trafficActionObjects;
    private int lastTickInSimulation;
    private List<FinalDestination> finalDestinations;
    private DumpDestination dumpDestination;
    private String agentGeneratorType;
    boolean fromPopulation;
    private List<PersonaProfile> personaProfiles;
    private String populationConfig;


    public List<StartEnd> getDestinationMatrix() {
        return destinationMatrix;
    }

    public void setDestinationMatrix(List<StartEnd> destinationMatrix) {
        this.destinationMatrix = destinationMatrix;
    }

    public int getSimulationTime() {
        return simulationTime;
    }

    public void setSimulationTime(int simulationTime) {
        this.simulationTime = simulationTime;
    }

    public List<TrafficActionObject> getTrafficActionObjects() {
        return trafficActionObjects;
    }

    public void setTrafficActionObjects(List<TrafficActionObject> trafficActionObjects) {
        this.trafficActionObjects = trafficActionObjects;
    }

    public int getLastTickInSimulation(){
        return this.lastTickInSimulation;
    }

    public void setLastTickInSimulation(int lastTickInSimulation){
        this.lastTickInSimulation = lastTickInSimulation;
    }


    public List<FinalDestination> getFinalDestinations() {
        return finalDestinations;
    }

    public void setFinalDestinations(List<FinalDestination> finalDestinations) {
        this.finalDestinations = finalDestinations;
    }

    public DumpDestination getDumpDestination() {
        return dumpDestination;
    }

    public void setDumpDestination(DumpDestination dumpDestination) {
        this.dumpDestination = dumpDestination;
    }

    public String getAgentGeneratorType() {
        return agentGeneratorType;
    }

    public void setAgentGeneratorType(String agentGeneratorType) {
        this.agentGeneratorType = agentGeneratorType;
    }

    public List<PersonaProfile> getPersonaProfiles() {
        return personaProfiles;
    }

    public void setPersonaProfiles(List<PersonaProfile> personaProfiles) {
        this.personaProfiles = personaProfiles;
    }

    public String getPopulationConfig() {
        return populationConfig;
    }

    public void setPopulationConfig(String populationConfig) {
        this.populationConfig = populationConfig;
    }

    public boolean isFromPopulation() {
        return fromPopulation;
    }

    public void setFromPopulation(boolean fromPopulation) {
        this.fromPopulation = fromPopulation;
    }
}
