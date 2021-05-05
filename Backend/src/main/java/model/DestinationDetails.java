package model;

import java.io.Serializable;

public class DestinationDetails implements Serializable {
    private static final long serialVersionUID = -8192886596890852340L;

    private String simId;
    private String locLat;
    private String locLon;
    private String locName;
    private String locType;
    private int directParkingSlots;
    private int directParkedCars;
    private double parkingArea;
    private int surroundingParkingSlots;
    private int surroundingParkedCars;


    public DestinationDetails() {
    }

    public DestinationDetails(String simId, String locLat, String locLon, String locName, String locType,
                              int directParkingSlots, int directParkedCars, double parkingArea,
                              int surroundingParkingSlots, int surroundingParkedCars) {
        this.simId = simId;
        this.locLat = locLat;
        this.locLon = locLon;
        this.locName = locName;
        this.locType = locType;
        this.directParkingSlots = directParkingSlots;
        this.directParkedCars = directParkedCars;
        this.parkingArea = parkingArea;
        this.surroundingParkingSlots = surroundingParkingSlots;
        this.surroundingParkedCars = surroundingParkedCars;
    }

    public String getSimId() {
        return simId;
    }

    public void setSimId(String simId) {
        this.simId = simId;
    }

    public String getLocLat() {
        return locLat;
    }

    public void setLocLat(String locLat) {
        this.locLat = locLat;
    }

    public String getLocLon() {
        return locLon;
    }

    public void setLocLon(String locLon) {
        this.locLon = locLon;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    public int getDirectParkingSlots() {
        return directParkingSlots;
    }

    public void setDirectParkingSlots(int directParkingSlots) {
        this.directParkingSlots = directParkingSlots;
    }

    public int getDirectParkedCars() {
        return directParkedCars;
    }

    public void setDirectParkedCars(int directParkedCars) {
        this.directParkedCars = directParkedCars;
    }

    public double getParkingArea() {
        return parkingArea;
    }

    public void setParkingArea(double parkingArea) {
        this.parkingArea = parkingArea;
    }

    public int getSurroundingParkingSlots() {
        return surroundingParkingSlots;
    }

    public void setSurroundingParkingSlots(int surroundingParkingSlots) {
        this.surroundingParkingSlots = surroundingParkingSlots;
    }

    public int getSurroundingParkedCars() {
        return surroundingParkedCars;
    }

    public void setSurroundingParkedCars(int surroundingParkedCars) {
        this.surroundingParkedCars = surroundingParkedCars;
    }
}
