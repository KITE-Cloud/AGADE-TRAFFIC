package model;

import rmi.model.Location;

import java.io.Serializable;

public class DumpDestination implements Serializable {

    private static final long serialVersionUID = 1814331000992555911L;

    private String name;
    private int capacity;
    private int currentlyWaiting;
    private Location location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentlyWaiting() {
        return currentlyWaiting;
    }

    public void setCurrentlyWaiting(int currentlyWaiting) {
        this.currentlyWaiting = currentlyWaiting;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
