package routingAndCostEvaluationProcedure;

public class WorldCalculator {

    public final static int SECONDS_MULTIPLIER = 60;

    /**
     * Calculates the distance between two coordinates represented through the 2 lat and long parameters
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static float distFrom(double lat1, double lng1, double lat2,
                                 double lng2) {
        double earthRadius = 6371000; // meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (float) (earthRadius * c);
    }

    /**
     * Currently the Timecalculation on the road is only available via a linear calculation.
     * This is the place to implement bpr calculation.
     * @author Nico Loewe
     * @param timeForRoad
     * @param trafficUnitValue
     * @param carsOnThisRoad
     * @return
     */
    public static double calculateTimeForRoad(String routingPreference, double timeForRoad, double trafficUnitValue, long carsOnThisRoad, long hateFactorCarsOnSameRoad) {
        switch (routingPreference){
            case "AVOID_MAINSTREAM":
                return avoidMainstreamFunction(timeForRoad, trafficUnitValue, carsOnThisRoad, hateFactorCarsOnSameRoad);
//            case "ECO":
//                return ecoFunction();
            default:
                return linearFunction(timeForRoad, trafficUnitValue, carsOnThisRoad);
        }
    }

    private static double linearFunction(double timeForRoad, double trafficUnitValue, long carsOnThisRoad) {
        return (timeForRoad * SECONDS_MULTIPLIER) + (trafficUnitValue * carsOnThisRoad);
    }

    private static double avoidMainstreamFunction(double timeForRoad, double trafficUnitValue, long carsOnThisRoad, long hateFactorCarsOnSameRoad) {
        //int hate_factor = 100;
        return (timeForRoad * SECONDS_MULTIPLIER) + (trafficUnitValue * carsOnThisRoad * hateFactorCarsOnSameRoad);
    }

    private static double ecoFunction(){
        // TODO: Calculation and road restriction
        return 10;
    }


    /**
     * Calculatates the timeamount for one car driving the provided distance with the provided maxspeed
     * @param meters
     * @param maxspeed
     * @return
     */
    public static double calculateTimeForOneCar(double meters, double maxspeed) {
        return meters / (maxspeed * 1000 / 360);
    }
}
