package jadexMAS.agents;

import rmi.model.Location;
import rmi.model.Vehicle;

public interface ITrafficParticipant extends IAgent {

    void setOrigin(Location location);
    Location getOrigin();

    void setDestination(Location location);
    Location getDestination();

    void setCurrentLocation(Location location);
    Location getCurrentLocation();

    void setArrivedAtDestination(boolean bool);

    boolean getArrivedAtDestination();

    int getBirthTick();

    void setBirthTick(int birthTick);

    int getId();

    void setId(int id);

    Vehicle getCurrentVehicle();

    void setCurrentVehicle(Vehicle currentVehicle);

    float getTravelDistance();

    void setTravelDistance(float travelDistance);

}
