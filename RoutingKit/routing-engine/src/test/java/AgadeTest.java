import rmi.model.Location;
import rmi.model.RoutingInformation;
import rmi.model.RoutingPreference;
import org.junit.Test;
import rmi.model.Vehicle;

import java.util.HashMap;

/**
 * The type Agade test.
 */
public class AgadeTest {

    /**
     * Agade zugang test.
     */
  /*  @Test
    public void AgadeZugangTest(){

        RMIClient agadeInterface = RMIClient.getInstance();

        agadeInterface.ping();

        //Location start = new Location(50.29756, 8.8157114); // nodeId 86
        //Location destination = new Location(50.2947805, 8.8173104); // nodeId 131

        Location start = new Location(50.2967364 , 8.8171619); // waschgasse
        Location destination = new Location(50.2973672 , 8.8138353); // am hain


        RoutingInformation routingInformation = new RoutingInformation(Vehicle.CAR, start, destination, RoutingPreference.DEFAULT);

        System.out.println(routingInformation.getCurrentLocation().getLat() + " " + routingInformation.getCurrentLocation().getLon());
        System.out.println(routingInformation.getTargetLocation().getLat() + " " + routingInformation.getTargetLocation().getLon());
        //System.out.println();

        while((routingInformation.getTargetLocation().getLat() != routingInformation.getCurrentLocation().getLat()) &
              (routingInformation.getTargetLocation().getLon() != routingInformation.getCurrentLocation().getLon())) {

            agadeInterface.nextTick(10, null, null);
            routingInformation = agadeInterface.routeAgent(routingInformation);

            System.out.println(routingInformation.getCurrentLocation().getLat() + " " + routingInformation.getCurrentLocation().getLon());
            System.out.println(routingInformation.getTargetLocation().getLat() + " " + routingInformation.getTargetLocation().getLon());
            System.out.println();
        }
    }*/
}
