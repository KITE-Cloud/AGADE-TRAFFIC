package model;

import rmi.model.Vehicle;

import java.io.Serializable;

public class ModeUtility implements Serializable {

    private Vehicle mode;
    private int flexibility;
    private int time;
    private int reliability;
    private int privacy;
    private int safety;
    private int environmentalImpact;
    private int monetaryCosts;
    private int convenience;

    public ModeUtility(Vehicle mode, int flexibility, int time, int reliability, int privacy, int safety, int environmentalImpact, int monetaryCosts, int convenience) {
        this.mode = mode;
        this.flexibility = flexibility;
        this.time = time;
        this.reliability = reliability;
        this.privacy = privacy;
        this.safety = safety;
        this.environmentalImpact = environmentalImpact;
        this.monetaryCosts = monetaryCosts;
        this.convenience = convenience;
    }

    public Vehicle getMode() {
        return mode;
    }

    public int getFlexibility() {
        return flexibility;
    }

    public int getTime() {
        return time;
    }

    public int getReliability() {
        return reliability;
    }

    public int getPrivacy() {
        return privacy;
    }

    public int getSafety() {
        return safety;
    }

    public int getEnvironmentalImpact() {
        return environmentalImpact;
    }

    public int getMonetaryCosts() {
        return monetaryCosts;
    }

    public int getConvenience() {
        return convenience;
    }
}
