package model.frontend;

import java.io.Serializable;

public class SimulationStatus implements Serializable {

    int currentTick;
    boolean simulationIsDone;

    public SimulationStatus() {
    }

    public SimulationStatus(int currentTick, boolean simulationIsDone) {
        this.currentTick = currentTick;
        this.simulationIsDone = simulationIsDone;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public void setCurrentTick(int currentTick) {
        this.currentTick = currentTick;
    }


    public boolean isSimulationIsDone() {
        return simulationIsDone;
    }

    public void setSimulationIsDone(boolean simulationIsDone) {
        this.simulationIsDone = simulationIsDone;
    }
}
