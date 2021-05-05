package model;

import rmi.model.Vehicle;

import java.io.Serializable;

public class ModeChangeCost implements Serializable {

    private Vehicle currentMode;
    private Vehicle newMode;
    private int changeCost;

    public ModeChangeCost(Vehicle currentMode, Vehicle newMode, int changeCost) {
        this.currentMode = currentMode;
        this.newMode = newMode;
        this.changeCost = changeCost;
    }

    public Vehicle getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(Vehicle currentMode) {
        this.currentMode = currentMode;
    }

    public Vehicle getNewMode() {
        return newMode;
    }

    public void setNewMode(Vehicle newMode) {
        this.newMode = newMode;
    }

    public int getChangeCost() {
        return changeCost;
    }

    public void setChangeCost(int changeCost) {
        this.changeCost = changeCost;
    }
}
