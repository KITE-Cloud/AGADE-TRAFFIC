package model.statisticalData;

import model.frontend.DiagramData;

import java.io.Serializable;

public class Statistics implements Serializable {

    private DiagramData numberOfMovingAgentsPerTick;
    private DiagramData createdAgentsPerTick;

    public DiagramData getNumberOfMovingAgentsPerTick() {
        return numberOfMovingAgentsPerTick;
    }

    public void setNumberOfMovingAgentsPerTick(DiagramData numberOfMovingAgentsPerTick) {
        this.numberOfMovingAgentsPerTick = numberOfMovingAgentsPerTick;
    }

    public DiagramData getCreatedAgentsPerTick() {
        return createdAgentsPerTick;
    }

    public void setCreatedAgentsPerTick(DiagramData createdAgentsPerTick) {
        this.createdAgentsPerTick = createdAgentsPerTick;
    }
}
