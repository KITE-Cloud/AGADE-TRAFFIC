//////////////////////////////////////////////SIMULATION.JS///////////////////////////////////////////////////
var agents;
var simulationID = -1;
var simulation = {};
let isNewSimulation = true;
const animationSpeed = 60; // 60 mal Schneller als Realität
var timer = new _timer(function (time) {
});
var lastTickInSimulation = 0;
var isPaused = false;
const tickLength = 60; // steht in AGADE Backend Klasse Client als Konstante
var carList = [];
var layerGroupCars;
var layerGroupDumpMarker;
var layerGroupLocationMarkers;
var locationMarkersList = [];
var layerGroupSupermarketLocationMarkers;
var locationSupermarketMarkersList = [];
var marker;
var markerCluster = L.markerClusterGroup();
var thresholdClusterAnimation = 2000;
var isInitial = true;
var destinationsArray = [];
var destinationMatrix = {};
var dumpDestionation = undefined;
var desiredArrivalTimes = {};
var simulationStatus;
var statusInterval;
var clusterAnimationModeExplained = false;
var timerTick = 0;     //  The default time of the timer
var timerStatus = 0;    //    Status: timer is running or stopped
var timer_id;    //    This is used by setInterval function
var population = {agents: []};
var fromPopulation = false;
var compConfig = {};
var compPopulation = {};
var compSimResult = {};


$("#initializeButton").on("click", function () {
    if (population.agents.length > 0) {
        openModalNotificationPopulation();
    } else {
        triggerBackendCalculations()
    }
});

function triggerBackendCalculations() {
    if (!isDemo) {
        openLoadingScreen("Initialising");

        //if destinationMatrix is empty
        if (JSON.stringify(destinationMatrix) === "{}") {
            fillRandom()
        }

        updateDrawControl();
        let destMatrixJson = JSON.stringify(destinationMatrix);
        let destArrayJson = JSON.stringify(destinationsArray);
        let dumpDestionationJson = JSON.stringify(dumpDestionation);
        let simulationTime = document.getElementById("simulationTimeValue").value;
        let trafficActionObjectsJson = JSON.stringify(trafficActionObjects);
        let desiredArrivaltimesAsJson = JSON.stringify(desiredArrivalTimes);
        let configObj = JSON.stringify(createConfigObject());
        let populationObj = JSON.stringify(population);
        let agentGeneratorType = document.getElementsByClassName("demand tablinks active")[0].name;

        startCheckingSimulationStatus();


        debugger;
        $.ajax({
            url: 'getAgents',
            method: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            mimeType: 'application/json',
            data: JSON.stringify({
                "trafficActionObjects": trafficActionObjectsJson,
                "destinationMatrix": destMatrixJson,
                "destinationArray": destArrayJson,
                "dumpDestination": dumpDestionationJson,
                "simulationTime": simulationTime,
                "isNewSimulation": isNewSimulation,
                "desiredArrivalTimes": desiredArrivaltimesAsJson,
                "configuration": configObj,
                "agentGeneratorType": agentGeneratorType,
                "population": populationObj,
                "fromPopulation": fromPopulation,
            }),
            success: function (data) {
                stopCheckingSimulationStatus();
                simulation = data;
                processSimulationJson(simulation.agents);
                if (fromPopulation) { // simulation has been performed with popuplation. reset population
                    population = {agents: []};
                    fromPopulation = false;
                }
            },
            error: function () {
                stopCheckingSimulationStatus();
                closeLoadingScreen()
                openModalNotification("An error occured. Your simulation could not be completed. Please reboot systems and try again.");
            }
        });
    } else {
        openModalNotification("This option is not available in the AGADE Traffic demo. See Github for installing AGADE Traffic on your local machine.");
    }
}


function startCheckingSimulationStatus() {
    if (!isDemo) {
        if (typeof simulationStatus !== "undefined")
            simulationStatus.simulationIsDone = false;
        let simulationTime = document.getElementById("simulationTimeValue").value;
        let tickToLabel = lastTickInSimulation + (simulationTime * 60 * 60) / 60 - 1;

        console.log("start requesting simulation status")
        statusInterval = window.setInterval(function () {

            $.ajax({
                url: 'getAgents',
                data: {
                    //"isNewSimulation": isNewSimulation,
                },
                type: 'GET',
                success: function (data) {
                    simulationStatus = data;
                    let msgLine2 = "Tick <span id='loaderCurrentTick'>" + simulationStatus.currentTick + "</span> of <span id='loaderLastTick'>" + tickToLabel + "</span>";
                    document.getElementById("preloaderLine1").innerHTML = "Calculating";
                    document.getElementById("preloaderLine2").innerHTML = msgLine2;
                }
            });
        }, 5000);
    }
}


function stopCheckingSimulationStatus() {
    window.clearInterval(statusInterval);
    console.log("stopping simulation status request")
}


$("#goButton").on("click", async function () {
    let tickFrom = document.getElementById("fromTickInput").value;
    if (timerTick === lastTickInSimulation) {
        tickFrom = 0;
    }
    initialize(tickFrom);
    timer.reset(tickFrom);
    timer.stop();


    timer = new _timer(
        function (time) {
            // if (time == 0) {
            //     timer.stop();
            // }
        }
    );
    mymap.removeLayer(markerCluster);
    //mymap.removeLayer(layerGroupStreetPopulation);
    $("path.leaflet-interactive").css("opacity", "0");
    if (typeof layerGroupLocationMarkers !== 'undefined') {
        mymap.removeLayer(layerGroupLocationMarkers);
    }

    layerGroupCars.addTo(mymap);
    isPaused = false;

    if (agents.length <= thresholdClusterAnimation) {
        for (let i = 0; i < carList.length; i++) {
            let currentMarker = layerGroupCars.getLayers()[i];
            if (currentMarker.isEnded() === false)
                currentMarker.start();
        }
        timer.start();
    } else {
        $(".timer").css("background-color", "#80ba24");

        let tickFrom = parseInt(document.getElementById("fromTickInput").value);
        while (tickFrom < lastTickInSimulation && isPaused === false) {
            tickFrom = parseInt(document.getElementById("fromTickInput").value);
            updateTickFromValue(tickFrom);
            await sleep(500);

            tickFrom++;
            document.getElementById("fromTickInput").value = tickFrom;
            $('div.timer').html(tickFrom);
        }

        document.getElementById("pauseButton").click();
        $(".timer").css("background-color", "#2f3c44");

    }
});


$("#pauseButton").on("click", function () {
    isPaused = true;
    timer.stop();
    for (let i = 0; i < carList.length; i++) {
        let currentMarker = layerGroupCars.getLayers()[i];
        if (currentMarker.isEnded() === false)
            currentMarker.pause();
    }
    mymap.removeLayer(layerGroupCars);
    if (typeof layerGroupLocationMarkers !== 'undefined') {
        layerGroupLocationMarkers.addTo(mymap);
    }
    //layerGroupStreetPopulation.addTo(mymap);
    $("path.leaflet-interactive").css("opacity", "1");
    markerCluster.clearLayers();
    markerCluster.addLayers(layerGroupCars).addTo(mymap);
});


function processSimulationJson(agentList) {
    agents = Array.from(agentList);
    simulationID = simulation.simulationID;
    lastTickInSimulation = simulation.currentTick;
    $("#fromTickInput").attr("max", lastTickInSimulation);
    $('link[title="preloaderCSS"]')[0].disabled = true;
    $(".loader").css({"display": "none"});
    isNewSimulation = false;
    calculatePathCount();
    initialize();
    if (agents.length > thresholdClusterAnimation && clusterAnimationModeExplained === false) {
        openModalNotification("Cluster animation mode activated as number of agents is too large.")
        clusterAnimationModeExplained = true;
    }
}


function initialize(tickFrom = 0) {

    for (var tick = tickFrom; tick >= 0; tick--) {
        if (DrawItemsForEachIteration.hasOwnProperty(tick)) {
            mymap.removeLayer(drawnItems);
            drawnItems = DrawItemsForEachIteration[tick];
            mymap.addLayer(drawnItems);
            break;
        }
    }

    if (carList.length !== 0) {
        layerGroupCars.clearLayers();
        carList = [];
        markerCluster.clearLayers();
    }
    //fixme fake public Trans    
    //fakePublicTrans();
    createAgentMarkers(tickFrom);
    layerGroupCars = L.layerGroup(carList)//.addTo(mymap);
    markerCluster.addLayers(layerGroupCars).addTo(mymap);

}


function createAgentMarkers(startTick) {
    for (let i = 0; i < agents.length; i++) {
        if (i == 264) {
            console.log("agent at index:" + i)
        }

        let coordinates = [];
        let vehicles = [];

        for (let j = 0; j < agents[i].routeCoordinates.length; j++) {
            coordinates.push(agents[i].routeCoordinates[j].location);
            vehicles.push(agents[i].routeCoordinates[j].vehicle);
        }

        // add waiting time to routes
        let coordinateArray = coordinates.slice();
        const startLoc = coordinates[0];
        coordinateArray.unshift(startLoc);

        // add waiting time to velocities
        let time = agents[i].velocities.slice(); // copy
        const delayStart = (tickLength * agents[i].birthTick * 1000);
        time.unshift(delayStart);

        // scale velocities to animation speed
        var timeWithAnimationSpeed = [];
        for (let j = 0; j < time.length; j++) {
            timeWithAnimationSpeed[j] = time[j] / animationSpeed;
        }

        // wait at the end
        let simuationEndTime = lastTickInSimulation * tickLength * 1000;
        let sumOfVelocities = 0;
        for (let i = 0; i < agents[0].velocities.length; i++) {
            sumOfVelocities = sumOfVelocities + agents[0].velocities[i]
        }
        let waitingTime = 0;
        if (sumOfVelocities < simuationEndTime) {
            waitingTime = simuationEndTime - sumOfVelocities;
        }
        coordinateArray.push(coordinateArray[coordinateArray.length - 1]);
        timeWithAnimationSpeed.push(waitingTime);


        // manage starting the simulation at an arbitrary tick
        if (startTick > 0) {

            const elapsedTime = (startTick * 60 * 1000) / animationSpeed;

            if (timeWithAnimationSpeed[0] > elapsedTime) {
                timeWithAnimationSpeed[0] -= elapsedTime;
            } else {
                let idxForTick;
                let elapsedTimeHelper = 0;
                let velocityInterpolated;
                let coordinateInterpolated;
                for (let k = 0; k < timeWithAnimationSpeed.length; k++) {
                    if (elapsedTimeHelper + timeWithAnimationSpeed[k] > elapsedTime) {
                        idxForTick = k;
                        if (coordinateArray[k + 1] !== undefined) {
                            const fraction = (elapsedTime - elapsedTimeHelper) / timeWithAnimationSpeed[k];
                            velocityInterpolated = timeWithAnimationSpeed[k] - (elapsedTime - elapsedTimeHelper);
                            coordinateInterpolated = interpolate(coordinateArray[k], coordinateArray[k + 1], fraction);
                        }

                        break;
                    } else {
                        elapsedTimeHelper += timeWithAnimationSpeed[k];
                    }
                }
                coordinateArray = coordinateArray.slice(idxForTick, coordinateArray.length);
                timeWithAnimationSpeed = timeWithAnimationSpeed.slice(idxForTick, timeWithAnimationSpeed.length);
                if (coordinateInterpolated !== undefined) {
                    coordinateArray.unshift(coordinateInterpolated);
                    timeWithAnimationSpeed.unshift(velocityInterpolated);
                }
            }

        }

        //todo me changing marker icon on runtime
        //let myMovingMarker = L.Marker.movingMarker(coordinateArray, time, { icon: carIcon }).on('end', ()=>{ console.log('executing end event'); myMovingMarker.setIcon(carIcon2);});

        let modes = "";
        let journey = "";
        let totalTravDist = -1;
        if (agents[i].journey) {
            for (let mode in agents[i].journey.transportationMode) {
                modes = modes + agents[i].journey.transportationMode[mode] + ";";
            }
            journey = journey + agents[i].journey.trips[0].start.locationName + ' (' + agents[i].journey.trips[0].start.lat + ',' + agents[i].journey.trips[0].start.lon + ')' + '<br>';
            for (let startend in agents[i].journey.trips) {
                journey = journey + agents[i].journey.trips[startend].end.locationName + ' (' + agents[i].journey.trips[startend].end.lat + ',' + agents[i].journey.trips[startend].end.lon + ')' + '<br>';
            }
            totalTravDist = agents[i].journey.totalTravelDistance;
        }

        //create Marker
        var myMovingMarker = L.Marker.movingMarker(coordinateArray, timeWithAnimationSpeed, {
            //autostart: false
        }).bindPopup('<strong>ID:</strong> ' + agents[i].id + '<br>'
            + '<strong>BirthTick:</strong> ' + agents[i].birthTick + '<br>'
            + '<strong>Persona:</strong> ' + agents[i].personaProfile + '<br>'
            + '<strong>Travel Time [s]:</strong> ' + agents[i].totalTravelTime + '<br>'
            + '<strong>Travel Distance [m]:</strong> ' + parseFloat(totalTravDist).toFixed(2) + '<br>'
            + ' <strong>Modes:</strong> ' + modes + '<br>'
            + ' <strong>Journey:</strong><br> ' + journey
        ).on('click', function (e) {
            console.log(e);
        });

        let carIcon;

        let transportationMode = (agents[i].journey) ? agents[i].journey.transportationMode[0] : agents[i].routeHasChanged.entry[0].value.vehicle;
        let iconURL = '';
        let iconCarYellow = 'resources/images/transportMode/car31.svg';
        let iconCarGreen = 'resources/images/transportMode/car41.svg';
        let iconCarRed = 'resources/images/transportMode/car51.svg';
        let iconCarSky = 'resources/images/transportMode/car61.svg';
        let iconCarBlack = 'resources/images/transportMode/car71.svg';
        let iconCarNavy = 'resources/images/transportMode/car81.svg';
        let iconCarGrape = 'resources/images/transportMode/car91.svg';
        let carIcons = [iconCarYellow, iconCarGreen, iconCarRed, iconCarSky, iconCarBlack, iconCarNavy, iconCarGrape];

        let iconBicycle = 'resources/images/transportMode/bicycle.svg';
        let iconPedstrianWoman = 'resources/images/transportMode/man.svg';
        let iconPedestrianMan = 'resources/images/transportMode/woman.svg';


        switch (transportationMode) {
            case 'CAR':
                iconURL = randomItem(carIcons);
                break;
            case 'FEET':
                let rand = Math.ceil(Math.random() * 10);
                iconURL = ((rand % 2 == 0) ? iconPedstrianWoman : iconPedestrianMan)
                break;
            case 'BIKE':
                iconURL = iconBicycle;
                break;
            default:
                iconURL = iconCarYellow;
                break;
        }

        carIcon = L.icon({
            iconUrl: iconURL, iconSize: [25, 25]
        });

        myMovingMarker.options.icon = carIcon;
        myMovingMarker.options.id = agents[i].id;

        carList.push(myMovingMarker);
        marker = myMovingMarker;
    }
}


function interpolate(a, b, frac) {
    const newLat = a.lat + (b.lat - a.lat) * frac;
    const newLon = a.lon + (b.lon - a.lon) * frac;
    return {lat: newLat, lon: newLon};
}


function _timer(callback) {

    // this will start the timer ex. start the timer
    this.start = function (interval) {
        interval = (typeof (interval) !== 'undefined') ? interval : (60000 / animationSpeed);
        $(".timer").css("background-color", "#80ba24");


        if (timerStatus === 0) {
            timerStatus = 1;
            timer_id = setInterval(function () {
                timerTick++;
                generateTime();
                if (typeof (callback) === 'function') callback(timerTick);
                if (timerTick === lastTickInSimulation) {
                    timer.stop();
                    document.getElementById("pauseButton").click()
                }
            }, interval);
        }
    };

    //  Same as the name, this will stop or pause the timer ex. timer.stop()
    this.stop = function () {
        if (timerStatus === 1) {
            timerStatus = 0;
            clearInterval(timer_id);
            $(".timer").css("background-color", "#2f3c44");
        }
    };

    // Reset the timer to zero or reset it to your own custom time ex. reset to zero second timer.reset(0)
    this.reset = function (tickBegin) {
        timerTick = tickBegin;
        generateTime(timerTick);
    };

    // This methode return the current value of the timer
    this.getTime = function () {
        return timerTick;
    };

    // This methode return the status of the timer running (1) or stoped (1)
    this.getStatus = function () {
        return timerStatus;
    };

    // This methode will render the time variable to hour:minute:second format
    function generateTime() {
        $("#fromTickInput").val(timerTick);
        $('div.timer').html(timerTick);
    }
}