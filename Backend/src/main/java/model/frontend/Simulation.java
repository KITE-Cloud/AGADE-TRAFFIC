package model.frontend;

import model.*;
import model.statisticalData.Statistics;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Simulation implements Serializable {
    private static final long serialVersionUID = -3636330854894948732L;
    @XmlElement(name="simulationID")
    private String simulationID;
    private List<FrontendAgent> agents;
    private int currentTick;
    //private List<PathCount> pathCounts;
    private Statistics statisticalKPIs;
    private List<TravelTimeDetail> travelTimeDetails;
    private List<LocationDetail> locationDetails;
    private Configurations configurations;

    public Simulation() {
    }

    public Simulation(String simulationID){ this.simulationID = simulationID;}

    public void setSimulationID(String simulationID) {
        this.simulationID = simulationID;
    }

    public String getSimulationID() {
        return simulationID;
    }

    public void setAgents(List<FrontendAgent> agents) {
        this.agents = agents;
    }

    public List<FrontendAgent> getAgents(){
        return agents;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }

    /*public List<PathCount> getPathCounts() {
        return pathCounts;
    }

    public void setPathCounts(List<PathCount> pathCounts) {
        this.pathCounts = pathCounts;
    }*/


    public Statistics getStatisticalKPIs() {
        return statisticalKPIs;
    }

    public void setStatisticalKPIs(Statistics statisticalKPIs) {
        this.statisticalKPIs = statisticalKPIs;
    }

    public List<TravelTimeDetail> getTravelTimeDetails() {
        return travelTimeDetails;
    }

    public void setTravelTimeDetails(List<TravelTimeDetail> travelTimeDetails) {
        this.travelTimeDetails = travelTimeDetails;
    }

    public List<LocationDetail> getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(List<LocationDetail> locationDetails) {
        this.locationDetails = locationDetails;
    }

    public Configurations getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Configurations configurations) {
        this.configurations = configurations;
    }
}
