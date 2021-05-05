#
# Build stage
#
FROM maven:3.5-jdk-8 AS build
COPY src /usr/src/app/src/
COPY pom.xml /usr/src/app
COPY neo4J-plugin /usr/src/app/neo4J-plugin
COPY osm-cleaner /usr/src/app/osm-cleaner
COPY routing-engine /usr/src/app/routing-engine

RUN mvn -f /usr/src/app/neo4J-plugin/pom.xml clean package
#RUN find /usr/src/app/neo4J-plugin/target

#
# Package stage
#
FROM neo4j:3.4.9
ENV NEO4J_AUTH=neo4j/1234
ENV NEO4J_dbms_connector_bolt_thread__pool__max__size=100000
ENV NEO4J_dbms_memory_heap_max__size=40G
#ENV NEO4J_dbms_memory_pagecache_size=90G
#ENV NEO4J_dbms_memory_heap_initial__size=90G
ENV JVM_OPTS="-Xmx40g -Xms2g"
#-XX:MaxPermSize=20G

COPY --from=build /usr/src/app/neo4J-plugin/target/neo4J-plugin-0.1.jar /var/lib/neo4j/plugins
COPY neo4J-plugin/src/main/resources/neo4j-spatial-0.25.6-neo4j-3.4.5-server-plugin.jar /var/lib/neo4j/plugins
COPY osm-cleaner/src/main/resources/neo4j-data/graph.db /var/lib/neo4j/data/databases/graph.db
#RUN find /var/lib/neo4j/data
#RUN ulimit -c unlimited




