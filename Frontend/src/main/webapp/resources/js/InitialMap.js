var anzplaces = 0;
var destinationSize = 1;
var trafficActionObjects = {};
var trafficActionCount = 0;
var drawLayer = [];
var currentPolyline = [];
//var initalMapLocation = [50.8701, 9.7020]; //bad hersfeld
//var initalMapLocation = [50.3316586,8.7598337]; //friedberg
var initalMapLocation = [50.552243, 8.497667]; //Wetzlar


var DrawItemsForEachIteration = [];


/*Initialize Leaflet Map*/
var mymap = L.map('mapid').setView(initalMapLocation, 15);

L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager_labels_under/{z}/{x}/{y}{r}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/attributions">CARTO</a>',
    subdomains: 'abcd',
    maxZoom: 19
}).addTo(mymap);

createDumpLocation();


function createDumpLocation() {
    if (!isDemo) {
        $.ajax({
            url: 'getNearest',
            data: {"lat": initalMapLocation[0], "lon": initalMapLocation[1] + 0.01},
            type: 'GET',
            success: function (data) {
                console.log(data);
                var resultLoc = data;
                addMarkerAtPosition({lat: resultLoc.lat, lng: resultLoc.lon}, "DUMP", 0, 0, true);
            },
            error: function () {
                openModalNotification("Error mapping the location with Neo4j");
            }
        });
    }
}


function addMarker(e) {

    console.log(e)

    let markerName = $('#markerName').val();
    let markerCapacity = $('#markerCapacity').val();
    let markerAreaForParking = $('#markerParkingArea').val();
    let markerPoiType = $('#markerPoiTypeSelect').val();

    if (markerName == null || markerName == "") {
        // alert("Prompt was cancelled.");
    } else {
        coordLng = e.layer._latlng.lng;
        coordLat = e.layer._latlng.lat;

        if (!isDemo) {
            $.ajax({
                url: 'getNearest',
                data: {"lat": coordLat, "lon": coordLng},
                type: 'GET',
                success: function (data) {
                    console.log(data);
                    var resultLoc = data;
                    e.layer._latlng.lng = resultLoc.lon;
                    e.layer._latlng.lat = resultLoc.lat;
                    addMarkerAtPosition(e.layer._latlng, markerName, markerCapacity, markerAreaForParking, false, markerPoiType);
                },
                error: function () {
                    openModalNotification("Error mapping the location with Neo4j");
                }
            });
        } else {
            //add to map without backend connection in demo
            addMarkerAtPosition(e.layer._latlng, markerName, markerCapacity, markerAreaForParking, false, markerPoiType);
        }

    }
}


function addMarkerAtPosition(loc, nameS, capacity, areaForParking, isDump, markerPoiType) {

    if (isDump) {
        var dumpMarker = L.marker(loc).bindPopup('<strong>Location:</strong>' + ' ' + nameS + ' <br>' + '<strong>Lat:</strong>' + ' ' + loc.lat + ' <br>' + '<strong>Lon:</strong>' + ' ' + loc.lng).on('click', function (e) {
            selectMarker(e);
        });

        let icon = L.icon({
            iconUrl: 'resources/images/icons/marker-icon-grey.png',
            iconSize: [25, 41],
            iconAnchor: [12, 41],
            popupAnchor: [0, -41]
        });

        dumpMarker.options.icon = icon;
        dumpMarker.options.id = -1; //todo so kann man eine ID im Marker definieren -> access: e.target.options.id

        layerGroupDumpMarker = L.layerGroup([dumpMarker]).addTo(mymap)

        let place = {name: "DUMP", location: loc, capacity: 0, areaForParking: 0, markerPoiType: "Location"};
        dumpDestionation = place;
    } else {
        var newMarker = new L.marker(loc).bindPopup('<strong>Location:</strong>' + ' ' + nameS + ' <br>' + '<strong>PoiType:</strong>' + ' ' + markerPoiType + ' <br>' + '<strong>Lat:</strong>' + ' ' + loc.lat + ' <br>' + '<strong>Lon:</strong>' + ' ' + loc.lng).on('click', function (e) {
            selectMarker(e);
        });

        newMarker.options.id = uuidv4();


        //todo supermakert icons?
        if (poiTypeIsSupermarket(markerPoiType)) {

            let icon = L.icon({
                iconUrl: 'resources/images/icons/supermarket.svg',
                iconSize: [25, 31],
                iconAnchor: [12, 31],
                popupAnchor: [0, -31]
            });
            newMarker.options.icon = icon;

            if (locationSupermarketMarkersList.length > 0) {
                locationSupermarketMarkersList.push(newMarker);
                layerGroupSupermarketLocationMarkers.addLayer(newMarker);
            } else {
                locationSupermarketMarkersList.push(newMarker);
                layerGroupSupermarketLocationMarkers = L.layerGroup(locationSupermarketMarkersList).addTo(mymap)
            }
        } else {
            if (locationMarkersList.length > 0) {
                locationMarkersList.push(newMarker);
                layerGroupLocationMarkers.addLayer(newMarker);
            } else {
                locationMarkersList.push(newMarker);
                layerGroupLocationMarkers = L.layerGroup(locationMarkersList).addTo(mymap)
            }
        }


        var place = {
            name: nameS,
            location: loc,
            capacity: capacity,
            areaForParking: areaForParking,
            markerPoiType: markerPoiType
        };
        destinationsArray[anzplaces] = place;
        anzplaces++;
        destinationSize++;
    }
}

function markerIsSupermarket(m) {
    let list = destinationsArray.filter(destination => (destination.location.lat == m.latlng.lat && destination.location.lng == m.latlng.lng));
    let markerPoiType = list[0].markerPoiType;
    return poiTypeIsSupermarket(markerPoiType)
}

function poiTypeIsSupermarket(markerPoiType) {
    if (markerPoiType === "Delicacies_Store" || markerPoiType === "Overseas_Store" || markerPoiType === "Supermarket" ||
        markerPoiType === "Bakery" || markerPoiType === "Bio_Store" || markerPoiType === "Discounter" || markerPoiType === "Local_Vegetables_Store") {
        return true;
    } else {
        return false;
    }
}

function clearMap() {
    anzplaces = 0;
    mymap.eachLayer(function (layer) {
        mymap.removeLayer(layer)
    });
    $('.mapContainer').remove();
    $('.main').prepend('<div class="mapContainer" style = "padding: 0em; height: 100%;"><div id="mapid" style=" height: 100vh; width: 100%; padding: 0em;"></div><div id="search"><input type="text" name="addr" value="" id="addr" size="10" placeholder="Enter Location" /> <button type="button" onclick="addr_search();" class="buttonSide" style="width: 51px; height: 20px; font-size: 13px; margin: 0px;">Search</button><div id="results" style="margin-right: 35px;"/></div></div>');

    /*Re-Initialise Map*/
    mymap = L.map('mapid').setView(initalMapLocation, 17);

    L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
        maxZoom: 18,
        attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
            '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
            'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
        id: 'mapbox.streets'
    }).addTo(mymap);

    mymap.on('click', addMarker);

    //Reset Destination Matrix
    const tab = document.getElementById("myTable");
    const rowList = tab.getElementsByTagName("tr");

    rowList[1].getElementsByTagName("h7")[0].innerHTML = "Source 1:";
    rowList[0].getElementsByTagName("h7")[0].innerHTML = "Dest. 1:";

    var anzD = 0;
    do {
        var heads = rowList[0];
        var headList = heads.getElementsByTagName("th");
        for (let i = 2; i < headList.length; i++) {
            heads.deleteCell(i);
        }

        for (let r = 1; r < rowList.length; r++) {
            var row = rowList[r];
            var dataList = row.getElementsByTagName("td");

            if (row.id == "initRow") {
                for (let i = 1; i < dataList.length; i++) {
                    row.deleteCell(i);
                }
            } else {
                tab.deleteRow(r);
            }
        }
        anzD++;
    } while (anzD <= 10);
    //reset = true;
    destinationSize = 1;
    destinationMatrix = {};
    destinationsArray = []
    isInitial = true;

}

/*#################### draw start###############################*/
// FeatureGroup is to store editable layers
var drawnItems = new L.FeatureGroup(drawLayer);
mymap.addLayer(drawnItems);
var drawControl = new L.Control.Draw({
    draw: {
        circle: false,
        rectangle: false,
        circlemarker: false,
        polygon: false
    },
    edit: {
        featureGroup: drawnItems,
        remove: true,
        edit: false
    }
});
mymap.addControl(drawControl);

$(".leaflet-draw-draw-polyline").html('<span class="sr-only">Traffic Signs</span>');
$(".leaflet-draw-draw-polyline").attr('title', 'Traffic Signs');


mymap.on(L.Draw.Event.CREATED, function (e) {
    var type = e.layerType,
        layer = e.layer;
    if (type === 'marker') {
        // Do marker specific actions
        openModalMarkerProps(e);
        //addMarker(e);

    } else if (type === 'polyline') {
        // mymap.addLayer(layer);
        drawLayer.push(layer);
        //popup to select choices
        currentPolyline = e.layer._latlngs;
        modalTrafficAction();
        updateDrawControl();
    } /*else if (type === 'polygon') {
        //todo do something with polygon
        mymap.addLayer(layer);
    }*/
});


function updateDrawControl() {
    mymap.removeLayer(drawnItems);
    mymap.removeControl(drawControl);
    drawnItems = new L.FeatureGroup(drawLayer);
    mymap.addLayer(drawnItems);
    drawControl = new L.Control.Draw({
        draw: {
            circle: false,
            rectangle: false,
            circlemarker: false,
            polygon: false
        },
        edit: {
            featureGroup: drawnItems,
            remove: true,
            edit: false
        }
    });
    mymap.addControl(drawControl);
    DrawItemsForEachIteration[lastTickInSimulation] = drawnItems;
    $(".leaflet-draw-draw-polyline").html('<span class="sr-only">Traffic Signs</span>');
    $(".leaflet-draw-draw-polyline").attr('title', 'Traffic Signs');
}

mymap.on(L.Draw.Event.DELETED, function (e) {
    layers = e.layers._layers;

    for (var layer in layers) {
        deleteByValue(layers[layer]);
    }
});


function deleteByValue(val) {
    for (var f in trafficActionObjects) {
        if (val.editing.hasOwnProperty("latlngs")) {
            if (trafficActionObjects[f].locations === val.editing.latlngs[0]) {
                delete trafficActionObjects[f];
            }
        } else if (val.editing.hasOwnProperty("marker")) {
            if (trafficActionObjects[f].locations === val.editing._marker.latlngs[0]) {
                delete trafficActionObjects[f];
            }
        }
    }
    for (var f in drawLayer) {
        for (var i = drawLayer.length - 1; i--;) {
            if (drawLayer[f] === val) {
                if (drawLayer.length === 2) {
                    drawLayer.length = [];
                } else
                    drawLayer.splice(i, 1);
            }
        }
    }
}

$('body').on('DOMSubtreeModified', '#fromTickLabel', function () {
    var currentTick = $('#fromTickInput').val();
    if (DrawItemsForEachIteration.hasOwnProperty(currentTick)) {
        mymap.removeLayer(drawnItems);
        drawnItems = DrawItemsForEachIteration[currentTick];
        mymap.addLayer(drawnItems);
    }

});

/*#################### draw end###############################*/


