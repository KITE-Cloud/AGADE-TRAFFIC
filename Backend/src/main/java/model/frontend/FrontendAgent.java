package model.frontend;


import model.RouteDetail;
import model.journey.Journey;
import rmi.model.Location;
import rmi.model.RouteChangeBean;
import rmi.model.Vehicle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FrontendAgent implements Serializable {

    private static final long serialVersionUID = 6012407857057878344L;
    int id;
    Vehicle vehicle;
    List<LocationFrontend> routeCoordinates;
    List<RouteDetail> routeDetails;
    List<Integer> velocities;
    Integer birthTick;
    Location origin;
    Location destination;
    int totalTravelTime;
    String personaProfile;
    HashMap<Integer, RouteChangeBean> routeHasChanged;
    Journey journey;


    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public String getPersonaProfile() {
        return personaProfile;
    }

    public void setPersonaProfile(String personaProfile) {
        this.personaProfile = personaProfile;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public int getTotalTravelTime() {
        return totalTravelTime;
    }

    public void setTotalTravelTime(int totalTravelTime) {
        this.totalTravelTime = totalTravelTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<LocationFrontend> getRouteCoordinates() {
        return routeCoordinates;
    }

    public List<double[]> getRouteCoordinatesAsArray() {
        List<double[]> coordinates = new ArrayList<>();

        for (LocationFrontend LocationFrontend : routeCoordinates){
            double data[] = new double[2];
            data [0] = LocationFrontend.getLocation().getLat();
            data [1] = LocationFrontend.getLocation().getLon();
            coordinates.add(data);
        }

        return coordinates;
    }

    public void setRouteCoordinates(List<LocationFrontend> routeCoordinates) {
        this.routeCoordinates = routeCoordinates;
    }

    public List<Integer> getVelocities() {
        return velocities;
    }

    public void setVelocities(List<Integer> velocities) {
        this.velocities = velocities;
    }


    public Integer getBirthTick() {
        return birthTick;
    }

    public void setBirthTick(Integer birthTick) {
        this.birthTick = birthTick;
    }


    public Location getOrigin() {
        return origin;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public List<RouteDetail> getRouteDetails() {
        return routeDetails;
    }

    public void setRouteDetails(List<RouteDetail> routeDetails) {
        this.routeDetails = routeDetails;
    }

    public HashMap<Integer, RouteChangeBean> getRouteHasChanged() {
        return routeHasChanged;
    }

    public void setRouteHasChanged(HashMap<Integer, RouteChangeBean> routeHasChanged) {
        this.routeHasChanged = routeHasChanged;
    }
}
