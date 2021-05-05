package rmi.model;

import java.io.Serializable;

public class DumpDestination extends Destination implements Serializable {
    private static final long serialVersionUID = -8326058302329913562L;

    public DumpDestination() {
        this.setCapacity(Integer.MAX_VALUE);
        this.setCurrentlyWaiting(0);
    }
}
