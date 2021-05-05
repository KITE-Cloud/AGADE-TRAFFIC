package controllers.converter;

import model.RouteDetail;
import model.frontend.FrontendAgent;
import model.frontend.LocationFrontend;
import model.journey.Journey;
import rmi.model.AgentMovement;
import rmi.model.Location;

import java.util.ArrayList;

public class RoutingInformationToFrontendAgentConverter {

    public static FrontendAgent routingInformationFromBackendToFrontendAgent(rmi.model.RoutingInformation routingInformation, int birthTick, int id, Location origin, Location destination, String personaType, Journey journey) {

        ArrayList<LocationFrontend> locationList = new ArrayList<>();

        ArrayList<RouteDetail> routeDetails = new ArrayList<>();

        ArrayList<Integer> durationList = new ArrayList<Integer>();

        ArrayList<AgentMovement> movements = routingInformation.getMovements();
        for (rmi.model.AgentMovement agentMovement : movements) {
            locationList.add(new LocationFrontend(agentMovement.getFrom(), agentMovement.getVehicle()));
          //  locationList.add(LocationConverter.LocationFromRoutingToFrontend(agentMovement.getTo()));
            double speed = agentMovement.getDurationInSec()*1000; //sec to millisec
            durationList.add((int) speed);

            routeDetails.add(new RouteDetail(agentMovement.getEdgeStart(), agentMovement.getFinalDest(), agentMovement.getEdgeId()));
        }

        locationList.add(new LocationFrontend(movements.get(movements.size()-1).getTo(), movements.get(movements.size()-1).getVehicle()));
        FrontendAgent frontendAgent = new FrontendAgent();
        frontendAgent.setRouteCoordinates(locationList);
        frontendAgent.setRouteDetails(routeDetails);
        frontendAgent.setVelocities(durationList);
        frontendAgent.setVehicle(routingInformation.getVehicle());
        frontendAgent.setBirthTick(birthTick);
        frontendAgent.setId(id);
        frontendAgent.setOrigin(origin);
        frontendAgent.setDestination(destination);
        frontendAgent.setRouteHasChanged(routingInformation.getRouteHasChanged());
        frontendAgent.setPersonaProfile(personaType);
        frontendAgent.setJourney(journey);

        int totalTravelTime= 0;
        for(Integer time : durationList){
            totalTravelTime = totalTravelTime+time;
        }
        frontendAgent.setTotalTravelTime(totalTravelTime/1000);

        return frontendAgent;

    }
}
