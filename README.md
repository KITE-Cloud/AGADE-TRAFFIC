# AGADE Traffic Simulation
![version](https://img.shields.io/badge/version-0.1.0-blue) ![license](https://img.shields.io/badge/license-MIT-purple) ![status](https://img.shields.io/badge/activity%20status-active-green)


AGADE Traffic is an agent based traffic simulator written in Java. Main objective of this project is to provide a tool that allows traffic planners to simulate and analyse behavioural structures in road traffic. The model assumes that personal preferences and decisions made by individual travellers have major impact on overall traffic flow. For this reason, AGADE Traffic focuses on modelling individuals as intelligent software agents. The modelling component uses semantic technologies (OWL and SWRL) to express agent knowledge and preferences that utimately has an effect on travel behaviour. The simulator uses the JADEX framework for implementing BDI agents. For representation of geographic network infrastucture, publicly available map data from OpenStreetMap is stored in a graph database (Neo4J). For more information visit our website [www.agade.de](https://agade.de/) and get access to simulation examples as described in our publications. We also provide an online live demo of AGADE Traffic frontend. 

![AGADE Traffic Architecture](https://user-images.githubusercontent.com/20316120/117135437-37aa8d80-ada7-11eb-8b09-ceb40ab7ab66.PNG)


## Getting Started

### Requirements  
![platform](https://img.shields.io/badge/platform-linux%20|%20windows%20|%20osx-darkgreen) ![java](https://img.shields.io/badge/java-8-blue) ![build](https://img.shields.io/badge/build-maven-red) ![virtualisation](https://img.shields.io/badge/virtualisation-docker-lightblue)

### Initial Setup
* Create a local repository using `git clone https://github.com/KITE-Cloud/AGADE-TRAFFIC.git` 
* Open `Frontend`, `Backend` and `RoutingKit` as separate Maven projects in your IDE.
* Start `RoutingKit/osm-cleaner/src/main/java/application/SetupManagerApplication.main()`. On first startup `SetupManagerApplication` creates important environmental variables that are required for connecting service modules. You will need to restart Java to detect changes.  

### Data Preprocessing and Import
* Manually select a map area at [openstreetmap](https://www.openstreetmap.org/export) and use `overpass API` to download necessary osm data for your simulation
* Rename the downloaded file and add  `.osm` file ending e.g. `london.osm`
* Place osm data in `{ProjectDir}/RoutingKit/osm-cleaner/src/main/resources/OSM_Files/`
* Launch `SetupManagerApplication` to select your osm file and clean your osm data. This produces a new osm file e.g. `london_cleaned.osm` in the same directory. 
* Then use `SetupManagerApplication` to select the cleaned osm file and import data into database. 

### Launch Service Modules
**Make sure you have Docker running!**

##### For End-Users
* Open a terminal and navigate to your project directory
* Startup AGADE Traffic Services (`Neo4j`, `RoutingEngine` and `Backend`) by typing `docker-compose up`
* On first startup project build must be completed. Project build will be cached which makes startup of subsequent sessions faster.
* You will have to start AGADE Traffic Frontend separately. In the `Frontend` project, launch `src/main/java/de/thm/agade/frontend/FrontendApplication.main()`
* Open `localhost:8081` in your browser 

##### For Developers
For developers working with AGADE Traffic we recommend starting up services separately. 
* Open a terminal and navigate to `{ProjectDir}/RoutingKit`
* Type in `docker-compose build` to build a prepared docker image of `Neo4j` database. If you make changes to the database (e.g. import new data), you will need to rebuild.
* Once build process is completed, type `docker-compose up` to start up the database
* In the `RoutingKit` project, launch `routing-engine/src/main/rmi/RMIServer.main()`
* In the `Backend` project, launch the `src/main/java/BackendApplication.main()` in the AGADE project
* In the `Frontend` project, launch `src/main/java/de/thm/agade/frontend/FrontendApplication.main()`
* Open `localhost:8081` in your browser 

### Create a simulation 

https://user-images.githubusercontent.com/20316120/116996758-32ccd780-acdc-11eb-9d00-be1369af334c.mp4

Simulation results will be output in the Frontend project under the following path: `src/main/resources/output`

## Licensing
AGADE Traffic is an open source product licensed under MIT



