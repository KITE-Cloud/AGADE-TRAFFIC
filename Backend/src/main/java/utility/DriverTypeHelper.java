package utility;

import model.AgentStartBean;
import model.frontend.Configurations;
import model.frontend.StartEnd;
import model.ontologyPojos.PersonaProfile;
import rmi.model.Location;

import java.util.*;

public class DriverTypeHelper {

    public static PersonaProfile determinePersonaProfile(Configurations configurations){

            Map<Double, PersonaProfile> personaDistribution = new TreeMap<>();
            List<PersonaProfile> personaProfiles = configurations.getPersonaProfiles();
            Collections.sort(personaProfiles, (o1, o2) -> Integer.valueOf(o1.getPercentage()).compareTo(o2.getPercentage()));
            double sum = 0;
            for ( PersonaProfile persona : personaProfiles){
                personaDistribution.put(persona.getPercentage()+sum, persona);
                sum += persona.getPercentage();
            }

            double dice = Math.random() * 100;
            for(Map.Entry<Double, PersonaProfile> entry: personaDistribution.entrySet()){
                if (dice <= entry.getKey()){
                    return entry.getValue();
                }
            }
        // usually not necessary
        return personaProfiles.get(0);
    }


    public static List<Location> generateLocationList(Configurations configurations, boolean isStart) {
        List<Location> locationList = new ArrayList<>();
        List<StartEnd> startEndArrayList = configurations.getDestinationMatrix();
        for (StartEnd startEnd : startEndArrayList) {
            int numberOfCars = startEnd.getNumberOfCars();
            for(int i = 0; i < numberOfCars; i++){
                if (isStart){
                    locationList.add(startEnd.getStart());
                }
                else{
                    locationList.add(startEnd.getEnd());
                }
            }
        }
        return locationList;
    }

    /**
     * Method that returns a Map which contains all the Ticks in which jadex.agents start.
     * @param configurations
     * @param currentTick
     * @return
     */
    public static Map<StartEnd, Map<Integer, List<AgentStartBean>>> generateAgentMomentsPerRoute(Configurations configurations, int currentTick){
        Map<StartEnd, Map<Integer, List<AgentStartBean>>> agentMomentsPerRoute = new HashMap<>();
        int tickTo = currentTick  + 60; //tickTo = currentTick + ticks for one hour
        for(StartEnd startEnd: configurations.getDestinationMatrix()){
            HashMap<Integer, List<AgentStartBean>> times = new HashMap<>();

            HashMap<Integer, List<AgentStartBean>> timeSeriesDTT = new HashMap<>();
            HashMap<Integer, List<AgentStartBean>> timeSeriesPoisson = new HashMap<>();
            if(startEnd.getNumberOfCarsPoisson()>0) {
                timeSeriesPoisson = MathDistributionUtil.createTimeSeries(startEnd, currentTick, tickTo, MathDistributionUtil.TYPE_POISSON);
            }

            if(startEnd.getNumberOfCarsDTT()>0) {
                timeSeriesDTT = MathDistributionUtil.createTimeSeries(startEnd, currentTick, tickTo, MathDistributionUtil.TYPE_TARGET_TIME);
            }

            for (Integer startTickPoisson : timeSeriesPoisson.keySet()) {
                if (timeSeriesDTT.containsKey(startTickPoisson)) {
                    timeSeriesDTT.get(startTickPoisson).addAll(timeSeriesPoisson.get(startTickPoisson));
                } else {
                    timeSeriesDTT.put(startTickPoisson, timeSeriesPoisson.get(startTickPoisson));
                }
            }

            times = timeSeriesDTT;

            agentMomentsPerRoute.put(startEnd, times);
        }
        return agentMomentsPerRoute;
    }

    /**
     * Method that returns a Map which contains all the Ticks in which jadex.agents start.
     *
     * @param configurations
     * @param currentTick
     * @return
     */
    public static Map<StartEnd, Map<Integer, List<AgentStartBean>>> generateAgentMomentsPerStartLocation(Configurations configurations, int currentTick) {
        //EndLocation is always DUMP location. Only Startlocation is relevant
        Map<StartEnd, Map<Integer, List<AgentStartBean>>> agentMomentsPerRoute = new HashMap<>();
        int tickTo = currentTick + 60; //tickTo = currentTick + ticks for one hour
        for (StartEnd startEnd : configurations.getDestinationMatrix()) {
            HashMap<Integer, List<AgentStartBean>> timeSeriesPoissonAC = new HashMap<>();
            if (startEnd.getNumberOfCarsPoissonAC() > 0) {
                timeSeriesPoissonAC = MathDistributionUtil.createTimeSeries(startEnd, currentTick, tickTo, MathDistributionUtil.TYPE_AC);
                agentMomentsPerRoute.put(startEnd, timeSeriesPoissonAC);
            }
        }
        return agentMomentsPerRoute;
    }


}
