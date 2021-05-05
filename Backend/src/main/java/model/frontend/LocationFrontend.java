package model.frontend;

import rmi.model.Location;
import rmi.model.Vehicle;

import java.io.Serializable;

public class LocationFrontend implements Serializable {

    private static final long serialVersionUID = -106033538844218187L;
    Location location;
    Vehicle vehicle;

    public LocationFrontend() {
    }

    public LocationFrontend(Location location, Vehicle vehicle) {
        this.location = location;
        this.vehicle = vehicle;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
