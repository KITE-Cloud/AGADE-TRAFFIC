package model.statisticalData;

import java.io.Serializable;

public class StatisticsTable implements Serializable {

    private static final long serialVersionUID = -5223394346140306610L;
    private String sim_id;
    private int phase;
    private int route_id;
    private String driver_type;
    private int num_agents;
    private float avg_time;

    public StatisticsTable() {
    }

    public StatisticsTable(String sim_id, int phase, int route_id, String driver_type, int num_agents, float avg_time) {
        this.sim_id = sim_id;
        this.phase = phase;
        this.route_id = route_id;
        this.driver_type = driver_type;
        this.num_agents = num_agents;
        this.avg_time = avg_time;
    }

    public String getSim_id() {
        return sim_id;
    }

    public int getRoute_id() {
        return route_id;
    }

    public int getPhase() {
        return phase;
    }

    public String getDriver_type() {
        return driver_type;
    }

    public int getNum_agents() {
        return num_agents;
    }

    public float getAvg_time() {
        return avg_time;
    }
}
