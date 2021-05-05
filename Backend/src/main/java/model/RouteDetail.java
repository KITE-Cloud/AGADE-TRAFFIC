package model;

import rmi.model.Location;

import java.io.Serializable;

public class RouteDetail implements Serializable {

    private static final long serialVersionUID = -7140983398347463411L;

    Location edgeStart, edgeEnd;
    Long edgeId;

    public RouteDetail() {
    }

    public RouteDetail(Location edgeStart, Location edgeEnd, Long edgeId) {
        this.edgeStart = edgeStart;
        this.edgeEnd = edgeEnd;
        this.edgeId = edgeId;
    }

    public Location getEdgeStart() {
        return edgeStart;
    }

    public void setEdgeStart(Location edgeStart) {
        this.edgeStart = edgeStart;
    }

    public Location getEdgeEnd() {
        return edgeEnd;
    }

    public void setEdgeEnd(Location edgeEnd) {
        this.edgeEnd = edgeEnd;
    }

    public Long getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(Long edgeId) {
        this.edgeId = edgeId;
    }
}
