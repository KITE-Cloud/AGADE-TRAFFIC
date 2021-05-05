package routingAndCostEvaluationProcedure;

import org.neo4j.graphdb.RelationshipType;

public enum EdgeType implements RelationshipType {
    ROUTE, ONEWAY
}