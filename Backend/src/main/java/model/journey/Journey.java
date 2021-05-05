package model.journey;

import model.frontend.StartEnd;
import rmi.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class Journey {

    private List<StartEnd> trips;
    private List<Vehicle> transportationMode;
    private double totalTravelDistance;
    private double totalUtilityScore;

    public Journey() {
        trips = new ArrayList<>();
        transportationMode = new ArrayList<>();
        totalUtilityScore =-1;
    }

    public Journey(List<StartEnd> trips, List<Vehicle> transportationMode) {
        this.trips = trips;
        this.transportationMode = transportationMode;
    }

    public List<StartEnd> getTrips() {
        return trips;
    }

    public void setTrips(List<StartEnd> trips) {
        this.trips = trips;
    }

    public List<Vehicle> getTransportationMode() {
        return transportationMode;
    }

    public void setTransportationMode(List<Vehicle> transportationMode) {
        this.transportationMode = transportationMode;
    }

    public double getTotalTravelDistance() {
        return totalTravelDistance;
    }

    public void setTotalTravelDistance(double totalTravelDistance) {
        this.totalTravelDistance = totalTravelDistance;
    }

    public double getTotalUtilityScore() {
        return totalUtilityScore;
    }

    public void setTotalUtilityScore(double totalUtilityScore) {
        this.totalUtilityScore = totalUtilityScore;
    }
}
