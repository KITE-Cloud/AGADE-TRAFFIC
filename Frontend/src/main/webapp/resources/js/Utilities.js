var json;
$(document).ready(function () {
    document.getElementById("filesPopulation").disabled = true;
});


function downloadConfigJson() {
    let exportObj = createConfigObject();
    let exportName = "Configuration";
    let dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(exportObj));
    let downloadAnchorNode = document.createElement('a');
    downloadAnchorNode.setAttribute("href", dataStr);
    downloadAnchorNode.setAttribute("download", exportName + ".json");
    document.body.appendChild(downloadAnchorNode); // required for firefox
    downloadAnchorNode.click();
    downloadAnchorNode.remove();
}


function createConfigObject() {
    let configObj = {
        destinationsArray: destinationsArray,
        dumpDestionation: dumpDestionation,
        destinationMatrix: destinationMatrix,
        desiredArrivalTimes: desiredArrivalTimes,
        personaProfiles: personaProfiles
    };

    return configObj;
}

/*################# FILE SELECT IMPORTS #########################*/
function handleFileSelect(evt) {
    let files = evt.target.files; // FileList object

    // files is a FileList of File objects. List some properties.
    for (let i = 0, f; f = files[i]; i++) {
        let reader = new FileReader();

        // Closure to capture the file information.
        reader.onload = (function (theFile) {
            return function (e) {
                console.log('e readAsText = ', e);
                console.log('e readAsText target = ', e.target);
                try {
                    json = JSON.parse(e.target.result);
                    let modal = document.getElementById('myModalOpen');
                    modal.style.display = "none";
                    initConfig(json);
                    document.getElementById("filesPopulation").disabled = false;
                    //document.title = "This is the newpage title.";
                    openModalNotification("Your simulation configuration has been successfully imported and initialised.")
                } catch (ex) {
                    openModalNotification('error when trying to parse json = ' + ex);
                }
            }
        })(f);
        reader.readAsText(f);
    }
}

document.getElementById('files').addEventListener('change', handleFileSelect, false);


function handlePopulationFileSelect(evt) {
    let files = evt.target.files; // FileList object

    // files is a FileList of File objects. List some properties.
    for (let i = 0, f; f = files[i]; i++) {
        let reader = new FileReader();

        // Closure to capture the file information.
        reader.onload = (function (theFile) {
            return function (e) {
                console.log('e readAsText = ', e);
                console.log('e readAsText target = ', e.target);
                try {
                    json = JSON.parse(e.target.result);
                    let modal = document.getElementById('myModalOpen');
                    modal.style.display = "none";

                    population = json;

                    //document.title = "This is the newpage title.";
                    openModalNotification(population.agents.length + " agents have been successfully imported from your population.")
                } catch (ex) {
                    openModalNotification('error when trying to parse json = ' + ex);
                }
            }
        })(f);
        reader.readAsText(f);
    }
}

document.getElementById('filesPopulation').addEventListener('change', handlePopulationFileSelect, false);


function handleResultFileSelect(evt) {
    let files = evt.target.files; // FileList object
    openLoadingScreen("Processing")
    // files is a FileList of File objects. List some properties.
    for (let i = 0, f; f = files[i]; i++) {

        let reader = new FileReader();

        // Closure to capture the file information.
        reader.onload = (function (theFile) {
            return function (e) {
                console.log('e readAsText = ', e);
                console.log('e readAsText target = ', e.target);
                try {
                    json = JSON.parse(e.target.result);
                    let modal = document.getElementById('myModalOpen');
                    modal.style.display = "none";
                    simulation = json;
                    processSimulationJson(simulation.agents);
                    closeLoadingScreen()
                    if (agents.length > thresholdClusterAnimation) {
                        openModalNotification("Your simulation file has been successfully imported and initialised <br> Cluster animation mode activated as number of agents is too large.")
                    } else {
                        openModalNotification("Your simulation file has been successfully imported and initialised.");
                    }

                } catch (ex) {
                    closeLoadingScreen()
                    openModalNotification('error when trying to parse json = ' + ex);
                }
            }
        })(f);
        reader.readAsText(f);
    }
}

document.getElementById('fileSimResult').addEventListener('change', handleResultFileSelect, false);

/*################# FILE SELECT IMPORTS END #########################*/


/*################# FILE SELECT COMPARE #########################*/

function handleCompConfigFileSelect(evt) {
    let files = evt.target.files; // FileList object

    // files is a FileList of File objects. List some properties.
    for (let i = 0, f; f = files[i]; i++) {
        let reader = new FileReader();

        // Closure to capture the file information.
        reader.onload = (function (theFile) {
            return function (e) {
                console.log('e readAsText = ', e);
                console.log('e readAsText target = ', e.target);
                try {
                    json = JSON.parse(e.target.result);
                    compConfig = json;
                    openModalNotification("Your simulation configuration has been successfully imported for comparison.")
                } catch (ex) {
                    openModalNotification('error when trying to parse json = ' + ex);
                }
            }
        })(f);
        reader.readAsText(f);
    }
}

document.getElementById('fileCompConfig').addEventListener('change', handleCompConfigFileSelect, false);


function handleCompPopulationFileSelect(evt) {
    let files = evt.target.files; // FileList object

    // files is a FileList of File objects. List some properties.
    for (let i = 0, f; f = files[i]; i++) {
        let reader = new FileReader();

        // Closure to capture the file information.
        reader.onload = (function (theFile) {
            return function (e) {
                console.log('e readAsText = ', e);
                console.log('e readAsText target = ', e.target);
                try {
                    json = JSON.parse(e.target.result);
                    compPopulation = json;
                    openModalNotification(population.agents.length + " agents have been successfully imported for comparison.")
                } catch (ex) {
                    openModalNotification('error when trying to parse json = ' + ex);
                }
            }
        })(f);
        reader.readAsText(f);
    }
}

document.getElementById('fileCompPopulation').addEventListener('change', handleCompPopulationFileSelect, false);


function handleCompResultFileSelect(evt) {
    let files = evt.target.files; // FileList object
    openLoadingScreen("Processing")
    // files is a FileList of File objects. List some properties.
    for (let i = 0, f; f = files[i]; i++) {

        let reader = new FileReader();

        // Closure to capture the file information.
        reader.onload = (function (theFile) {
            return function (e) {
                console.log('e readAsText = ', e);
                console.log('e readAsText target = ', e.target);
                try {
                    json = JSON.parse(e.target.result);
                    compSimResult = json;
                    closeLoadingScreen()
                    openModalNotification("Your simulation file has been successfully imported for comparison.");

                } catch (ex) {
                    closeLoadingScreen()
                    openModalNotification('error when trying to parse json = ' + ex);
                }
            }
        })(f);
        reader.readAsText(f);
    }
}

document.getElementById('fileCompSimResult').addEventListener('change', handleCompResultFileSelect, false);

/*################# FILE SELECT COMPARE END #########################*/

function fillRandom() {
    //fill Random OD
    $("#dynamicTableContent input").each(function () {
        let str = $(this).attr("id");
        let random1To10 = Math.floor((Math.random() * 10) + 1);
        let res = str.split('-')[1].split(';');
        let from = res[0];
        let to = res[1];

        //only fill fields not on diagonale and for poisson
        if (str.includes('PD') && (from !== to)) {
            document.getElementById(str).value = random1To10;
            writeValueToMatrix(str, random1To10);
        }
    });

    //fill Random Activity
    $("#dynamicTableActivityContent  input").each(function () {
        let id = $(this).attr("id");
        let random1To10 = Math.floor((Math.random() * 10) + 1);

        document.getElementById(id).value = random1To10;
        writeValueToMatrix(id, random1To10);
    });

}

function initConfig(config) {
    //remove old dumpMarker from map
    if (!isDemo) {
        layerGroupDumpMarker.clearLayers();
    }

    dumpDestionation = config.dumpDestionation;
    addMarkerAtPosition(dumpDestionation.location, dumpDestionation.name,
        dumpDestionation.capacity, dumpDestionation.areaForParking,
        true);
    mymap.panTo(new L.LatLng(dumpDestionation.location.lat, dumpDestionation.location.lng));
    mymap.setZoom(13);

    let j = 0;
    destinationsArray = config.destinationsArray;
    for (j = 0; j < destinationsArray.length; j++) {
        addMarkerAtPosition(destinationsArray[j].location, destinationsArray[j].name,
            destinationsArray[j].capacity, destinationsArray[j].areaForParking,
            false, destinationsArray[j].markerPoiType);
    }


    destinationMatrix = config.destinationMatrix;
    desiredArrivalTimes = config.desiredArrivalTimes;

    if (config.hasOwnProperty('personaProfiles')) {
        personaProfiles = config.personaProfiles;
        updatePersonaCards();
    }
}


function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function randomItem(items) {
    return items[Math.floor(Math.random() * items.length)];
}

function removeItemOnce(arr, value) {
    var index = arr.indexOf(value);
    if (index > -1) {
        arr.splice(index, 1);
    }
    return arr;
}


function distFrom(lat1, lng1, lat2, lng2) {
    let earthRadius = 6371000; // meters
    let dLat = toRadians(lat2 - lat1);
    let dLng = toRadians(lng2 - lng1);
    let a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(toRadians(lat1))
        * Math.cos(toRadians(lat2)) * Math.sin(dLng / 2)
        * Math.sin(dLng / 2);
    let c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return earthRadius * c;
}

function toRadians(degrees) {
    let pi = Math.PI;
    return degrees * (pi / 180);
}


function initCompPrefAccs() {
    var comp_acc = document.getElementsByClassName("comp-accordion");
    var comp_i;
    for (comp_i = 0; comp_i < comp_acc.length; comp_i++) {
        comp_acc[comp_i].addEventListener("click", function () {
            this.classList.toggle("comp-active");
            let panel = this.nextElementSibling;
            let agentID = this.innerHTML;
            let pcAgent = prefChangedAgents.filter(agent => agent.agentID == agentID)[0];
            let htmlEntry = "<div class='row'><div class='col-4'>Preference</div><div class='col-4'>Current Value</div><div class='col-4'>Comparative Value</div>";

            for (prefIndex in pcAgent.changedPrefs) {
                htmlEntry = htmlEntry + "<div class='col-4'>" + pcAgent.changedPrefs[prefIndex].prefName + "</div>";
                htmlEntry = htmlEntry + "<div class='col-4'>" + pcAgent.changedPrefs[prefIndex].valA + "</div>";
                htmlEntry = htmlEntry + "<div class='col-4'>" + pcAgent.changedPrefs[prefIndex].valB + "</div>";
            }
            htmlEntry = htmlEntry + "</div>";
            panel.innerHTML = htmlEntry;

            if (panel.style.maxHeight) {
                panel.style.maxHeight = null;
            } else {
                panel.style.maxHeight = panel.scrollHeight + "px";
            }
        });
    }
}

function countAgentsPassingAtLocations(agentList) {

    let count = new Map();
    let alreadyCapturedLocations = []; //avoiding duplicates
    for (let index in agentList) {
        let agent = agentList[index];
        let origin = agent.origin;
        let trips = agent.journey.trips;

        for (let tripIndex in trips) {
            let targetLoc = trips[tripIndex].end;
            if (targetLoc.lat !== origin.lat && targetLoc.lon !== origin.lon) {
                if (!alreadyCapturedLocations.includes(targetLoc.locationName)) {
                    count.set(targetLoc.locationName, 1);
                } else {
                    let updatedCount = count.get(targetLoc.locationName) + 1;
                    count.set(targetLoc.locationName, updatedCount);
                }
                alreadyCapturedLocations.push(targetLoc.locationName);
            }
        }
    }
    return count;
}

function calculateAvgAgentUtility(agentList){

    let sum = 0;
    for (let index in agentList) {
        let agent = agentList[index];
        if(agent.journey.totalUtilityScore){
            sum = sum + agent.journey.totalUtilityScore;
        }
    }

    return parseFloat(sum/agentList.length).toFixed(4);
}