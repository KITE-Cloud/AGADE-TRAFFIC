package rmi.model;

import java.io.Serializable;

public class RelationshipInfoBean implements Serializable {
    private static final long serialVersionUID = -7752112457885943882L;

    private long relationshipId;
    private int relationshipListIndex;
    private double maxSpeed;
    private double distance;

    public RelationshipInfoBean() {
    }

    public RelationshipInfoBean(long relationshipId, double maxSpeed, double distance) {
        this.relationshipId = relationshipId;
        this.maxSpeed = maxSpeed;
        this.distance = distance;
    }

    public long getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(long relationshipId) {
        this.relationshipId = relationshipId;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getRelationshipListIndex() {
        return relationshipListIndex;
    }

    public void setRelationshipListIndex(int relationshipListIndex) {
        this.relationshipListIndex = relationshipListIndex;
    }
}
