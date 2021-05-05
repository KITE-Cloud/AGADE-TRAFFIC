
## AGADE Traffic Frontend 
is a Java-based web application that has been implemented using SpringWeb. Project structure is structured in two major packages: `webapp` and `java`.

### Webapp package

The webapp package contains a file `index.html` in which html components for rendering the webfrontend are managed from a central perspective. CSS, JS and image files are located in the corresponding resources folders and imported by `index.html`. AGADE Traffic frontend is based on `Bootstrap 4` and `jQuery`. For map visualisation AGADE Traffic makes use of `Leaflet` which is an open source Javascript library. For the initialisation coordinates of a starting location can be specified in `InitailMap.js`. 

### Java Package

The Java package provides a series of HTTP endpoints in order to establish communication between the webapp and the Java backend. HTTP endpoints act as intermediary and performs necessary data transformations in order to forward web requests to service methods provided by AGADE Backend. 
e contains information on calculated KPIs and statistics. Furthermore, information on agent properties and movement is included. The file can be imported into the frontend for visualisation.