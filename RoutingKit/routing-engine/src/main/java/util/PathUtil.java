package util;

import org.neo4j.driver.internal.InternalRelationship;
import org.neo4j.driver.internal.value.PathValue;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Utility Klasse
 */
public class PathUtil {

    /**
     * utility method: returns als nodes from a path.
     */
    public ArrayList<Node> getNodesFromPath(PathValue path) throws RemoteException {
        Iterable<Node> nodesAsIterable = path.asPath().nodes();
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node node: nodesAsIterable){
            nodes.add(node);
        }
        return nodes;
    }


    /**
     * utility method: returns als relationships from a path.
     */
    public ArrayList<InternalRelationship> getRelationshipsFromPath(PathValue pathValue) throws RemoteException{

        // some directions of path segments are to the wrong side. Maybe this can be better fixed in the neo4j-plugin.
        // A quick fix is done here:
        ArrayList<InternalRelationship> relationships = new ArrayList<>();
        Path path = pathValue.asPath();
        long lastTraversed = path.start().id();

        for (Path.Segment segment : path){
            InternalRelationship relationship = (InternalRelationship) segment.relationship();
            if (relationship.startNodeId() == lastTraversed){
                relationships.add(relationship);
                lastTraversed = relationship.endNodeId();
            } else {
                long newStart = relationship.endNodeId();
                long newEnd = relationship.startNodeId();
                relationship.setStartAndEnd(newStart, newEnd);
                relationships.add(relationship);
                lastTraversed = relationship.startNodeId();
            }
        }

        return relationships;
    }


}
