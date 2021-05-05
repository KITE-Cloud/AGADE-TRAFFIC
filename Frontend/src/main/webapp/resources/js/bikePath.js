var isActiveBikeMode = false;
var bikeSections;
let layerGroupBikeSections;
let bikeSecList = [];

function bikeMode() {
    if (!isDemo) {
        if (!isActiveBikeMode) {
            isActiveBikeMode = true;
            //alert("Edit Mode started");
            mymap.dragging.disable();
            mymap.zoomControl.disable();
            mymap.touchZoom.disable();
            mymap.doubleClickZoom.disable();
            mymap.scrollWheelZoom.disable();
            let bounds = mymap.getBounds();
            console.log(bounds);


            $.ajax({
                url: 'getBikePaths',
                data: {
                    "minLat": bounds.getSouthWest().lat,
                    "minLon": bounds.getSouthWest().lng,
                    "maxLat": bounds.getNorthEast().lat,
                    "maxLon": bounds.getNorthEast().lng
                },
                type: 'GET',
                success: function (data) {
                    bikeSections = data;
                    console.log(bikeSections);
                    processBikePathData(bikeSections);
                }
            });
        } else {
            isActiveBikeMode = false;
            //alert("Edit Mode stopped");
            mymap.dragging.enable();
            mymap.touchZoom.enable();
            mymap.doubleClickZoom.enable();
            mymap.scrollWheelZoom.enable();
            mymap.zoomControl.enable();

            layerGroupBikeSections.clearLayers();
            bikeSecList = [];
        }
    } else {
        openModalNotification("This option is not available in the AGADE Traffic demo. See Github for installing AGADE Traffic on your local machine.");
    }

}


function processBikePathData(v) {
    for (let i = 0; i < v.length; i++) {
        let bikePath = v[i];
        let polyline;

        polyline = L.polyline(nodesInSectionToArray(bikePath.nodesInSection), {color: '#ff665f'});

        bikeSecList.push(polyline);
    }

    layerGroupBikeSections = L.layerGroup(bikeSecList).addTo(mymap);

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




