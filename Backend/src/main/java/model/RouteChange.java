package model;

import java.io.Serializable;

public class RouteChange implements Serializable {

    private static final long serialVersionUID = -538450881533698134L;
    private String simId;
    private String locLatFrom;
    private String locLonFrom;
    private String locLatTo;
    private String locLonTo;
    private String vehicle;
    private String locLatChange;
    private String locLonChange;

    public RouteChange() {
    }

    public RouteChange(String simId, String locLatFrom, String locLonFrom, String locLatTo, String locLonTo,
                       String vehicle, String locLatChange, String locLonChange) {
        this.simId = simId;
        this.locLatFrom = locLatFrom;
        this.locLonFrom = locLonFrom;
        this.locLatTo = locLatTo;
        this.locLonTo = locLonTo;
        this.vehicle = vehicle;
        this.locLatChange = locLatChange;
        this.locLonChange = locLonChange;
    }

    public String getSimId() {
        return simId;
    }

    public void setSimId(String simId) {
        this.simId = simId;
    }

    public String getLocLatFrom() {
        return locLatFrom;
    }

    public void setLocLatFrom(String locLatFrom) {
        this.locLatFrom = locLatFrom;
    }

    public String getLocLonFrom() {
        return locLonFrom;
    }

    public void setLocLonFrom(String locLonFrom) {
        this.locLonFrom = locLonFrom;
    }

    public String getLocLatTo() {
        return locLatTo;
    }

    public void setLocLatTo(String locLatTo) {
        this.locLatTo = locLatTo;
    }

    public String getLocLonTo() {
        return locLonTo;
    }

    public void setLocLonTo(String locLonTo) {
        this.locLonTo = locLonTo;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getLocLatChange() {
        return locLatChange;
    }

    public void setLocLatChange(String locLatChange) {
        this.locLatChange = locLatChange;
    }

    public String getLocLonChange() {
        return locLonChange;
    }

    public void setLocLonChange(String locLonChange) {
        this.locLonChange = locLonChange;
    }
}
