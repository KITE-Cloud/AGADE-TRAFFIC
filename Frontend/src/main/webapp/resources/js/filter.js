var acc = document.getElementsByClassName("accordion");
var agentIDFilterList = [];

var vehicleOptions = ["CAR", "BIKE", "FEET", "PUBLICTRANSPORT"]

for (let i = 0; i < acc.length; i++) {
    acc[i].addEventListener("click", openFilterAccordion);
}

function openFilterAccordion(accElement) {
    if (accElement) {
        document.getElementsByClassName("accordion")[0].classList.toggle("active");
        var panel = document.getElementsByClassName("accordion")[0].nextElementSibling;
    } else {
        this.classList.toggle("active");
        var panel = this.nextElementSibling;
    }


    if (panel.style.display === "block") {
        panel.style.display = "none";
    } else {
        createPersonaFilters();
        createOriginFilters();
        createTargetLocationFilters();
        panel.style.display = "block";
    }
}

function createPersonaFilters() {
    let agentFilterPersonaOptions = '<p>Persona</p><div class="row">';
    for (let item in personaProfiles) {
        agentFilterPersonaOptions = agentFilterPersonaOptions + '<div class="col-12 col-sm-6 col-4 col-md-3  col-lg-2">' +
            '<input type="checkbox" id="personaFilter' + personaProfiles[item].name + '" name="' + personaProfiles[item].name + '" value="' + personaProfiles[item].name + '">' +
            '<label for="' + personaProfiles[item].name + '">' + personaProfiles[item].name + '</label>' +
            '</div>';
    }
    agentFilterPersonaOptions = agentFilterPersonaOptions + '</div>'
    $('#agentFilterPersona').html(agentFilterPersonaOptions);
}

function createOriginFilters() {
    let agentFilterOriginOptions = '<p>Origin Location</p><div class="row">';
    for (let item in destinationsArray) {
        agentFilterOriginOptions = agentFilterOriginOptions + '<div class="col-12 col-sm-6 col-4 col-md-3  col-lg-2">' +
            '<input type="checkbox" id="originFilter' + destinationsArray[item].name + '" name="' + destinationsArray[item].name + '" value="' + destinationsArray[item].name + '">' +
            '<label for="' + destinationsArray[item].name + '">' + destinationsArray[item].name + '</label>' +
            '</div>';
    }
    agentFilterOriginOptions = agentFilterOriginOptions + '</div>'
    $('#agentFilterOrigin').html(agentFilterOriginOptions);
}

function createTargetLocationFilters() {
    let agentFilterTargetLocationOptions = '<p>Target Locations</p><div class="row">';
    for (let item in destinationsArray) {
        agentFilterTargetLocationOptions = agentFilterTargetLocationOptions + '<div class="col-12 col-sm-6 col-4 col-md-3  col-lg-2">' +
            '<input type="checkbox" id="targetLocationFilter' + destinationsArray[item].name + '" name="' + destinationsArray[item].name + '" value="' + destinationsArray[item].name + '">' +
            '<label for="' + destinationsArray[item].name + '">' + destinationsArray[item].name + '</label>' +
            '</div>';
    }
    agentFilterTargetLocationOptions = agentFilterTargetLocationOptions + '</div>'
    $('#agentFilterTargetLocation').html(agentFilterTargetLocationOptions);
}


function addToAgentIDFilterList() {
    let val = document.getElementById('agentFilterIDInput').value;
    $('#agentFilterIDError').html("");
    if (val != "" && val >= 0 && val < simulation.agents.length && !agentIDFilterList.includes(val)) {
        agentIDFilterList.push(val)
        updateAgentIDFilterTags()
        document.getElementById('agentFilterIDInput').value = "";
    } else {
        $('#agentFilterIDError').html("Agent ID not found.")
    }
}

function removeFromAgentIDFilterList(id) {
    removeItemOnce(agentIDFilterList, id)
    updateAgentIDFilterTags()
}

function updateAgentIDFilterTags() {
    $('#agentFilterIDTags').empty();
    for (item in agentIDFilterList) {
        let tag = '<span class="personaPropTag" style="display:inline-block;"><span><img class="filterDelete" onclick="removeFromAgentIDFilterList(this.name)" src="resources/images/icons/kite4.png"  height=20 name="' + agentIDFilterList[item] + '" /></span><span>AgentID: ' + agentIDFilterList[item] + '</span></span>'
        $('#agentFilterIDTags').append(tag);
    }
}

$("#agentFilterIDInput").on('keyup', function (e) {
    if (e.keyCode == 13) {
        addToAgentIDFilterList()
    }
});


function applyFiltersToVisualisation(reset) {
    let personaFilterList = [];
    let originLocationFilterList = [];
    let targetLocationFilterList = [];
    let vehicleFilterList = [];
    let filteredAgentList = [];

    if (reset) {
        filteredAgentList = Array.from(simulation.agents);
    } else {
        for (let item in personaProfiles) {
            let id = "personaFilter" + personaProfiles[item].name;
            if (document.getElementById(id).checked == true) {
                personaFilterList.push(document.getElementById(id).name)
            }
        }

        for (let item in destinationsArray) {
            let id = "targetLocationFilter" + destinationsArray[item].name;
            if (document.getElementById(id).checked == true) {
                targetLocationFilterList.push(document.getElementById(id).name)
            }
            id = "originFilter" + destinationsArray[item].name;
            if (document.getElementById(id).checked == true) {
                originLocationFilterList.push(document.getElementById(id).name)
            }
        }

        for (let item in vehicleOptions) {
            let id = "targetMode" + vehicleOptions[item];
            if (document.getElementById(id).checked == true) {
                vehicleFilterList.push(document.getElementById(id).name)
            }
        }

        for (agent in simulation.agents) {
            if (agentIDFilterList.includes(simulation.agents[agent].id.toString()) ||
                personaFilterList.includes(simulation.agents[agent].personaProfile) ||
                originLocationFilterList.includes(simulation.agents[agent].origin.locationName)
            ) {
                filteredAgentList.push(simulation.agents[agent]);
            }

            if (simulation.agents[agent].journey) {
                if (vehicleFilterList.includes(simulation.agents[agent].journey.transportationMode[0]) && !filteredAgentList.includes(simulation.agents[agent])) {
                    filteredAgentList.push(simulation.agents[agent]);
                }
                for (trip in simulation.agents[agent].journey.trips) {
                    let currentStartEnd = simulation.agents[agent].journey.trips[trip];
                    let curentTargetLocation = currentStartEnd.end;
                    if (targetLocationFilterList.includes(curentTargetLocation) && !filteredAgentList.includes(simulation.agents[agent])) {
                        filteredAgentList.push(filteredAgentList.push(simulation.agents[agent]));
                    }
                }
            }
        }
    }

    //peform filtering
    agents = filteredAgentList;
    processSimulationJson(filteredAgentList);
    document.getElementsByClassName("accordion")[0].nextElementSibling.style.display = "none";
    openModalNotification("Number of filtered Agents: " + filteredAgentList.length + " of " + simulation.agents.length)
}


