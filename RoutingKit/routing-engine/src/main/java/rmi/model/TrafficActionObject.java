package rmi.model;

import java.io.Serializable;
import java.util.List;

public class TrafficActionObject implements Serializable {
    private static final long serialVersionUID = 6L;
    private String type;
    private boolean block;
    private int maxspeed;
    private List<Location> locations;

    public TrafficActionObject() {
    }

    public TrafficActionObject(String type, boolean block, int maxspeed, List<Location> locations) {
        this.type = type;
        this.block = block;
        this.maxspeed = maxspeed;
        this.locations = locations;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public int getMaxspeed() {
        return maxspeed;
    }

    public void setMaxspeed(int maxspeed) {
        this.maxspeed = maxspeed;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}

