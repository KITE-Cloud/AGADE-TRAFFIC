package neo4jAccessor;

import exceptions.NoPathFoundException;
import org.neo4j.driver.internal.value.RelationshipValue;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Relationship;
import rmi.RMIConfiguration;
import rmi.model.*;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.v1.*;
import util.SystemConfiguration;

import java.util.*;


/**
 * The type Neo4j query factory.
 */
@SuppressWarnings("Duplicates")
public class Neo4JQueryFactory {

    private Driver driver;

    // Reference: https://wiki.openstreetmap.org/wiki/DE:Genauigkeit_von_Koordinaten
    private double degreeOfLatitude = 50;  // ~ deutscher Breitengrad
    private double long_meterPerFifthDecimal = Math.cos(degreeOfLatitude) * 1.11;
    private double lat_meterPerFifthDecimal = 1.11; // 1 meter = 1.11 * 0,00001 (lat)


    public List<BikePath> getBikePathsInBoundingBox(Location max, Location min) {
        String query = "MATCH (n)-[r]-(m) WHERE n.lat<=" + max.getLat() +
                " AND n.lon<=" + max.getLon() +
                " AND n.lat>=" + min.getLat() +
                " AND n.lon>=" + min.getLon() +
                " AND m.lat<=" + max.getLat() +
                " AND m.lon<=" + max.getLon() +
                " AND m.lat>=" + min.getLat() +
                " AND m.lon>=" + min.getLon() +
                " AND r.bicycleAllowed=true" +
                " AND NOT r.streetType='motorway'" +
                " AND NOT r.streetType='motorway_link'" +
                " return distinct r.wayID";

        Map<Way, List<Record>> recordsByWayId = getSortedCoordinatesInBoundingBox(query);


        // create VelocitySections per wayID
        List<BikePath> ways = new ArrayList<>();
        for (Map.Entry<Way, List<Record>> entry : recordsByWayId.entrySet()) {
            List<Location> locs = new ArrayList<>();
            for (Record rec : entry.getValue()) {
                Location locN = getLocationFromNode((NodeValue) rec.get("n"), LOCATION);
                Location locM = getLocationFromNode((NodeValue) rec.get("o"), LOCATION);
                locs.add(locN);
                locs.add(locM);
            }
            ways.add(new BikePath(locs, entry.getKey().getName()));
        }

        return ways;
    }

    public List<Location> findPossibleBikeTargetLocation(RoutingInformation routingInfo, List<Long> blockedStreets, Map<String, Integer> modifiedSpeedMap) {

        FinalDestination finalDestination = routingInfo.getFinalDestination();

        Location location = finalDestination.getLocation();

        boolean destinationFound = false;
        int radius = 20;

        StatementResult nearestNodes;
        List<Location> locationList = new ArrayList<>();
        while (!destinationFound) {

            locationList.clear();

            String query = genQueryToFindNodesInRadius("distinct n", location, radius);
            radius += 20;

            nearestNodes = executeCypherQuery(query, null);

            while (nearestNodes.hasNext()) {
                if (!destinationFound) destinationFound = true;

                Record record = nearestNodes.next();
                //System.out.println();

                Node node = record.get(0).asNode();
                double lon = node.get("lon").asDouble();
                double lat = node.get("lat").asDouble();

                locationList.add(new Location(lat, lon));
            }

        }


        return locationList;
    }


    private static final class InstanceHolder {
        private static final Neo4JQueryFactory INSTANCE = new Neo4JQueryFactory();
    }

    /**
     * Instantiates a new Neo4j query factory.
     */
    private Neo4JQueryFactory() {
        RMIConfiguration config = RMIConfiguration.getInstance();
        this.driver = GraphDatabase.driver(config.URL_NEO4J, AuthTokens.basic(config.USER_NEO4J, config.PASS_NEO4J));
        dataCleansingMaxSpeed();
    }

    public static Neo4JQueryFactory getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Execute cypher query statement result.
     *
     * @param query      the query
     * @param parameters the parameters
     * @return the statement result
     */
    public StatementResult executeCypherQuery(String query, Map<String, Object> parameters) {

        if (parameters == null)
            parameters = new HashMap<>();

        try (Session session = driver.session()) {
            Transaction tx = session.beginTransaction();
            StatementResult result = tx.run(query, parameters);
            tx.success();
            return result;
        }
    }


    public int getAnzCarsOnSameRoad(List<RelationshipInfoBean> relations, double shareOfLastRelation) {
        boolean firstIteration = true;

        String query = "Match (n)-[r]-(m) where ";
        for (RelationshipInfoBean relation : relations) {
            query += firstIteration ? "" : "OR ";
            firstIteration = false;
            query += "id(r) = " + relation.getRelationshipId() + " ";
        }
        query += "return r";


        StatementResult statementResult = executeCypherQuery(query, null);

        int carCounter = 0;
        while (statementResult.hasNext()) {
            Record record = statementResult.next();

            Relationship relation = record.get(0).asRelationship();

            if (!statementResult.hasNext()) carCounter += (int) shareOfLastRelation * relation.get("anzCars").asInt();
            else carCounter += relation.get("anzCars").asInt();
        }

        return carCounter;
    }

    /**
     * Find path value.
     *
     * @param start             the start
     * @param end               the end
     * @param routingPreference the routing preference
     * @return the value
     */
    public Value findPath(Vehicle vehicle, int start, int end, String routingPreference, List<Long> blockedStreets, Map<String, Integer> modifiedStreetMap, int hateFactorCarsOnSameRoad) {
        Map<String, Object> map = new TreeMap();
        map.put("vehicle", vehicle.toString());
        map.put("startnode", start);
        map.put("endnode", end);
        map.put("routingPreference", routingPreference);
        map.put("blocked", blockedStreets);
        map.put("modifiedSpeedMap", modifiedStreetMap);
        map.put("hateFactorCarsOnSameRoad", hateFactorCarsOnSameRoad);
        String query = "call routingAndCostEvaluationProcedure.aStar($vehicle,$startnode,$endnode,$routingPreference,$blocked,$modifiedSpeedMap, $hateFactorCarsOnSameRoad);";

        try (Session session = driver.session()) {
            StatementResult result = session.run(query, map);
            List<Record> list = result.list();
            if (list.size() == 0) {
                throw new NoPathFoundException("No path found. Start: " + start + " | End: " + end);
            }
            List<Value> values = list.get(0).values();
            return values.get(0);
        } catch (Exception e) {
            if (e instanceof NoPathFoundException) {
                System.out.println(e.getMessage());
            } else {
                e.printStackTrace();
            }
            return null;
        }
    }


    /**
     * updates the number of cars on every road
     *
     * @param roadInfo the road info
     * @return boolean
     */
    public boolean updateNumberOfCarsOnRoads(HashMap<Long, Integer> roadInfo) {

        // first reset all anzCars values
        executeCypherQuery("MATCH ()-[r]-() SET r.anzCars=0", null);

        // then write new values
        ArrayList<HashMap<String, Object>> roadInfoStructured = new ArrayList<>();
        for (HashMap.Entry<Long, Integer> entry : roadInfo.entrySet()) {
            HashMap<String, Object> tmpMap = new HashMap<>();
            tmpMap.put("id", entry.getKey());
            tmpMap.put("anzCars", entry.getValue());
            roadInfoStructured.add(tmpMap);
        }

        String query = "UNWIND {data} AS d MATCH ()-[r]-() WHERE id(r) = d.id SET r.anzCars = d.anzCars";
        Map<String, Object> map = new HashMap<>();
        map.put("data", roadInfoStructured);
        executeCypherQuery(query, map);

        return true;
    }


    /**
     * finds node in neo4j that is nearest to a given location
     *
     * @param location the location
     * @return int
     */
    public int findNodeNearestToLocation(Location location) {

        double latLowerEnd = Math.floor(location.getLat() * 10000) / 10000d; //trims to 4 decimal places
        double lonLowerEnd = Math.floor(location.getLon() * 10000) / 10000d;
        double latUpperEnd = latLowerEnd + 0.01;
        double lonUpperEnd = lonLowerEnd + 0.01;

        String query = "MATCH (n) WHERE n.lat>" + latLowerEnd + " AND n.lat<=" + latUpperEnd + " AND n.lon>" + lonLowerEnd + " AND n.lon<=" + lonUpperEnd + " RETURN *";
        StatementResult nearestNodes = executeCypherQuery(query, null);

        int nearestNodeId = 0;
        double differenceToLocation = Double.MAX_VALUE;

        while (nearestNodes.hasNext()) {
            Record record = nearestNodes.next();
            NodeValue node = (NodeValue) record.get(0);
            int id = Math.toIntExact(node.asNode().id());
            Map<String, Object> properties = node.asNode().asMap();

            double lon = (double) properties.get("lon");
            double lat = (double) properties.get("lat");
            double difference = Math.abs(location.getLat() - lat) + Math.abs(location.getLon() - lon);

            if (differenceToLocation > difference) {
                differenceToLocation = difference;
                nearestNodeId = id;
            }

        }
        return nearestNodeId;
    }

    private String genQueryToFindNodesInRadius(String returnPattern, Location location, double areaRadius) {
        // Parking Slot bounding box.
        double latLowerEnd = location.getLat() - (areaRadius / lat_meterPerFifthDecimal * 0.00001);
        double lonLowerEnd = location.getLon() - (areaRadius / long_meterPerFifthDecimal * 0.00001);
        double latUpperEnd = location.getLat() + (areaRadius / lat_meterPerFifthDecimal * 0.00001);
        double lonUpperEnd = location.getLon() + (areaRadius / long_meterPerFifthDecimal * 0.00001);

        return "MATCH (n)-[r]->(m) WHERE " +
                "n.lat>" + latLowerEnd + " AND " +
                "n.lat<=" + latUpperEnd + " AND " +
                "n.lon>" + lonLowerEnd + " AND " +
                "n.lon<=" + lonUpperEnd + " AND " +
                "m.lat>" + latLowerEnd + " AND " +
                "m.lat<=" + latUpperEnd + " AND " +
                "m.lon>" + lonLowerEnd + " AND " +
                "m.lon<=" + lonUpperEnd + " RETURN " + returnPattern;
    }

    public int findNodeNearToLocation(FinalDestination destination) {

        Location location = destination.getLocation();
        double areaDiameterForParking = destination.getAreaDiameterForParking();
        double areaRadiusForParking = areaDiameterForParking / 2;

        String query = genQueryToFindNodesInRadius("*", location, areaRadiusForParking);
        StatementResult nearestNodes = executeCypherQuery(query, null);

        int nearestNodeId = 0;
        double differenceToLocation = Double.MAX_VALUE;

        while (nearestNodes.hasNext()) {
            Record record = nearestNodes.next();
            NodeValue loc1 = (NodeValue) record.get(0);
            NodeValue loc2 = (NodeValue) record.get(1);
            RelationshipValue relationship = (RelationshipValue) record.get(2);


            String streetType = relationship.asRelationship().get("streetType").asString();
            double distance = relationship.asRelationship().get("distance").asDouble();

            int relCapacity = 0;
            int loc1Capacity = 0, loc2Capacity = 0;

            double meterPerParkingSlot = 10;

            if (!streetType.equals("footway")) {

                relCapacity = (int) (distance / meterPerParkingSlot);
                loc1Capacity = (relCapacity / 2) + relCapacity % 2;
                loc2Capacity = (relCapacity / 2);

                ParkingDestination parkingDestination1 = getParkingDestination(loc1, "" + loc1.asNode().id(), loc1Capacity);
                // TODO: 15.11.2019  setFinalDestination rausnehmen und info in Agent schreiben
                ParkingDestination parkingDestination2 = getParkingDestination(loc2, "" + loc2.asNode().id(), loc2Capacity);

                if (!isSameLocation(destination.getLocation(), parkingDestination1.getLocation()))
                    this.addParkingDestinationToFinalDestination(destination, parkingDestination1, loc1.asNode().id());
                if (!isSameLocation(destination.getLocation(), parkingDestination2.getLocation()))
                    this.addParkingDestinationToFinalDestination(destination, parkingDestination2, loc2.asNode().id());

                //System.out.println();
            }

        }
        return nearestNodeId;
    }

    public boolean isSameLocation(Location l1, Location l2) {
        return l1.getLat() == l2.getLat() && l1.getLon() == l2.getLon();
    }

    private ParkingDestination getParkingDestination(NodeValue node, String pDId, int locCapacity) {

        SystemConfiguration config = SystemConfiguration.getInstance();

        if (config.getParkingDestinations().containsKey(pDId)) {
            return config.getParkingDestinations().get(pDId);
        } else {
            ParkingDestination parkingDestination = new ParkingDestination();
            parkingDestination.setLocation(getLocationFromNode(node, LOCATION));
            parkingDestination.setName(pDId);
            parkingDestination.setCapacity(locCapacity);
            config.getParkingDestinations().put(pDId, parkingDestination);
            return parkingDestination;
        }
    }

    public void addParkingDestinationToFinalDestination(FinalDestination finalDestination, ParkingDestination parkingDestination, Long locationId) {
        if (parkingDestination.getCapacity() > 0)
            if (!finalDestination.getParkingSlots().containsKey(locationId)) {
                finalDestination.getParkingSlots().put(locationId, parkingDestination);
            } else {
                ParkingDestination oldParkingDestination = finalDestination.getParkingSlots().get(locationId);
                oldParkingDestination.setCapacity(parkingDestination.getCapacity() + oldParkingDestination.getCapacity());
            }
    }

    public Location getLocationFromNodeID(int nodeID) {
        String query = "MATCH (n) WHERE id(n)=" + nodeID + " RETURN n";
        StatementResult statementResult = executeCypherQuery(query, null);
        if (statementResult.hasNext()) {
            Record record = statementResult.next();
            NodeValue node = (NodeValue) record.get(0);
            Map<String, Object> properties = node.asNode().asMap();
            double lon = (double) properties.get("lon");
            double lat = (double) properties.get("lat");
            return new Location(lat, lon);
        }

        return new Location(100d, 200d); // invalid Location
    }


    private Map<Way, List<Record>> getSortedCoordinatesInBoundingBox(String query) {
        StatementResult statementResult = executeCypherQuery(query, null);
        List<Integer> wayIDs = new ArrayList<>();
        while (statementResult.hasNext()) {
            Record record = statementResult.next();
            int wayID = record.values().get(0).asInt();
            wayIDs.add(wayID);
        }

        query = "MATCH (n)-[r]->(o) where r.wayID IN {data} return *";
        Map<String, Object> map = new HashMap<>();
        map.put("data", wayIDs);
        statementResult = executeCypherQuery(query, map);

        // group records by wayID
        Map<Way, List<Record>> recordsByWayId = new HashMap<>();
        while (statementResult.hasNext()) {
            Record record = statementResult.next();
            Map<String, Object> relProperties = record.get("r").asRelationship().asMap();

            int wayID = Integer.parseInt(relProperties.get("wayID").toString());
            double maxSpeed = Double.parseDouble(relProperties.get("maxSpeed").toString());
            String name = relProperties.get("name").toString();
            Way way = new Way(wayID, name, maxSpeed);

            if (recordsByWayId.containsKey(way)) {
                recordsByWayId.get(way).add(record);
            } else {
                List<Record> records = new ArrayList<>();
                records.add(record);
                recordsByWayId.put(way, records);
            }
        }
        return recordsByWayId;
    }


    public List<VelocitySection> getVelocitySectionsInBoundingBox(Location max, Location min) {
        String query = "MATCH (n)-[r]-(m) WHERE n.lat<=" + max.getLat() +
                " AND n.lon<=" + max.getLon() +
                " AND n.lat>=" + min.getLat() +
                " AND n.lon>=" + min.getLon() +
                " AND m.lat<=" + max.getLat() +
                " AND m.lon<=" + max.getLon() +
                " AND m.lat>=" + min.getLat() +
                " AND m.lon>=" + min.getLon() +
                " return distinct r.wayID";
        /*StatementResult statementResult = executeCypherQuery(query,null);
        List<Integer> wayIDs = new ArrayList<>();
        while (statementResult.hasNext()){
            Record record = statementResult.next();
            int wayID = record.values().get(0).asInt();
            wayIDs.add(wayID);
        }

        query = "MATCH (n)-[r]->(o) where r.wayID IN {data} return *";
        Map<String, Object> map = new HashMap<>();
        map.put("data", wayIDs);
        statementResult = executeCypherQuery(query, map);

        // group records by wayID
        Map<Way, List<Record>> recordsByWayId = new HashMap<>();
        while (statementResult.hasNext()) {
            Record record = statementResult.next();
            Map<String, Object> relProperties = record.get("r").asRelationship().asMap();

            int wayID = Integer.parseInt(relProperties.get("wayID").toString());
            double maxSpeed = Double.parseDouble(relProperties.get("maxSpeed").toString());
            String name = relProperties.get("name").toString();
            Way way = new Way(wayID, name, maxSpeed);

            if (recordsByWayId.containsKey(way)) {
                recordsByWayId.get(way).add(record);
            } else {
                List<Record> records = new ArrayList<>();
                records.add(record);
                recordsByWayId.put(way, records);
            }
        }
        */

        Map<Way, List<Record>> recordsByWayId = getSortedCoordinatesInBoundingBox(query);
        // sort records
        for (Map.Entry<Way, List<Record>> entry : recordsByWayId.entrySet()) {
            Collections.sort(entry.getValue(), (rec1, rec2) -> {
                if (rec1.get("n").get("lat") == rec2.get("o").get("lat") &&
                        rec1.get("n").get("lon") == rec2.get("o").get("lon")) {
                    return -1;
                } else if (rec1.get("o").get("lat") == rec2.get("n").get("lat") &&
                        rec1.get("o").get("lon") == rec2.get("n").get("lon")) {
                    return 1;
                } else return 0;
            });
        }

        // create VelocitySections per wayID
        List<VelocitySection> ways = new ArrayList<>();
        for (Map.Entry<Way, List<Record>> entry : recordsByWayId.entrySet()) {
            List<Location> locs = new ArrayList<>();
            for (Record rec : entry.getValue()) {
                Location locN = getLocationFromNode((NodeValue) rec.get("n"), LOCATION);
                Location locM = getLocationFromNode((NodeValue) rec.get("o"), LOCATION);
                locs.add(locN);
                locs.add(locM);
            }
            ways.add(new VelocitySection(locs, entry.getKey().getMaxSpeed(), entry.getKey().getName()));
        }

        return ways;
    }

    public void dataCleansingMaxSpeed() {
        List<String> queries = new ArrayList<>();
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"motorway\" SET r.maxSpeed =	130.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"motorway_link\" SET r.maxSpeed =	130.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"primary\" SET r.maxSpeed =	130.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"secondary\" SET r.maxSpeed =	100.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"service\" SET r.maxSpeed =	50.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"track\" SET r.maxSpeed =	50.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"living_street\" SET r.maxSpeed =	10.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"tertiary\" SET r.maxSpeed =	80.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"footway\" SET r.maxSpeed =	8.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"path\" SET r.maxSpeed =	8.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"residential\"	SET r.maxSpeed =	30.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"unclassified\" SET r.maxSpeed =	50.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"cycleway\" SET r.maxSpeed =	30.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"platform\" SET r.maxSpeed =	30.0	");
        queries.add("MATCH ()-[r]-() WHERE r.maxSpeed = -1 AND r.streetType =\"steps\" SET r.maxSpeed =	8.0	");

        for (String query : queries) {
            executeCypherQuery(query, null);
        }

    }

    private final String GHOST_LOCATION = "GLOC", LOCATION = "loc";

    private Location getLocationFromNode(NodeValue node, String type) {
        Map<String, Object> properties = node.asNode().asMap();
        double lon = (double) properties.get("lon");
        double lat = (double) properties.get("lat");

        if (type.equals(LOCATION))
            return new Location(lat, lon);


        return new Location(lat, lon);
    }


    private class Way {
        private int id;
        private String name;
        private double maxSpeed;

        public Way(int id, String name, double maxSpeed) {
            this.id = id;
            this.name = name;
            this.maxSpeed = maxSpeed;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getMaxSpeed() {
            return maxSpeed;
        }
    }

}


