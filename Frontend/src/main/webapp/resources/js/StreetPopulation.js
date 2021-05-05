var layerGroupStreetPopulation;
var streetPopSecList = [];

var isPopulationModeActivated = false;

function populationMode() {

    if (!isPopulationModeActivated) {
        isPopulationModeActivated = true;
        $("path.leaflet-interactive").css("opacity", "1");
        //drawStreetPopulationOnMap(simulation.pathCounts);
    } else {
        isPopulationModeActivated = false;

        //layerGroupStreetPopulation.clearLayers();
        $("path.leaflet-interactive").css("opacity", "0");
        streetPopSecList = [];
    }
}


function drawStreetPopulationOnMap(streetData) {

    if (streetPopSecList.length == 0) {
        let iHighestPopulationValue = getHighestPopulationValue(streetData);

        streetData.forEach(data => {
            if (data.from != null && data.to != null) {

                var polyline;
                if (data.count / iHighestPopulationValue <= 0.1) {
                    polyline = L.polyline(convertStreetDataIntoLeafletArray(data), {color: '#00ff40'});
                } else if (data.count / iHighestPopulationValue <= 0.2) {
                    polyline = L.polyline(convertStreetDataIntoLeafletArray(data), {color: '#ffbf00'});
                } else if (data.count / iHighestPopulationValue <= 0.4) {
                    polyline = L.polyline(convertStreetDataIntoLeafletArray(data), {color: '#ff8000'});
                } else if (data.count / iHighestPopulationValue <= 0.5) {
                    polyline = L.polyline(convertStreetDataIntoLeafletArray(data), {color: '#ff4000'});
                } else {
                    polyline = L.polyline(convertStreetDataIntoLeafletArray(data), {color: '#ff0000'});
                }
                streetPopSecList.push(polyline);
            }
        });
    }

    layerGroupStreetPopulation = L.layerGroup(streetPopSecList).addTo(mymap);
    isPopulationModeActivated = true;
}

function getHighestPopulationValue(streetdata) {
    let cache = 0;
    streetdata.forEach(data => {
        if (cache < data.count)
            cache = data.count;
    })
    return cache;
}


function convertStreetDataIntoLeafletArray(data) {
    var newArray = [];

    let nodeArray = [];
    nodeArray[0] = data.from.lat;
    nodeArray[1] = data.from.lon;
    newArray.push(nodeArray);

    nodeArray = [];
    nodeArray[0] = data.to.lat;
    nodeArray[1] = data.to.lon;
    newArray.push(nodeArray);

    return newArray
}

var streetPopulationHasBeenCalculatedOnce = false;

function calculatePathCount() {
    streetPopSecList = [];
    if (streetPopulationHasBeenCalculatedOnce) {
        mymap.removeLayer(layerGroupStreetPopulation);
    } else {
        streetPopulationHasBeenCalculatedOnce = true;
    }

    let pathCounts = new Map();

    agents.forEach(agent => {
        let alreadyCapturedEdges = []; //avoiding duplicates
        agent.routeDetails.forEach(routeDetail => {
            if (!alreadyCapturedEdges.includes(routeDetail.edgeId)) {
                if (!pathCounts.has(routeDetail.edgeId)) {
                    let pathCountObject = {
                        count: 1,
                        from: routeDetail.edgeStart,
                        to: routeDetail.edgeEnd
                    }
                    pathCounts.set(routeDetail.edgeId, pathCountObject);
                } else {
                    let pathCountObject = {
                        count: pathCounts.get(routeDetail.edgeId).count + 1,
                        from: routeDetail.edgeStart,
                        to: routeDetail.edgeEnd
                    }
                    pathCounts.set(routeDetail.edgeId, pathCountObject);
                }
                alreadyCapturedEdges.push(routeDetail.edgeId);
            }
        });
    });

    drawStreetPopulationOnMap(Array.from(pathCounts.values()));
}



