package rmi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RouteChangeBean implements Serializable {

    private static final long serialVersionUID = -3261746819555838288L;
    private Boolean hasChanged;
    private Location location;
    private Vehicle vehicle;
    private List<String> activeRules;

    public RouteChangeBean() {
    }

    public RouteChangeBean(Boolean hasChanged, Location location, Vehicle vehicle) {
        this.hasChanged = hasChanged;
        this.location = location;
        this.vehicle = vehicle;
    }

    public Boolean getHasChanged() {
        return hasChanged;
    }

    public void setHasChanged(Boolean hasChanged) {
        this.hasChanged = hasChanged;
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

    public List<String> getActiveRules() {
        if(activeRules == null) activeRules = new ArrayList<>();
        return activeRules;
    }

    public void setActiveRules(List<String> activeRules) {
        this.activeRules = activeRules;
    }
}
