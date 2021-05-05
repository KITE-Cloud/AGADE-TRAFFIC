## The RoutingKit 
is the part of the backend that is responsible for determining routes and positions of agents. The RoutingKit is structured in three submodules:
* neo4j-plugin
* osm-cleaner
* routing-engine

### Neo4J-Plugin
This module holds customisation to the default settings of A* routing algorithm in neo4j such as custom cost functions. For calling the algorithm through the plugin a procedure is provided which expects two node IDs and a routing preference. This function calculates required time for travelling on a road section given current mode of travel, speed limits and traffic load.  Use the maven lifecycle package to produce a jar file that can be imported into neo4j as a plugin.

### OSM-Cleaner
In order to initially load or update map data, OSM files need to be preprocessed. The module includes implementation for cleaning unused information of osm files. For this purpose `OSM-Cleaner` makes use of [Osmosis]( https://github.com/openstreetmap/osmosis) application. Preprocessing can be conducted using the graphical user interface provided in the `SetupManagerApplication`. Furthermore this module includes implementation for importing cleaned OSM data into the neo4j database which can also be done using `SetupManagerApplication`. 

### Routing-Engine
Once OSM data has been imported into neo4j, `Routing-Engine` can be started for calculating routes. The routing-engine is implemented as an RMI Server and processes routing requests from agents created in AGADE Backend. Communication between AGADE Backend and Routing Engine can be described as follows: For each simulation round (tick), agents send a request to Routing Engine via `RMI.Client.routeAgent()` method to ask for routing information. Routing Engine takes current information of the agent as input and calculates the next position of the agent based on A* algorithm configured in `Neo4J-Plugin`. 
