package poisson;

import org.junit.Test;


import java.util.*;

public class Poisson {
    @Test
    public void poissonTest() {
        System.out.println(createTimeSeries(5, 0, 20));
    }

    private List<Integer> createTimeSeries(int numCars, int tickFrom, int tickTo) {
        List<Integer> times = new ArrayList<>();
        while (times.size()<numCars){
            int poisson = getPoissonRandom((tickTo-tickFrom)/2.+tickFrom);
            if (poisson>=tickFrom && poisson<=tickTo){
                times.add(poisson);
            }
        }
        Collections.sort(times);
        return times;
    }

    private int getPoissonRandom(double mean) {
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

}
