package model;

import java.io.Serializable;

public class DTTBean implements Serializable {
    private static final long serialVersionUID = 2926854527371789462L;

    private int desiredArrivalTick;
    private double share;


    public int getDesiredArrivalTick() {
        return desiredArrivalTick;
    }

    public void setDesiredArrivalTick(int desiredArrivalTick) {
        this.desiredArrivalTick = desiredArrivalTick;
    }

    public double getShare() {
        return share;
    }

    public void setShare(double share) {
        this.share = share;
    }
}
