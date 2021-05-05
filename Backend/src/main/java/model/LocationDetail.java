package model;

import rmi.model.Location;

import java.io.Serializable;

public class LocationDetail implements Serializable {

    private static final long serialVersionUID = -266203028592394576L;
    private String simId;
    private int directParkingSlots;
    private int directParkedCars;
    private double parkingArea;
    private int surroundingParkingSlots;
    private int surroundingParkedCars;
    private Location location;
    private String locationType;
    private int bikesParked;

    public LocationDetail() {
    }

    public LocationDetail(String simId, int directParkingSlots, int directParkedCars, double parkingArea, int surroundingParkingSlots, int surroundingParkedCars, Location location, String locationType, int bikesParked) {
        this.simId = simId;
        this.directParkingSlots = directParkingSlots;
        this.directParkedCars = directParkedCars;
        this.parkingArea = parkingArea;
        this.surroundingParkingSlots = surroundingParkingSlots;
        this.surroundingParkedCars = surroundingParkedCars;
        this.location = location;
        this.locationType = locationType;
        this.bikesParked = bikesParked;
    }

    public String getSimId() {
        return simId;
    }

    public void setSimId(String simId) {
        this.simId = simId;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public int getBikesParked() {
        return bikesParked;
    }

    public void setBikesParked(int bikesParked) {
        this.bikesParked = bikesParked;
    }
}
