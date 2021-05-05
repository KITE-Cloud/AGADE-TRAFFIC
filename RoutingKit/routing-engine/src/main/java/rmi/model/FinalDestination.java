package rmi.model;

import java.io.Serializable;
import java.util.HashMap;

public class FinalDestination extends Destination implements Serializable {

    private static final long serialVersionUID = -3185114667747486775L;

    private double areaDiameterForParking;
    private HashMap<Long, ParkingDestination> parkingSlots;
    private HashMap <String, Double> durationTableBetweenParkingSlots;

    public FinalDestination() {
        this.parkingSlots = new HashMap<>();
        this.durationTableBetweenParkingSlots = new HashMap<>();
    }

    public double getAreaDiameterForParking() {
        return areaDiameterForParking;
    }

    public void setAreaDiameterForParking(double areaDiameterForParking) {
        this.areaDiameterForParking = areaDiameterForParking;
    }

    public HashMap<Long, ParkingDestination> getParkingSlots() {
        return parkingSlots;
    }

    public void setParkingSlots(HashMap<Long, ParkingDestination> parkingSlots) {
        this.parkingSlots = parkingSlots;
    }

    public HashMap<String, Double> getDurationTableBetweenParkingSlots() {
        return durationTableBetweenParkingSlots;
    }

    public void setDurationTableBetweenParkingSlots(HashMap<String, Double> durationTableBetweenParkingSlots) {
        this.durationTableBetweenParkingSlots = durationTableBetweenParkingSlots;
    }
}