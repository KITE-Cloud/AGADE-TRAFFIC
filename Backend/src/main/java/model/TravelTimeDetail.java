package model;

import rmi.model.Location;

import java.io.Serializable;

public class TravelTimeDetail implements Serializable {

    private static final long serialVersionUID = 4160149495777889367L;
    private String simId;
    private int phase;
    private String driverType;
    private int numberAgents;
    private double avgTravelTime;
    private Location locationFrom;
    private Location locationTo;


    public TravelTimeDetail() {
    }

    public TravelTimeDetail(String simId, int phase, String driverType, int numberAgents, double avgTravelTime, Location locationFrom, Location locationTo) {
        this.simId = simId;
        this.phase = phase;
        this.driverType = driverType;
        this.numberAgents = numberAgents;
        this.avgTravelTime = avgTravelTime;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
    }

    public String getSimId() {
        return simId;
    }

    public void setSimId(String simId) {
        this.simId = simId;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }


    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public int getNumberAgents() {
        return numberAgents;
    }

    public void setNumberAgents(int numberAgents) {
        this.numberAgents = numberAgents;
    }

    public double getAvgTravelTime() {
        return avgTravelTime;
    }

    public void setAvgTravelTime(double avgTravelTime) {
        this.avgTravelTime = avgTravelTime;
    }

    public Location getLocationFrom() {
        return locationFrom;
    }

    public void setLocationFrom(Location locationFrom) {
        this.locationFrom = locationFrom;
    }

    public Location getLocationTo() {
        return locationTo;
    }

    public void setLocationTo(Location locationTo) {
        this.locationTo = locationTo;
    }
}
