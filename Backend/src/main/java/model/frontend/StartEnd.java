package model.frontend;

import model.DTTBean;
import rmi.model.Location;

import java.io.Serializable;
import java.util.ArrayList;

public class StartEnd implements Serializable {
    Location start;
    Location end;

    int desiredArrivalTick;
    int numberOfCars;
    int numberOfCarsPoissonAC;
    int numberOfCarsPoisson;
    int numberOfCarsDTT;

    ArrayList<DTTBean> desiredArrivalTickList;


    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getEnd() {
        return end;
    }

    public void setEnd(Location end) {
        this.end = end;
    }

    public int getNumberOfCars() {
        return numberOfCars;
    }

    public void setNumberOfCars(int numberOfCars) {
        this.numberOfCars = numberOfCars;
    }

    public int getNumberOfCarsPoisson() {
        return numberOfCarsPoisson;
    }

    public void setNumberOfCarsPoisson(int numberOfCarsPoisson) {
        this.numberOfCarsPoisson = numberOfCarsPoisson;
    }

    public int getNumberOfCarsDTT() {
        return numberOfCarsDTT;
    }

    public void setNumberOfCarsDTT(int numberOfCarsDTT) {
        this.numberOfCarsDTT = numberOfCarsDTT;
    }

    public int getDesiredArrivalTick() {
        return desiredArrivalTick;
    }

    public void setDesiredArrivalTick(int desiredArrivalTick) {
        this.desiredArrivalTick = desiredArrivalTick;
    }

    public ArrayList<DTTBean> getDesiredArrivalTickList() {
        return desiredArrivalTickList;
    }

    public void setDesiredArrivalTickList(ArrayList<DTTBean> desiredArrivalTickList) {
        this.desiredArrivalTickList = desiredArrivalTickList;
    }

    public int getNumberOfCarsPoissonAC() {
        return numberOfCarsPoissonAC;
    }

    public void setNumberOfCarsPoissonAC(int numberOfCarsPoissonAC) {
        this.numberOfCarsPoissonAC = numberOfCarsPoissonAC;
    }
}
