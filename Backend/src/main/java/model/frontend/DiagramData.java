package model.frontend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiagramData implements Serializable {

    private static final long serialVersionUID = -7469958584014760053L;
    private String simulationId;
    private String KPIName;
    private List<Double> xCoordinates;
    private List<Double> yCoordinates;

    public DiagramData(){}

    public DiagramData(String KPIName) {
        this.KPIName = KPIName;
        this.xCoordinates = new ArrayList<>();
        this.yCoordinates = new ArrayList<>();
    }

    public String getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(String simulationId) {
        this.simulationId = simulationId;
    }

    public String getKPIName() {
        return KPIName;
    }

    public void setKPIName(String KPIName) {
        this.KPIName = KPIName;
    }

    public List<Double> getxCoordinates() {
        return xCoordinates;
    }

    public void setxCoordinates(List<Double> xCoordinates) {
        this.xCoordinates = xCoordinates;
    }

    public List<Double> getyCoordinates() {
        return yCoordinates;
    }

    public void setyCoordinates(List<Double> yCoordinates) {
        this.yCoordinates = yCoordinates;
    }


}
