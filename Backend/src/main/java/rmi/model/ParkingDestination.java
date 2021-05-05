package rmi.model;

import java.io.Serializable;

public class ParkingDestination extends Destination implements Serializable {

    private static final long serialVersionUID = -2709818735931879479L;

    private FinalDestination finalDestination;

    public FinalDestination getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(FinalDestination finalDestination) {
        this.finalDestination = finalDestination;
    }
}
