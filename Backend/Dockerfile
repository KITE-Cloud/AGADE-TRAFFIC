#
# Build stage
#
FROM maven:3.5-jdk-8 AS build
COPY src /usr/src/app/src
COPY libs /usr/src/app
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package
#RUN find /usr/src/app/target

#
# Package stage
#
FROM openjdk:8-jre-slim
COPY --from=build /usr/src/app/target/Backend.jar /usr/app/Backend.jar

#EXPOSE 9000
ENTRYPOINT ["java","-jar","/usr/app/Backend.jar"]

#FROM neo4j:3.4.9




