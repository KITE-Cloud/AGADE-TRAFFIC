package controllers.journeyPlanners;

import jadexMAS.agents.TrafficParticipantBDI;
import model.frontend.StartEnd;
import model.journey.Journey;
import rmi.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class DefaultJourneyPlanner implements IJourneyPlanner {

    private List<String> activeRules;

    public DefaultJourneyPlanner() {
        this.activeRules = new ArrayList<>();
    }

    @Override
    public Journey generateJourneys(TrafficParticipantBDI agent) {
        Journey journey = new Journey();
        journey.setTrips(agent.getTrips());

        List<Vehicle> transportationModes = new ArrayList<>();
        for (StartEnd trip : journey.getTrips()) {
            Vehicle vehicle = selectVehicle(trip, Vehicle.None);
            transportationModes.add(vehicle);
        }
        journey.setTransportationMode(transportationModes);
        return journey;
    }

    @Override
    public Vehicle selectVehicle(StartEnd trip, Vehicle currentVehicle) {
        return Vehicle.CAR;
    }

    @Override
    public int getLengthOfStayInTicksPerDestination() {
        return 0;
    }

    @Override
    public List<String> getActiveRules() {
        return activeRules;
    }

}
