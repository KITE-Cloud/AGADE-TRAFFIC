#
# Build stage
#
FROM maven:3.5-jdk-8 AS build
COPY src /usr/src/app/src/
COPY pom.xml /usr/src/app
COPY neo4J-plugin /usr/src/app/neo4J-plugin
COPY osm-cleaner /usr/src/app/osm-cleaner
COPY routing-engine /usr/src/app/routing-engine

RUN mvn -f /usr/src/app/routing-engine/pom.xml clean package
#RUN find /usr/src/app/routing-engine/

RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix /usr/src/app/routing-engine/docker-entrypoint.sh && apt-get --purge remove -y dos2unix && rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["/bin/bash", "/usr/src/app/routing-engine/docker-entrypoint.sh"]



