package model;

public class AgentStartBean {

    private int expectedArrivalTick;
    private int actualStartTick;
    private int intendedStartTick;
    private int desiredArrivalTick;

    public AgentStartBean() {
    }

    public AgentStartBean(int actualStartTick, int intendedStartTick, int expectedArrivalTick, int desiredArrivalTick) {
        this.actualStartTick = actualStartTick;
        this.intendedStartTick = intendedStartTick;
        this.desiredArrivalTick = desiredArrivalTick;
        this.expectedArrivalTick = expectedArrivalTick;
    }

    public int getActualStartTick() {
        return actualStartTick;
    }

    public void setActualStartTick(int actualStartTick) {
        this.actualStartTick = actualStartTick;
    }

    public int getIntendedStartTick() {
        return intendedStartTick;
    }

    public void setIntendedStartTick(int intendedStartTick) {
        this.intendedStartTick = intendedStartTick;
    }

    public int getDesiredArrivalTick() {
        return desiredArrivalTick;
    }

    public void setDesiredArrivalTick(int desiredArrivalTick) {
        this.desiredArrivalTick = desiredArrivalTick;
    }

    public int getExpectedArrivalTick() {
        return expectedArrivalTick;
    }

    public void setExpectedArrivalTick(int expectedArrivalTick) {
        this.expectedArrivalTick = expectedArrivalTick;
    }
}
