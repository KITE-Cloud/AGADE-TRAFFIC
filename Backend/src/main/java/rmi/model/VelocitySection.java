package rmi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VelocitySection implements Serializable {

    private static final long serialVersionUID = 3L;
    private List<Location> nodesInSection;
    private double velocity;
    private String name;

    public VelocitySection() {
        this.nodesInSection = new ArrayList<>();
    }

    public VelocitySection(List<Location> nodesInSection, double velocity, String name) {
        this.nodesInSection = nodesInSection;
        this.velocity = velocity;
        this.name = name;
    }

    public List<Location> getNodesInSection() {
        return nodesInSection;
    }

    public void setNodesInSection(List<Location> nodesInSection) {
        this.nodesInSection = nodesInSection;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
