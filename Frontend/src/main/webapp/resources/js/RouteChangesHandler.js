var layerGroupRouteChanges;
var routeChangesMarkerList = [];
var isRouteChangeModeActivated = false;
let usedLocations = [];

function routeChangeMode() {

    if (!isRouteChangeModeActivated) {
        isRouteChangeModeActivated = true;

        requestRouteChanges();
    } else {
        isRouteChangeModeActivated = false;

        layerGroupRouteChanges.clearLayers();
        routeChangesMarkerList = [];
        usedLocations = [];
    }

}


function requestRouteChanges() {

    let Changeicon = L.Icon.extend({
        options: {
            iconSize: [20, 20]
        }
    });

    let changeIcon = new Changeicon({
        iconUrl: '../resources/images/icons/change.png'
    });

    let agents = simulation.agents;


    agents.forEach(agent => {
        if (agent.origin.lat === selectedMarkers[0].latlng.lat && agent.origin.lon === selectedMarkers[0].latlng.lng &&
            agent.destination.lat === selectedMarkers[1].latlng.lat && agent.destination.lon === selectedMarkers[1].latlng.lng) {
            for (let key in agent.routeHasChanged) {

                let routeChangeBean = agent.routeHasChanged[key][0].value;

                if (routeChangeBean.hasChanged &&
                    !usedLocations.filter(usedLocation => {
                            return (
                                usedLocation.lat === routeChangeBean.location.lat &&
                                usedLocation.lon === routeChangeBean.location.lon
                            )
                        }
                    ).length > 0
                ) {
                    let marker = L.marker([Number(routeChangeBean.location.lat), Number(routeChangeBean.location.lon)], {icon: changeIcon}).bindPopup("Route Change: \nVehicle: " + routeChangeBean.vehicle);

                    routeChangesMarkerList.push(marker);

                    usedLocations.push({lat: routeChangeBean.location.lat, lon: routeChangeBean.location.lon});
                }

            }
        }
    });

    layerGroupRouteChanges = L.layerGroup(routeChangesMarkerList).addTo(mymap);
}
