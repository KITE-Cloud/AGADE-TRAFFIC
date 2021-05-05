package jadexMAS.plans;

import jadex.bdiv3.annotation.*;

@Plan
public class DriveToPlanOld {/*

    private final TrafficParticipantOldBDI executingAgent;

    public DriveToPlanOld(TrafficParticipantOldBDI executingAgent) {
        this.executingAgent = executingAgent;
        System.out.println("Konstruktor-Auruf des Plans");
        System.out.println("AGENT: " + executingAgent);
    }

    @PlanBody
    public void move() {
        // Do something
        System.out.println("Agent führt Plan aus");

        System.out.println("Alte Location= Lat: " + this.executingAgent.getCurrentLocation().getLattitude() + " Long: " + this.executingAgent.getCurrentLocation().getLongitude());
        System.out.println("Alte Location=: " + this.executingAgent.getCurrentLocation());

        //Location newLocation = new Location(this.executingAgent.getCurrentLocation().getLattitude() + 1,this.executingAgent.getCurrentLocation().getLongitude() + 1);
        //this.executingAgent.setCurrentLocation(newLocation);

        Location agentCurrentLocation = executingAgent.getCurrentLocation();
        agentCurrentLocation.setLattitude(agentCurrentLocation.getLattitude() + 1);
        agentCurrentLocation.setLongitude(agentCurrentLocation.getLongitude() + 1);

        System.out.println("Neue Location= Lat: " + this.executingAgent.getCurrentLocation().getLattitude() + " Long: " + this.executingAgent.getCurrentLocation().getLongitude());
        System.out.println("Neue Location=: " + this.executingAgent.getCurrentLocation());

        if ((executingAgent.getCurrentLocation().getLattitude() == executingAgent.getDestination().getLattitude()) && (executingAgent.getCurrentLocation().getLongitude() == executingAgent.getDestination().getLongitude())) {
            executingAgent.setArrivedAtDestination(true);
        }
    }

    @PlanPassed
    public void passed() {
        System.out.println("Plan finished successfully.");
    }

    @PlanAborted
    public void aborted() {
        System.out.println("Plan aborted.");
    }

    @PlanFailed
    public void failed(Exception e) {
        System.out.println("Plan failed: " + e);
    }*/
}
