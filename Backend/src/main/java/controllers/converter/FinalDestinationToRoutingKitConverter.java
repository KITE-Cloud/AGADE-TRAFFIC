package controllers.converter;



import rmi.model.FinalDestination;

import java.util.ArrayList;
import java.util.List;

public class FinalDestinationToRoutingKitConverter {

    public static List<FinalDestination> convertFinalDestinationList(List<FinalDestination> destinations){

        List<rmi.model.FinalDestination> destinationListRmiFormat = new ArrayList<>();

        destinations.forEach(destination -> {
            rmi.model.FinalDestination destinationRmiFormat = new rmi.model.FinalDestination();

            destinationRmiFormat.setCapacity(destination.getCapacity());
            destinationRmiFormat.setCurrentlyWaiting(destination.getCurrentlyWaiting());
            destinationRmiFormat.setName(destination.getName());
            destinationRmiFormat.setLocation(destination.getLocation());
            destinationRmiFormat.setAreaDiameterForParking(destination.getAreaDiameterForParking());

            destinationListRmiFormat.add(destinationRmiFormat);
        });

        return destinationListRmiFormat;
    }

}
