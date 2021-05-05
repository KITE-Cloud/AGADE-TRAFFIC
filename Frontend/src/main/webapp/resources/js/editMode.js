var isActiveEditMode = false;
var velocitySections;
var layerGroupVelocitySections;
var velocitySecList = [];

function editMode() {

    if (!isDemo) {
        if (!isActiveEditMode) {
            isActiveEditMode = true;
            $(".legend").css({"display": "block"});
            //alert("Edit Mode started");
            mymap.dragging.disable();
            mymap.zoomControl.disable();
            mymap.touchZoom.disable();
            mymap.doubleClickZoom.disable();
            mymap.scrollWheelZoom.disable();
            let bounds = mymap.getBounds();
            console.log(bounds);

            $.ajax({
                url: 'getVelocitySections',
                data: {
                    "minLat": bounds.getSouthWest().lat,
                    "minLon": bounds.getSouthWest().lng,
                    "maxLat": bounds.getNorthEast().lat,
                    "maxLon": bounds.getNorthEast().lng
                },
                type: 'GET',
                success: function (data) {
                    velocitySections = data;
                    console.log(velocitySections);
                    processVelocitySectionsData(velocitySections);
                }
            });
        } else {
            isActiveEditMode = false;
            //alert("Edit Mode stopped");
            $(".legend").css({"display": "none"});
            mymap.dragging.enable();
            mymap.touchZoom.enable();
            mymap.doubleClickZoom.enable();
            mymap.scrollWheelZoom.enable();
            mymap.zoomControl.enable();

            if(layerGroupVelocitySections != undefined){
                layerGroupVelocitySections.clearLayers();
            }
            velocitySecList = [];
        }
    } else {
        openModalNotification("This option is not available in the AGADE Traffic demo. See Github for installing AGADE Traffic on your local machine.");
    }
}


function processVelocitySectionsData(v) {

    for (var i = 0; i < v.length; i++) {
        var velocitySec = v[i];
        var polyline;
        if (velocitySec.velocity <= 10) {
            polyline = L.polyline(nodesInSectionToArray(velocitySec.nodesInSection), {color: '#ff665f'});
        } else if (velocitySec.velocity <= 20) {
            polyline = L.polyline(nodesInSectionToArray(velocitySec.nodesInSection), {color: '#600917'});
        } else if (velocitySec.velocity <= 30) {
            polyline = L.polyline(nodesInSectionToArray(velocitySec.nodesInSection), {color: '#19d800'});
        } else if (velocitySec.velocity <= 50) {
            polyline = L.polyline(nodesInSectionToArray(velocitySec.nodesInSection), {color: '#0a6000'});
        } else if (velocitySec.velocity <= 60) {
            polyline = L.polyline(nodesInSectionToArray(velocitySec.nodesInSection), {color: '#ffe900'});
        } else if (velocitySec.velocity <= 70) {
            polyline = L.polyline(nodesInSectionToArray(velocitySec.nodesInSection), {color: '#fc8a00'});
        } else if (velocitySec.velocity <= 80) {
            polyline = L.polyline(nodesInSectionToArray(velocitySec.nodesInSection), {color: '#8e4e00'});
        } else if (velocitySec.velocity <= 100) {
            polyline = L.polyline(nodesInSectionToArray(velocitySec.nodesInSection), {color: '#44bdff'});
        } else if (velocitySec.velocity <= 120) {
            polyline = L.polyline(nodesInSectionToArray(velocitySec.nodesInSection), {color: '#0053e0'});
        } else {
            polyline = L.polyline(nodesInSectionToArray(velocitySec.nodesInSection), {color: '#041b77'});
        }

        velocitySecList.push(polyline);
    }

    layerGroupVelocitySections = L.layerGroup(velocitySecList).addTo(mymap);
}


function nodesInSectionToArray(nodesInSec) {
    var newArray = [];

    for (node of nodesInSec) {
        var nodeArr = [];
        nodeArr[0] = node.lat;
        nodeArr[1] = node.lon;
        newArray.push(nodeArr);
    }

    return newArray
}




