package jadexMAS.goals;

import jadex.bdiv3.annotation.*;
import jadex.bdiv3.model.MProcessableElement;

@Goal(excludemode = MProcessableElement.ExcludeMode.Never)
public class DriveToGoalOld {
/*
    @GoalParameter
    Location destination;

    @GoalParameter
    Location currentLocation;

    @GoalParameter
    AbstractTrafficParticipantOWLAgent agent;

    @GoalResult
    Boolean destinationReached;

    public DriveToGoalOld(Location destination, Location currentLocation) {

        this.destination = destination;
        this.currentLocation = currentLocation;

    }

    public DriveToGoalOld(AbstractTrafficParticipantOWLAgent executingAgent) {

        this.agent = executingAgent;

    }

    @GoalMaintainCondition(beliefs = "arrivedAtDestination")
    protected boolean maintain() {
        System.out.println("MAINTAIN-ÜBERPRÜFUNG Destination: " + agent.getDestination().getLat() + "  " + agent.getDestination().getLon());
        System.out.println("MAINTAIN-ÜBERPRÜFUNG CurrentLocation: " + agent.getCurrentLocation().getLat() + "  " + agent.getCurrentLocation().getLon());
        System.out.println("MAINTAIN-ÜBERPRÜFUNG ArrivedAtDestination: " + agent.getArrivedAtDestination());
        //return ((agent.getCurrentLocation().getLattitude() == agent.getDestination().getLattitude()) && (agent.getCurrentLocation().getLongitude() == agent.getDestination().getLongitude()));
        return agent.getArrivedAtDestination();
    }

    @GoalTargetCondition(beliefs = "arrivedAtDestination")
    protected boolean target() {
        System.out.println("ÜBERPRÜFUNG TARGET: Destination: " + agent.getDestination().getLat() + "  " + agent.getDestination().getLon());
        System.out.println("ÜBERPRÜFUNG TARGET: CurrentLocation: " + agent.getCurrentLocation().getLat() + "  " + agent.getCurrentLocation().getLon());
        System.out.println("ÜBERPRÜFUNG TARGET: ArrivedAtDestination: " + agent.getArrivedAtDestination());
        //return ((agent.getCurrentLocation().getLattitude() == agent.getDestination().getLattitude()) && (agent.getCurrentLocation().getLongitude() == agent.getDestination().getLongitude()));
        return agent.getArrivedAtDestination();
    }
*/
}
