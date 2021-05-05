package utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathUtil {

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

    public static Map<String, Double> minMaxDoubleInList(List<Double> list) {
        Map<String, Double> minMax = new HashMap<>();
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (double d : list) {
            if (d < min)
                min = d;
            if (d > max)
                max = d;
        }

        minMax.put("min", min);
        minMax.put("max", max);
        return minMax;
    }


    public static Double sumOfList(List<Double> list) {
        double sum = 0;
        for (double d : list) {
            sum = sum + d;
        }
        return sum;
    }

    public static Double minMaxScaling(double val, double minVal, double maxVal) {
        if (maxVal == val)
            return 1d;
        else {
            return (val - minVal) / (maxVal - minVal); // MinMax Scaling
        }

    }


}
