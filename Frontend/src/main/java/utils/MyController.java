package utils;

import wsdl.*;

import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class MyController {

    AGADETrafficServiceFacadeService serviceFacadeService;
    AGADETrafficServiceFacade servicePort;
    public SimulationStatus simulationStatus;
    private static MyController instance = null;

    private MyController() {
        this.serviceFacadeService = new AGADETrafficServiceFacadeService();
        this.servicePort = serviceFacadeService.getAGADETrafficServiceFacadePort();
        simulationStatus = new SimulationStatus();
        simulationStatus.setCurrentTick(0);
        simulationStatus.setSimulationIsDone(false);
    }

    public static MyController getInstance() {
        if (instance == null) {
            instance = new MyController();
        }
        return instance;
    }

    public void initialiseAgentsJSP(List<StartEnd> destinationMatrix, List<FinalDestination> destinationList, int simulationTime,
                                    boolean isNewSimulation, List<TrafficActionObject> trafficActionObjects, DumpDestination dumpDestination,
                                    String agentGeneratorType, List<PersonaProfile> personaProfiles, String populationJson, boolean fromPopulation) {
        Configurations configuration = new Configurations();
        configuration.setDumpDestination(dumpDestination);
        configuration.setAgentGeneratorType(agentGeneratorType);
        configuration.getTrafficActionObjects().addAll(trafficActionObjects);
        for (StartEnd startEnd : destinationMatrix) {
            configuration.getDestinationMatrix().add(startEnd);
        }

        configuration.setSimulationTime(simulationTime);

        configuration.getFinalDestinations().removeAll(configuration.getFinalDestinations());
        configuration.getFinalDestinations().addAll(destinationList);
        configuration.getPersonaProfiles().addAll(personaProfiles);
        configuration.setPopulationConfig(populationJson);
        configuration.setFromPopulation(fromPopulation);

        initialiseSimulation(configuration, isNewSimulation);

    }


    ///////////////////////////////////////////BACKEND////////////////////////////////////////////////////////////////

    public FrontendResultWrapper getSimulationResult() {
        return servicePort.getSimulationResult();
    }


    public SimulationStatus getSimulationStatus() {
        this.simulationStatus = servicePort.getSimulationStatus();
        return simulationStatus;
    }

    public void initialiseSimulation(Configurations configurations, boolean isNewSimulation) {
        String s = servicePort.initialiseSimulation(configurations, isNewSimulation);

    }

    public OntologyProps getOntologyProps() {
        return servicePort.getOntologyProps();
    }

    public Location findNearest(Location loc) {
        return servicePort.findNodeNearestToLocation(loc);
    }


    ////////////////////////////////////////BACKEND//////////////////////////////////////


    public List<StartEnd> transformJSONMap(Map map, List<FinalDestination> destinationsList) {
        List<StartEnd> list = new ArrayList();
        for (Object key : map.keySet()) {
            Map item = (Map) map.get(key);
            StartEnd startEnd = new StartEnd();

            startEnd.setStart(new Location());
            Map startLocation = (Map) item.get("start");
            String name = (String) startLocation.get("name");
            startEnd.getStart().setLocationName(name);
            startEnd.getStart().setLat(((Double) startLocation.get("lat")).doubleValue());
            startEnd.getStart().setLon(((Double) startLocation.get("lon")).doubleValue());
            startEnd.getStart().setPoiType(getPoiTypeFromName(name, destinationsList));

            startEnd.setEnd(new Location());
            Map endLocation = (Map) item.get("end");
            name = (String) endLocation.get("name");
            startEnd.getEnd().setLocationName(name);
            startEnd.getEnd().setLat(((Double) endLocation.get("lat")).doubleValue());
            startEnd.getEnd().setLon(((Double) endLocation.get("lon")).doubleValue());

            if (!name.equals("DUMP")) {
                startEnd.getEnd().setPoiType(getPoiTypeFromName(name, destinationsList));
            } else {
                startEnd.getEnd().setPoiType("Location");
            }


            int numberOfCarsPoisson = Integer.parseInt(item.get("numberOfCarsPoisson").toString());
            startEnd.setNumberOfCarsPoisson(numberOfCarsPoisson);
            int numberOfCarsDTT = Integer.parseInt(item.get("numberOfCarsDTT").toString());
            startEnd.setNumberOfCarsDTT(numberOfCarsDTT);
            int numberOfCarsPoissonAC = Integer.parseInt(item.get("numberOfCarsPoissonAC").toString());
            startEnd.setNumberOfCarsPoissonAC(numberOfCarsPoissonAC);

            list.add(startEnd);

        }
        return list;
    }

    public List<PersonaProfile> transformPersonaProfiles(Map map) {
        List<PersonaProfile> list = new ArrayList();
        List personaProfiles = (List) map.get("personaProfiles");

        for (Object personaElement : personaProfiles) {
            if (personaElement != null) {
                PersonaProfile currentPersona = new PersonaProfile();
                currentPersona.getAge().addAll((List<String>) ((Map) personaElement).get("age"));
                currentPersona.setDescription((String) ((Map) personaElement).get("description"));
                currentPersona.setEducation((String) ((Map) personaElement).get("education"));
                currentPersona.setGender((String) ((Map) personaElement).get("gender"));
                currentPersona.setId((String) ((Map) personaElement).get("id"));
                currentPersona.setMaritalStatus((String) ((Map) personaElement).get("maritalStatus"));
                currentPersona.setName((String) ((Map) personaElement).get("name"));
                currentPersona.setOccupation((String) ((Map) personaElement).get("occupation"));
                currentPersona.setPercentage((int) ((Map) personaElement).get("percentage"));
                currentPersona.setPic((String) ((Map) personaElement).get("pic"));

                PersonaMapProperties healthProperties = new PersonaMapProperties();
                List<String> keys = (List<String>) ((Map) ((Map) personaElement).get("healthProperties")).get("keys");
                List<Integer> values = (List<Integer>) ((Map) ((Map) personaElement).get("healthProperties")).get("values");
                healthProperties.getKeys().addAll(keys);
                healthProperties.getValues().addAll(values);
                currentPersona.setHealthProperties(healthProperties);

                PersonaMapProperties shoppingProperties = new PersonaMapProperties();
                keys = (List<String>) ((Map) ((Map) personaElement).get("shoppingProperties")).get("keys");
                values = (List<Integer>) ((Map) ((Map) personaElement).get("shoppingProperties")).get("values");
                shoppingProperties.getKeys().addAll(keys);
                shoppingProperties.getValues().addAll(values);
                currentPersona.setShoppingProperties(shoppingProperties);

                list.add(currentPersona);
            }
        }
        return list;
    }

    public List<TrafficActionObject> transformTrafficActionJsonMap(Map map) {
        List<TrafficActionObject> list = new ArrayList();

        for (Object key : map.keySet()) {
            TrafficActionObject trafficActionObject = new TrafficActionObject();
            Map item = (Map) map.get(key);
            trafficActionObject.setBlock((boolean) item.get("block"));
            trafficActionObject.setMaxspeed((int) item.get("maxspeed"));
            trafficActionObject.setType((String) item.get("type"));
            List<Location> locations = new ArrayList<>();

            List<Map> locationsMap = (ArrayList<Map>) item.get("locations");
            for (Map loc : locationsMap) {
                Location location = new Location();
                location.setLat((double) loc.get("lat"));
                location.setLon((double) loc.get("lng"));
                locations.add(location);
            }
            trafficActionObject.getLocations().addAll(locations);
            list.add(trafficActionObject);
        }

        return list;
    }


    public List<FinalDestination> transformDestArrayJsonMap(List list) {
        List<FinalDestination> returnList = new ArrayList();

        list.forEach(entry -> {
            Map map = (Map) entry;

            FinalDestination finalDestination = new FinalDestination();
            finalDestination.setName((String) map.get("name"));
            finalDestination.setCapacity(Integer.valueOf((String) map.get("capacity")));
            finalDestination.setCurrentlyWaiting(0);
            finalDestination.setAreaDiameterForParking(Double.valueOf((String) map.get("areaForParking")));

            Location location = new Location();

            location.setLat((Double) ((Map) map.get("location")).get("lat"));
            location.setLon((Double) ((Map) map.get("location")).get("lng"));
            location.setLocationName((String) map.get("name"));
            location.setPoiType((String) map.get("markerPoiType"));

            finalDestination.setLocation(location);

            returnList.add(finalDestination);
        });

        return returnList;
    }

    public DumpDestination transformObjectToDumpDestObject(LinkedHashMap dumpDestinationObject) {

        String name = (String) dumpDestinationObject.get("name");
        Double loc_lat = (Double) ((Map) dumpDestinationObject.get("location")).get("lat");
        Double loc_lon = (Double) ((Map) dumpDestinationObject.get("location")).get("lng");

        DumpDestination dumpDestination = new DumpDestination();

        dumpDestination.setCapacity(Integer.MAX_VALUE);
        dumpDestination.setCurrentlyWaiting(0);

        dumpDestination.setName(name);
        Location location = new Location();

        location.setLon(loc_lon);
        location.setLat(loc_lat);
        location.setLocationName(name);
        location.setPoiType((String) dumpDestinationObject.get("markerPoiType"));
        dumpDestination.setLocation(location);

        return dumpDestination;
    }


    public String getVelocitySectionsInBoundingBox(Location min, Location max) {
        return servicePort.getVelocitySectionsInBoundingBox(max, min);
    }

    //
    private int numberCounterThreads = 1;

    /**
     * simple counter function that returns list of PathCount object.
     * PathCount are used to count how often agents drove on a specific path (From Location -> To location)
     * The counting may is implemented using an Executor Service. By adjusting the numberCounterThreads variable
     * the number of parralel working Threads that count the driven Streets can be increased or decreased.
     *
     * @return
     */
  /*  public List<PathCount> compareDrivenStreets(List<FrontendAgent> agents) {

        HashMap<String, PathCount> coordinateCounter = new HashMap<>();

        List<List<FrontendAgent>> taskPackages = this.createTaskPackages(agents);
        ExecutorService executor = Executors.newFixedThreadPool(numberCounterThreads);

        taskPackages.forEach(taskPackage -> {
            Future<HashMap<String, PathCount>> futureResult = executor.submit(new CounterTask(taskPackage));
            try {
                HashMap<String, PathCount> result = (HashMap<String, PathCount>) futureResult.get();

                synchronized (coordinateCounter) {
                    result.forEach((key, val) -> {
                        if (coordinateCounter.containsKey(key)) {
                            coordinateCounter.get(key).setCount(coordinateCounter.get(key).getCount() + 1);
                        } else {
                            coordinateCounter.put(key, val);
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });


        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
            System.out.println("Done!!!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<PathCount> returnList = new ArrayList<>();

        coordinateCounter.forEach((key, pathCount) -> returnList.add(pathCount));

        System.out.println("Done");

        return returnList;
    } */
    private List<List<FrontendAgent>> createTaskPackages(List<FrontendAgent> agents) {
        ArrayList<List<FrontendAgent>> taskPackages = new ArrayList<>();
        int sizeTaskPackages = 0;

        sizeTaskPackages = (int) Math.ceil((agents.size() + (agents.size() % this.numberCounterThreads)) / this.numberCounterThreads);


        for (int i = 0; i < numberCounterThreads; i++) {
            List<FrontendAgent> taskPackage = null;
            if ((i + 1) * sizeTaskPackages < agents.size())
                taskPackage = agents.subList(i * sizeTaskPackages, (i + 1) * sizeTaskPackages);
            else
                taskPackage = agents.subList(i * sizeTaskPackages, agents.size());

            taskPackages.add(taskPackage);
        }

        return taskPackages;
    }

    public List<StartEnd> mergeArrivalTimesIntoDestMatrix(Map desiredArrivaltimesMap, List<StartEnd> destinationMatrix) {

        for (String key : (Set<String>) desiredArrivaltimesMap.keySet()) {

            double shareCount = 0;
            //int desiredArrivalTick = Integer.parseInt((String) ((ArrayList) dataTuple).get(1));

            String locationNameFrom = key.substring(0, key.indexOf(";"));
            String locationNameTo = key.substring(key.indexOf(";") + 1);

            ArrayList<LinkedHashMap> desiredArrivalDetails = (ArrayList<LinkedHashMap>) desiredArrivaltimesMap.get(key);


            StartEnd startEnd = getStartEndByLocNames(locationNameFrom, locationNameTo, destinationMatrix);
            ArrayList<DttBean> dttBeanList = new ArrayList<>();
            for (LinkedHashMap dttDetailMap : desiredArrivalDetails) {
                DttBean dttBean = new DttBean();
                for (Object mapKey : dttDetailMap.keySet()) {
                    if (mapKey.equals("id")) {

                    } else if (mapKey.equals("value")) {
                        dttBean.setDesiredArrivalTick((Integer) dttDetailMap.get("value"));
                    } else if (mapKey.equals("share")) {
                        Double share = Double.valueOf(String.valueOf(dttDetailMap.get("share")));
                        shareCount += share;
                        dttBean.setShare(share);
                    }
                }
                dttBeanList.add(dttBean);
            }

            if (shareCount != 100d) {
                for (DttBean dttBean : dttBeanList) {
                    dttBean.setShare((dttBean.getShare() / shareCount) * 100);
                }
            }

            startEnd.getDesiredArrivalTickList().addAll(dttBeanList);

        }

        return destinationMatrix;
    }

    private StartEnd getStartEndByLocNames(String locationNameFrom, String locationNameTo, List<StartEnd> destinationMatrix) {

        for (StartEnd startEnd : destinationMatrix) {

            if (startEnd.getStart().getLocationName().equals(locationNameFrom)
                    && startEnd.getEnd().getLocationName().equals(locationNameTo)) {
                return startEnd;
            }

        }

        return null;

    }

    public String getBikePathsInBoundingBox(Location locMin, Location locMax) {
        return servicePort.getBikePathsInBoundingBox(locMax, locMin);
    }

    public String getPoiTypeFromName(String name, List<FinalDestination> destinationList) {
        FinalDestination finalDestination = destinationList.stream()
                .filter(destination -> destination.getLocation().getLocationName().equals(name))
                .collect(Collectors.toList()).get(0);
        return finalDestination.getLocation().getPoiType();
    }


   /* class CounterTask implements Callable<HashMap<String, PathCount>> {

        private List<FrontendAgent> agents;

        public CounterTask(List<FrontendAgent> agents) {
            this.agents = agents;
        }

        @Override
        public HashMap<String, PathCount> call() throws Exception {

            HashMap<String, PathCount> coordinateCounter = new HashMap<>();

            agents.forEach(agent -> {

                List<RouteDetail> routeDetails = agent.getRouteDetails();
                String previousKey = "";
                for(int i = 0; i<routeDetails.size();i++) {
                    String key = routeDetails.get(i).getEdgeId().toString();

                    // does not count duplicate edge ids that belong to one Agent!
                    if (!previousKey.equals(key)){
                        this.increaseCounterValue(coordinateCounter, key, routeDetails.get(i).getEdgeStart(), routeDetails.get(i).getEdgeEnd());
                    }

                    previousKey = key;

                }
            });

            return coordinateCounter;
        }

        private void increaseCounterValue(HashMap<String, PathCount> coordinateCounter, String key, Location from, Location to){
            if(coordinateCounter.containsKey(key))
                    coordinateCounter.get(key).setCount(coordinateCounter.get(key).getCount() + 1);
            else{
                PathCount pathCount = new PathCount();
                pathCount.setCount(1);
                pathCount.setFrom(from);
                pathCount.setTo(to);
                coordinateCounter.put(key, pathCount);
            }
        }
    }*/
}
