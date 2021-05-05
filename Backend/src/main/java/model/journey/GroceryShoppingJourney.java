package model.journey;

import model.frontend.StartEnd;
import model.ontologyPojos.Supermarket;
import org.jetbrains.annotations.NotNull;
import rmi.model.Vehicle;
import utility.MathUtil;

import java.util.ArrayList;
import java.util.List;

public class GroceryShoppingJourney extends Journey implements Comparable<GroceryShoppingJourney> {

    private List<StartEnd> trips;
    private List<Vehicle> transportationMode;
    private List<Double> modeUtility;
    private List<Double> estimatedTravelDistances;
    private List<Double> shoppingUtility;
    private List<String> groceryList;
    private List<Supermarket> visitedSupermarket;
    private double totalTravelDistance;
    private double totalUtilityScore;
    private List<Double> parkingSlots;

    public GroceryShoppingJourney() {
        trips = new ArrayList<>();
        transportationMode = new ArrayList<>();
        this.modeUtility = new ArrayList<>();
        this.estimatedTravelDistances = new ArrayList<>();
        this.shoppingUtility = new ArrayList<>();
        this.groceryList = new ArrayList<>();
        this.visitedSupermarket = new ArrayList<>();
        this.parkingSlots = new ArrayList<>();
        this.totalUtilityScore = -1;
    }


    public GroceryShoppingJourney(List<StartEnd> trips, List<Vehicle> transportationMode, List<Double> modeUtility, List<Double> estimatedTravelDistances, List<Double> shoppingUtility, List<String> groceryList, List<Supermarket> visitedSupermarket) {
        this.trips = new ArrayList<>(trips);
        this.transportationMode = new ArrayList<>(transportationMode);
        this.modeUtility = new ArrayList<>(modeUtility);
        this.estimatedTravelDistances = new ArrayList<>(estimatedTravelDistances);
        this.shoppingUtility = new ArrayList<>(shoppingUtility);
        this.groceryList = new ArrayList<>(groceryList);
        this.visitedSupermarket = new ArrayList<>(visitedSupermarket);
        this.parkingSlots = new ArrayList<>();
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

    public List<Double> getModeUtility() {
        return modeUtility;
    }

    public Double getAvgModeUtility() {
        return MathUtil.sumOfList(modeUtility) / this.visitedSupermarket.size();
    }

    public void setModeUtility(List<Double> modeUtility) {
        this.modeUtility = modeUtility;
    }

    public List<Double> getEstimatedTravelDistances() {
        return estimatedTravelDistances;
    }

    public void setEstimatedTravelDistances(List<Double> estimatedTravelDistances) {
        this.estimatedTravelDistances = estimatedTravelDistances;
    }

    public List<String> getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(List<String> groceryList) {
        this.groceryList = groceryList;
    }

    public List<Double> getShoppingUtility() {
        return shoppingUtility;
    }

    public Double getAvgShoppingUtility() {
        return MathUtil.sumOfList(shoppingUtility) / this.visitedSupermarket.size();
    }

    public void setShoppingUtility(List<Double> shoppingUtility) {
        this.shoppingUtility = shoppingUtility;
    }

    public List<Supermarket> getVisitedSupermarket() {
        return visitedSupermarket;
    }

    public void setVisitedSupermarket(List<Supermarket> visitedSupermarket) {
        this.visitedSupermarket = visitedSupermarket;
    }

    @Override
    public double getTotalUtilityScore() {
        return totalUtilityScore;
    }

    @Override
    public void setTotalUtilityScore(double totalUtilityScore) {
        this.totalUtilityScore = totalUtilityScore;
    }

    public double getTotalTravelDistance() {
        return MathUtil.sumOfList(estimatedTravelDistances);
    }

    public List<Double> getParkingSlots() {
        return parkingSlots;
    }

    public void setParkingSlots(List<Double> parkingSlots) {
        this.parkingSlots = parkingSlots;
    }

    public void setTotalTravelDistance(double totalTravelDistance) {
        this.totalTravelDistance = totalTravelDistance;
    }

    @Override
    public int compareTo(@NotNull GroceryShoppingJourney o) {
        return Double.compare(getTotalUtilityScore(), o.getTotalUtilityScore());
    }
}
