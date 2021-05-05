package controller;

import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MASOSMImporter {

    private static final Logger LOG = LoggerFactory.getLogger(MASOSMImporter.class);

    private MASConfiguration config;
    private GraphDatabaseService db = null;


    public MASOSMImporter() {
        this.config = MASConfiguration.getInstance();
    }


    /**
     * call this method to load your osm file into neo4j
     * @param pathToOSM the path in the file system
     */
    public void loadOSMIntoDB(String pathToOSM){
        OSMImporter importer = new OSMImporter(pathToOSM);
        importer.setCharset(Charset.forName("UTF-8"));
        BatchInserter batchInserter = null;
        try {
            batchInserter = BatchInserters.inserter(new File(config.DB_PATH), config.getDBConfiguration());
            //batchInserter = BatchInserters.inserter(new File("RoutingKit/osm-cleaner/src/main/resources/neo4j-data/graph.db"), config.getDBConfiguration());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            importer.importFile(batchInserter, pathToOSM, false);
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
        batchInserter.shutdown();

       cleanDB();
    }


    /***
     * Methode zum Kovertieren der geladenen Neo4J Spatial Struktur in
     * gewünschte Form
     ***/
    public void cleanDB() {
        try {
            startDB();

            if (!isCleaned()) { // Überprüft ob DB bereits konvertiert wurde
                createRoutes(); // Erzeugt neue Relationships vom Typ "ROUTE"
                deleteRelationships(); // Löscht überflüssige Relationships
                deleteNodes(); // Löscht überflüssige Nodes
                changeCleanedStatus();
            }

        } catch (Exception e) {
            LOG.error("Error while cleaning DB.", e);
        }

    }


    // startet die DB, deren Filepath in MASConstants.DB_PATH hinterlegt ist
    private void startDB() throws Exception {
        db = new GraphDatabaseFactory().newEmbeddedDatabase(new File(config.DB_PATH));
        registerShutdownHook(db);
        LOG.info("Started Neo4J DB");
    }


    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(
                new Thread() {
                    @Override
                    public void run() {
                        graphDb.shutdown();
                    }
                });
    }


    private boolean isCleaned() {
        boolean cleaned = false;
        try (Transaction tx = db.beginTx()) {
            Node node = null;
            ResourceIterator<Node> nodeIterator = db.findNodes(OSMLabel.INFO_NODE);

            // Es sollte eigentlich nur genau 1 INFO_NODE existieren
            if (nodeIterator.hasNext()) {
                node = nodeIterator.next();
                String cleanedProperty = (String) node.getProperty("cleaned", "NO");
                if (cleanedProperty.equals("YES")) {
                    cleaned = true;
                }
            }

            tx.success();
        } catch (Exception e) {
            throw e;
        }

        return cleaned;
    }


    private void createRoutes() {
        try (Transaction tx = db.beginTx()) {

            LOG.info("Get all FIRST_NODE relationships.");
            ResourceIterable<Relationship> relationships = db.getAllRelationships();
            // iteriert über alle Relationships
            for (Relationship rel : relationships) {
                // es werden aber nur die Relationships "FIRST_NODE" betrachtet
                if (rel.isType(OSMRelationship.FIRST_NODE)) {
                    // Informationen der StartNode der FIRST_NODE Relationship
                    // werden zwischengespeichert. Diese spezielle StartNode
                    // enthält die Informationen zu einem Straßenabschnitt


                    // get maxspeed
                    Node streetNode = rel.getStartNode();
                    List<Node> nodeList = new ArrayList<>();
                    nodeList.add(streetNode);
                    double maxspeed = searchForMaxSpeed(nodeList);
//                    if (endNode.hasRelationship(OSMRelationship.NODE, Direction.OUTGOING)){
//                        Relationship nodeRel = endNode.getSingleRelationship(OSMRelationship.NODE, Direction.OUTGOING);
//                        Node nodeRelEndNode = nodeRel.getEndNode();
//                        if (nodeRelEndNode.hasRelationship(OSMRelationship.TAGS, Direction.OUTGOING)){
//                            Node maxSpeedNode = nodeRelEndNode.getSingleRelationship(OSMRelationship.TAGS, Direction.OUTGOING).getEndNode();
//                            maxspeed = Double.parseDouble((String) maxSpeedNode.getProperty("maxspeed", "-1"));
//                            System.out.println(maxspeed);
//                        }
//                    }

                    String name = (String) streetNode.getProperty("name", "Unknown");
                    String highwayType = (String) streetNode.getProperty("highway", "Unknown");
                    String cycleway = (String) streetNode.getProperty("cycleway", "");
                    boolean isCycleway = !cycleway.equals("");
                    String bicycle = (String) streetNode.getProperty("bicycle", "yes");
                    boolean bicycleAllowed = bicycle.equals("yes");
                    String oneway = (String) streetNode.getProperty("oneway", "Unknown");
                    long wayID = (Long) streetNode.getProperty("way_osm_id", -1L);
                    // double maxspeed = getMaxSpeedByName(db.getAllNodes(), name);

                    // Ausgehend von der EndNode der FIRST_NODE Relationship wird
                    // sukzessiv über alle NEXT_Relationships iteriert

                    Node endNode = rel.getEndNode();
                    while (endNode.hasRelationship(OSMRelationship.NEXT, Direction.OUTGOING)) {
                        Relationship streetRel = endNode.getSingleRelationship(OSMRelationship.NEXT, Direction.OUTGOING);
                        Node startNode = endNode;
                        endNode = streetRel.getEndNode();

                        // Es wird davon ausgegangen Start/EndNode immer eine
                        // NODE Relationship haben (zur Koordinate)
                        // Bei (sauber) geladenen OSM Dateien sollte dies der
                        // Fall sein
                        Node startCoordinateNode = startNode.getSingleRelationship(OSMRelationship.NODE, Direction.OUTGOING).getOtherNode(startNode);
                        Node endCoordinateNode = endNode.getSingleRelationship(OSMRelationship.NODE, Direction.OUTGOING).getOtherNode(endNode);

                        // Koordinatenpunkte erhalten Label "COORDINATE_NODE"
                        // zum späteren einfachen finden
                        startCoordinateNode.addLabel(OSMLabel.COORDINATE_NODE);
                        endCoordinateNode.addLabel(OSMLabel.COORDINATE_NODE);

                        // Es wird eine neue Relationship vom Typ "ROUTE" oder
                        // "ONEWAY"
                        // zwischen den 2 Koordinatenpunkten aufgebaut
                        // Die neue Relationship erhält die
                        // zwischengespeicherten Werte der
                        // Straßenabschnittsdefinition

                        Relationship routeRel;
                        if (oneway.equals("BOTH")) {
                            routeRel = startCoordinateNode.createRelationshipTo(endCoordinateNode, OSMRelationship.ROUTE);
                        } else {
                            routeRel = startCoordinateNode.createRelationshipTo(endCoordinateNode, OSMRelationship.ONEWAY);
                        }

                        routeRel.setProperty("name", name);
                        routeRel.setProperty("streetType", highwayType);
                        routeRel.setProperty("oneway", oneway);
                        routeRel.setProperty("maxSpeed", maxspeed);
                        routeRel.setProperty("anzCars", 0);
                        routeRel.setProperty("distance", calcDistanceBetweenNodes(startCoordinateNode, endCoordinateNode));
                        routeRel.setProperty("lambda", config.getLambdaForStreetType(highwayType));
                        routeRel.setProperty("wayID", wayID);
                        routeRel.setProperty("cycleway", isCycleway);
                        routeRel.setProperty("bicycleAllowed", bicycleAllowed);
                    }
                }
            }

            LOG.info("Built all new ROUTE relationships.");

            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private double calcDistanceBetweenNodes(Node startNode, Node endNode){
        Map<String,Object> n1 = startNode.getProperties("lat", "lon");
        Map<String,Object> n2 = endNode.getProperties("lat", "lon");

        return distBetweenCoordinates(
                (double) n1.get("lon"),
                (double) n1.get("lat"),
                (double) n2.get("lon"),
                (double) n2.get("lat"));
    }


    private double getMaxSpeedByName(ResourceIterable<Node> allNodes, String name) {
        for (Node node : allNodes) {
            if (node.hasProperty("name")
                    && node.getProperty("name").equals(name)
                    && node.hasProperty("maxspeed")) {
                try {
                    double out = Double.parseDouble((String) node.getProperty("maxspeed"));
                    return out;
                } catch (Exception e) {
                    // fails sometimes because of input string "walk", expected?
                    LOG.warn("getMaxSpeedByName failed for {}.", node.getId(), e);
                }
            }
        }
        return 5;
    }


    private void deleteRelationships() {

        try (Transaction tx = db.beginTx()) {
            // LOG.info("Deleting unneeded relationships.");

            ResourceIterable<Relationship> relationships = db.getAllRelationships();
            // Iteriert über alle Relationships
            for (Relationship rel : relationships) {
                if (!(rel.isType(OSMRelationship.ROUTE) || rel.isType(OSMRelationship.ONEWAY))) {
                    rel.delete();
                    // löscht alle Relationships die nicht vom
                    // eigens definierten Typ "ROUTE" oder
                    // "ONEWAY" sind
                }
            }
            tx.success();
            LOG.info("Deleted unneeded relationships (all except new relationship ROUTE and ONEWAY).");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void deleteNodes() throws Exception {
        try (Transaction tx = db.beginTx()) {
            LOG.info("Deleting unneeded nodes.");

            ResourceIterable<Node> nodes = db.getAllNodes();
            // Iteriert über alle Nodes
            for (Node node : nodes) {
                if (!(node.hasLabel(OSMLabel.COORDINATE_NODE) || node.hasLabel(OSMLabel.INFO_NODE))) {
                    // Löscht alle Nodes die nicht das eigene Label
                    // "COORDINATE_NODE" oder "INFO_NODE" besitzen
                    node.delete(); // ACHTUNG: Der Node wird tatsächlich von der
                    // Methode nur gelöscht wenn die Node keine
                    // Relationships mehr hat
                }
            }

            tx.success();
            LOG.info("Deleted unneeded nodes (all except new nodes COORDINATE_NODE and INFO_NODE).");
        } catch (Exception e) {
            throw e;
        }
    }


    private void changeCleanedStatus() {
        try (Transaction tx = db.beginTx()) {
            Node node = null;
            ResourceIterator<Node> nodeIterator = db.findNodes(OSMLabel.INFO_NODE);

            // Es sollte nur genau 1 INFO_NODE existieren
            if (nodeIterator.hasNext()) {
                node = nodeIterator.next();
            } else {
                node = db.createNode(OSMLabel.INFO_NODE);
            }
            node.setProperty("cleaned", "YES");

            tx.success();
        } catch (Exception e) {
            throw e;
        }
    }

    private double distBetweenCoordinates(double lon1, double lat1, double lon2, double lat2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (earthRadius * c);
    }

    private double searchForMaxSpeed(List<Node> nodesStart) {
        List<Node> endnodes = new ArrayList<>();
        for (Node n : nodesStart) {
            Iterable<Relationship> relationships = n.getRelationships(Direction.OUTGOING, OSMRelationship.NEXT,
                    OSMRelationship.NODE, OSMRelationship.TAGS);

            for (Relationship relationship : relationships) {
                Node endNode = relationship.getEndNode();
                endnodes.add(endNode);
            }
            for (Node node : endnodes) {
                if (node.hasProperty("maxspeed")) {
                    String maxSpeed = node.getProperty("maxspeed").toString();
                    try {
                        return Double.parseDouble(maxSpeed);
                    } catch (NumberFormatException e) {
                        return -1d;
                    }
                }
            }
        }
        if (endnodes.size()>0)
            return searchForMaxSpeed(endnodes);
        else return -1;
    }

}
