package controller;

import org.neo4j.graphdb.RelationshipType;

public enum OSMRelationship implements RelationshipType {
    BBOX,
    CHANGESET,
    FIRST_NODE,
    GEOM,
    MEMBER,
    NEXT,
    NODE,
    OSM,
    OSM_USER,
    RELATIONS,
    TAGS,
    USER,
    USERS,
    WAYS,
    ROUTE,
    ONEWAY,
    CYCLEWAY
}
