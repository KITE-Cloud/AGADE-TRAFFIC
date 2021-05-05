package controllers.journeyPlanners;

import jadexMAS.agents.TrafficParticipantBDI;
import model.frontend.StartEnd;
import model.journey.Journey;
import rmi.model.Vehicle;

import java.util.List;

public interface IJourneyPlanner {

    public Vehicle selectVehicle(StartEnd trip, Vehicle currentVehicle);

    public Journey generateJourneys(TrafficParticipantBDI agent);

    public int getLengthOfStayInTicksPerDestination();

    public List<String> getActiveRules();

}
