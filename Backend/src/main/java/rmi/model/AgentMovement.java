package rmi.model;

import java.io.Serializable;
public class AgentMovement implements Serializable {

    private static final long serialVersionUID = 7061601918325688083L;
    private Location from, to;
    private double durationInSec;
    private Location edgeStart, finalDest;
    private long edgeId;
    private Vehicle vehicle;

    public AgentMovement(Location from, Location to, double durationInSec) {
        this.from = from;
        this.to = to;
        this.durationInSec = durationInSec;
    }

    public AgentMovement(Location from, Location to, double durationInSec, Location edgeStart, Location finalDest, long edgeId){
        this.from = from;
        this.to = to;
        this.durationInSec = durationInSec;
        this.edgeStart = edgeStart;
        this.finalDest = finalDest;
        this.edgeId = edgeId;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public double getDurationInSec() {
        return durationInSec;
    }

    public void setDurationInSec(double durationInSec) {
        this.durationInSec = durationInSec;
    }

    public Location getEdgeStart() {
        return edgeStart;
    }

    public void setEdgeStart(Location edgeStart) {
        this.edgeStart = edgeStart;
    }

    public Location getFinalDest() {
        return finalDest;
    }

    public void setFinalDest(Location finalDest) {
        this.finalDest = finalDest;
    }

    public long getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(long edgeId) {
        this.edgeId = edgeId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}

