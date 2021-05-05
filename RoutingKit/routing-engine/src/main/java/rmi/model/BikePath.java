package rmi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BikePath implements Serializable {

    private static final long serialVersionUID = -2901849738805039915L;
    private List<Location> nodesInSection;
    private String name;

    public BikePath() {
        this.nodesInSection = new ArrayList<>();
    }

    public BikePath(List<Location> nodesInSection, String name) {
        this.nodesInSection = nodesInSection;
        this.name = name;
    }

    public List<Location> getNodesInSection() {
        return nodesInSection;
    }

    public void setNodesInSection(List<Location> nodesInSection) {
        this.nodesInSection = nodesInSection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
