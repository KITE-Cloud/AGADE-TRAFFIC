package utility;

import controllers.Client;
import model.AgentStartBean;
import model.DTTBean;
import model.frontend.StartEnd;
import rmi.RMIClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MathDistributionUtil {

    public static final String TYPE_POISSON = "poisson", TYPE_TARGET_TIME = "target time", TYPE_AC = "activity";


    /**
     * Determines when jadex.agents start driving, considering a initialization type (Desired target Time | Possion distribution)
     */
    public static HashMap<Integer, List<AgentStartBean>> createTimeSeries(StartEnd startEnd, int tickFrom, int tickTo, String type) {
        int tickLength = Client.getInstance().getTICKLENGTH_IN_SECONDS();

        double timeInSecondsForPath = RMIClient.getInstance().calculatefastestWayToDestination(startEnd.getStart(), startEnd.getEnd());
        double timeInMinutes = timeInSecondsForPath / 60;
        int minutesRounded = (int) Math.round(timeInMinutes);

        HashMap<Integer, List<AgentStartBean>> times = new HashMap<>();
        int numCars;
        int numCarsInMap;
        switch (type) {
            // creates a number of cars using a poisson distribution
            case TYPE_POISSON:
                numCars = startEnd.getNumberOfCarsPoisson();
                numCars = (tickTo - tickFrom) * tickLength / 60 / 60 * numCars;

                numCarsInMap = 0;
                while (numCarsInMap < numCars) {
                    int poisson = getPoissonRandom((tickTo - tickFrom) / 2. + tickFrom);
                    if (poisson >= tickFrom && poisson <= tickTo) {
                        if (!times.containsKey(poisson)) {
                            times.put(poisson, new ArrayList());
                        }
                        times.get(poisson).add(new AgentStartBean(poisson, poisson, minutesRounded + poisson, minutesRounded + poisson));
                    }
                    numCarsInMap = getNumberOfCarsInMap(times);
                }
                return times;

            // calculating the time serious by considering the DTT value of each startEnd (From Location -> To Location)
            case TYPE_TARGET_TIME:
                Random random = new Random();
                List<DTTBean> desiredArrivalTickList = startEnd.getDesiredArrivalTickList();
                HashMap<Integer, Integer> carsPerTick = new HashMap<>();
                double shareCum;

                // calculating the cars per tick.
                // the result is saved in the Map carsPerTick
                for (int i = 0; i < startEnd.getNumberOfCarsDTT(); i++) {
                    shareCum = 0d;
                    int randomInt = random.nextInt(100);
                    if (desiredArrivalTickList == null) {
                        System.out.println("");
                    }
                    for (DTTBean dttBean : desiredArrivalTickList) {

                        shareCum += dttBean.getShare();
                        if (randomInt <= shareCum) {
                            carsPerTick.put(dttBean.getDesiredArrivalTick(),
                                    carsPerTick.containsKey(dttBean.getDesiredArrivalTick()) ?
                                            (carsPerTick.get(dttBean.getDesiredArrivalTick()) + 1) :
                                            (1));
                            break;
                        }
                    }
                }

                // the map carsPerTick is now used to create the timeseries
                for (Integer desiredArrivalTick : carsPerTick.keySet()) {

                    if (desiredArrivalTick != -1) {

                        int randomFactor = 1; // ==> 10 minuten
                        int targetTime = tickFrom + desiredArrivalTick;
                        int intendedStartTime = targetTime - minutesRounded;


                        int borderBottom = targetTime - randomFactor - minutesRounded < tickFrom ? tickFrom : targetTime - randomFactor - minutesRounded;
                        int borderTop = targetTime + randomFactor - minutesRounded > tickTo ? tickTo : targetTime + randomFactor - minutesRounded;
                        borderTop = borderTop <= borderBottom ? borderBottom + 1 : borderTop;
                        if (borderBottom > borderTop) borderBottom = borderTop;

                        int index = 0;
                        while (index++ < carsPerTick.get(desiredArrivalTick)) {
                            int startTime = random.nextInt(borderTop - borderBottom) + borderBottom;

                            if (!times.containsKey(startTime)) {
                                times.put(startTime, new ArrayList());
                            }
                            times.get(startTime).add(new AgentStartBean(startTime, intendedStartTime, startTime + minutesRounded, desiredArrivalTick));
                        }

                    }
                }
                return times;
            case TYPE_AC:
                numCars = startEnd.getNumberOfCarsPoissonAC();
                numCars = (tickTo - tickFrom) * tickLength / 60 / 60 * numCars;
                numCarsInMap = 0;
                while (numCarsInMap < numCars) {
                    int poisson = getPoissonRandom((tickTo - tickFrom) / 2. + tickFrom);
                    if (poisson >= tickFrom && poisson <= tickTo) {
                        if (!times.containsKey(poisson)) {
                            times.put(poisson, new ArrayList());
                        }
                        times.get(poisson).add(new AgentStartBean(poisson, poisson, minutesRounded + poisson, minutesRounded + poisson));
                    }
                    numCarsInMap = getNumberOfCarsInMap(times);
                }
                return times;
            default:
                return createTimeSeries(startEnd, tickFrom, tickTo, TYPE_POISSON);


        }

    }

    private static int getPoissonRandom(double mean) {
        Random r = new Random();
        double L = Math.exp(-mean);
        int k = 0;
        double p = 1.0;
        do {
            p = p * r.nextDouble();
            k++;
        } while (p > L);
        return k - 1;
    }


    public static int getNumberOfCarsInMap(HashMap<Integer, List<AgentStartBean>> times) {
        if (times.size() > 0) {
            int sum = 0;
            for (List list : times.values()) {
                sum = sum + list.size();
            }
            return sum;
        } else {
            return 0;
        }
    }

}
