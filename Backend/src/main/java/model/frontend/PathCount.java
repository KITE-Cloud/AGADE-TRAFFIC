package model.frontend;

import rmi.model.Location;

import java.io.Serializable;

public class PathCount implements Serializable {

    private static final long serialVersionUID = 6012407857057872344L;

    private Location from, to;
    private int count;

    public PathCount() {
    }

    public PathCount(Location from, Location to) {
        this.from = from;
        this.to = to;
        this.count = 1;
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

    public int getCount() {
        return count;
    }

    public PathCount setCount(int count) {
        this.count = count;
        return this;
    }
}