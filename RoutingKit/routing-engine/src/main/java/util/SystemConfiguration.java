package util;

import rmi.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemConfiguration {
    private static SystemConfiguration ourInstance = new SystemConfiguration();

    public static SystemConfiguration getInstance() {
        return ourInstance;
    }

    private SystemConfiguration() {
    }

    private DumpDestination dumpDestination;
    private List<FinalDestination> finalDestinations = new ArrayList<>();
    private List<Long> blockedStreetIds = new ArrayList<>();
    private Map<String, Integer> modifiedSpeedMap = new HashMap<>();
    private Map<String, ParkingDestination> parkingDestinations = new HashMap<>();
    private List<InternalDestination> internalDestinations = new ArrayList<>();

    public List<FinalDestination> getFinalDestinations() {
        return finalDestinations;
    }

    public void setFinalDestinations(List<FinalDestination> finalDestinations) {
        this.finalDestinations = finalDestinations;
    }

    public FinalDestination findDestination(Location location) {
        for (FinalDestination destination : finalDestinations) {
            if (location.getLocationName().equals(destination.getName()))
                return destination;
        }

        return null;
    }

    public Destination findDestination(Location location, RoutingInformation routingInfo) {
        for (InternalDestination internalDestination : internalDestinations) {
            if (internalDestination.getLocation().getLon() == location.getLon() && internalDestination.getLocation().getLat() == location.getLat()) {
                return internalDestination;
            }
        }

        if(location.getLocationName() != null)
            if(location.getLocationName().equals(SystemConfiguration.getInstance().getDumpDestination().getName())){

                return SystemConfiguration.getInstance().getDumpDestination();

            }
        if (routingInfo.getSelectedParkingDestination() != null) {
            if (location.equals(routingInfo.getSelectedParkingDestination().getLocation())) {
                return SystemConfiguration.getInstance().getParkingDestinations().get(routingInfo.getSelectedParkingDestination().getName());
            }
        } else {
            for (FinalDestination destination : finalDestinations) {
                if (location.getLocationName().equals(destination.getName()))
                    return destination;
            }
        }

        return null;
    }

    public List<Long> getBlockedStreetIds() {
        return blockedStreetIds;
    }

    public void setBlockedStreetIds(List<Long> blockedStreetIds) {
        this.blockedStreetIds = blockedStreetIds;
    }

    public Map<String, Integer> getModifiedSpeedMap() {
        return modifiedSpeedMap;
    }

    public void setModifiedSpeedMap(Map<String, Integer> modifiedSpeedMap) {
        this.modifiedSpeedMap = modifiedSpeedMap;
    }

    public Map<String, ParkingDestination> getParkingDestinations() {
        return parkingDestinations;
    }

    public void setParkingDestinations(Map<String, ParkingDestination> parkingDestinations) {
        this.parkingDestinations = parkingDestinations;
    }

    public void setDumpDestination(DumpDestination dumpDestination) {
        this.dumpDestination = dumpDestination;
    }

    public DumpDestination getDumpDestination() {
        return dumpDestination;
    }

    public List<InternalDestination> getInternalDestinations() {
        return internalDestinations;
    }

    public void setInternalDestinations(List<InternalDestination> internalDestinations) {
        this.internalDestinations = internalDestinations;
    }
}
