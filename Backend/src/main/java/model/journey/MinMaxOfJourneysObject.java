package model.journey;

public class MinMaxOfJourneysObject {

    double minDistance;
    double maxDistance;
    double minShoppingUtility;
    double maxShoppingUtility;
    double minModeUtility;
    double maxModeUtility;
    double minParking;
    double maxParking;

    public MinMaxOfJourneysObject() {
    }


    public MinMaxOfJourneysObject(double minDistance, double maxDistance, double minShoppingUtility, double maxShoppingUtility, double minModeUtility, double maxModeUtility, double minParking, double maxParking) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.minShoppingUtility = minShoppingUtility;
        this.maxShoppingUtility = maxShoppingUtility;
        this.minModeUtility = minModeUtility;
        this.maxModeUtility = maxModeUtility;
        this.minParking = minParking;
        this.maxParking = maxParking;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public double getMinShoppingUtility() {
        return minShoppingUtility;
    }

    public void setMinShoppingUtility(double minShoppingUtility) {
        this.minShoppingUtility = minShoppingUtility;
    }

    public double getMaxShoppingUtility() {
        return maxShoppingUtility;
    }

    public void setMaxShoppingUtility(double maxShoppingUtility) {
        this.maxShoppingUtility = maxShoppingUtility;
    }

    public double getMinModeUtility() {
        return minModeUtility;
    }

    public void setMinModeUtility(double minModeUtility) {
        this.minModeUtility = minModeUtility;
    }

    public double getMaxModeUtility() {
        return maxModeUtility;
    }

    public void setMaxModeUtility(double maxModeUtility) {
        this.maxModeUtility = maxModeUtility;
    }

    public double getMinParking() {
        return minParking;
    }

    public void setMinParking(double minParking) {
        this.minParking = minParking;
    }

    public double getMaxParking() {
        return maxParking;
    }

    public void setMaxParking(double maxParking) {
        this.maxParking = maxParking;
    }
}
