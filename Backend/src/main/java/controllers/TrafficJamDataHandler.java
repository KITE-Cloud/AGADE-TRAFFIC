package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TrafficJamDataHandler {

    private List<Long> synchronizedList;

    private static TrafficJamDataHandler ourInstance = new TrafficJamDataHandler();

    public static TrafficJamDataHandler getInstance() {
        if (ourInstance == null)
            ourInstance = new TrafficJamDataHandler();
        return ourInstance;
    }

    public static void resetInstance() {
        ourInstance = null;
    }


    private TrafficJamDataHandler() {
        this.synchronizedList = Collections.synchronizedList(new ArrayList());
    }


    public void registerRoadInfo(Long id) {
        this.synchronizedList.add(id);
    }

    public HashMap<Long, Integer> getRoadInformation() {
        HashMap<Long, Integer> map = new HashMap();
        for (Long id : synchronizedList) {
            if (!map.containsKey(id)) {
                map.put(id,Collections.frequency(synchronizedList, id));
            }
        }
        resetInstance();
        return map;

    }

}
