function modalStatistics() {
    // Get the modal
    let modal = document.getElementById('myModal');
    // Get the <span> element that closes the modal
    let span = document.getElementsByClassName("closeStatistics")[0];


    if (simulation.agents) {
        $('#statisticTotalNumberOfAgents').html('Number of Agents: ' + agents.length);
        $('#statisticCombustionDist').html('Combustion Distance [km]: ' + calculateCombustionKilometers(agents));
        $('#statisticTotalTravelDist').html('Global Travel Distance [km]: ' + calculateGlobalTravelKilometers(agents));
        $('#statisticAvgTravellerUtility').html('Avg. Traveller Utility: ' + calculateAvgAgentUtility(agents));
    }

    modal.style.display = "block";

    // When the user clicks on <span> (x), close the modal
    span.onclick = function () {
        modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
}

function modalCompare() {
    // Get the modal
    let modal = document.getElementById('myModalCompare');
    // Get the <span> element that closes the modal
    let span = document.getElementsByClassName("closeCompareModal")[0];

    $('#compATotalNumberOfAgents').html(simulation.agents.length);
    $('#compACombustionDist').html(calculateCombustionKilometers(simulation.agents));
    $('#compATotalTravelDist').html(calculateGlobalTravelKilometers(simulation.agents));
    $('#compBTotalNumberOfAgents').html(compSimResult.agents.length);
    $('#compAAvgTravellerUtility').html(calculateAvgAgentUtility(simulation.agents));
    $('#compBCombustionDist').html(calculateCombustionKilometers(compSimResult.agents));
    $('#compBTotalTravelDist').html(calculateGlobalTravelKilometers(compSimResult.agents));
    $('#compBAvgTravellerUtility').html(calculateAvgAgentUtility(compSimResult.agents));


    /* compare preferences*/
    let prefChangedAgents = examineAgentsForChangedPrefs(population.agents, compPopulation.agents);
    $('#compNumAgentsChangedPref').html(prefChangedAgents.length);

    let acchtml = "";
    let countPrefChangesMap = {};
    for (agentIndex in prefChangedAgents) {
        prefChangedAgents[agentIndex].changedPrefs.forEach(pref => {
            let key = pref.prefName + "<br/> Value A:" + pref.valA + " | Value B:" + pref.valB;
            if (!countPrefChangesMap.hasOwnProperty(key)) {
                countPrefChangesMap[key] = 1;
            } else {
                let count = countPrefChangesMap[key] + 1;
                countPrefChangesMap[key] = count;
            }
        })
    }
    for (let key in countPrefChangesMap) {
        acchtml = acchtml + '<div class="col-6"><button class="comp-accordion">' + key + ' | count:(' + countPrefChangesMap[key] + ')' + '</button><div class="comp-panel"></div></div>'
    }
    //
    $('#prefChangedAgentAcc').html(acchtml);

    /* compare initial tranport modes*/
    examineAgentsForChangedInitMode();
    $('#compNumAgentsChangedInitMode').html(initModeChangedAgents.length);

    acchtml = "";
    let countInitModeChangesMap = {};
    for (agentIndex in initModeChangedAgents) {
        let key = "Value A:" + initModeChangedAgents[agentIndex].changedInitMode[0].valA + " | Value B:" + initModeChangedAgents[agentIndex].changedInitMode[0].valB;
        if (!countInitModeChangesMap.hasOwnProperty(key)) {
            countInitModeChangesMap[key] = 1;
        } else {
            let count = countInitModeChangesMap[key] + 1;
            countInitModeChangesMap[key] = count;
        }
    }
    for (let key in countInitModeChangesMap) {
        acchtml = acchtml + '<div class="col-6"><button class="comp-accordion">' + key + ' | count:(' + countInitModeChangesMap[key] + ')' + '</button><div class="comp-panel"></div></div>'
    }
    $('#initModeChangedAgentAcc').html(acchtml);

    /* compare travel distance*/
    examineAgentsForChangedDist();
    $('#compNumAgentsChangedDist').html(distChangedAgents.length);

    acchtml = "";
    let countDistChangesMap = {};
    for (agentIndex in distChangedAgents) {
        let key = "";
        if (distChangedAgents[agentIndex].changedDist[0].valA < distChangedAgents[agentIndex].changedDist[0].valB) {
            key = "Distance A < Distance B";
        } else {
            key = "Distance A >= Distance B";
        }
        if (!countDistChangesMap.hasOwnProperty(key)) {
            countDistChangesMap[key] = 1;
        } else {
            let count = countDistChangesMap[key] + 1;
            countDistChangesMap[key] = count;
        }
    }
    for (let key in countDistChangesMap) {
        acchtml = acchtml + '<div class="col-6"><button class="comp-accordion">' + key + ' | count:(' + countDistChangesMap[key] + ')' + '</button><div class="comp-panel"></div></div>'
    }
    $('#distChangedAgentAcc').html(acchtml);


    modal.style.display = "block";
    plotCompare();
    plotTargetGraphs()

    // When the user clicks on <span> (x), close the modal
    span.onclick = function () {
        modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
}


function modalSettings() {
    loadActivityTable();
    loadMatrixTable();
    loadDTTSettingsArea();
    //loadAgentTypeSettingsArea();

    let modal = document.getElementById('myModalSettings');
    let span = document.getElementsByClassName("closeSettings")[0];

    modal.style.display = "block";

    span.onclick = function () {
        modal.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
}


function modalOpen() {
    let modal = document.getElementById('myModalOpen');
    let span = document.getElementsByClassName("closeOpenModal")[0];

    modal.style.display = "block";

    span.onclick = function () {
        modal.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
}


function modalTrafficAction() {
    let modal = document.getElementById('myModalTrafficAction');
    modal.style.display = "block";
}


function closeTrafficModal(selectedNames) {
    const classNamesList = selectedNames.split(' ');
    selectedName = classNamesList[0];

    var modal = document.getElementById('myModalTrafficAction');
    modal.style.display = "none";
    var selectedTrafficAction = selectedName;

    var isBlock;
    var changedVelocity;
    var signIcon;
    if (selectedTrafficAction === "to-blocked") {
        isBlock = true;
        changedVelocity = -1;
        signIcon = L.icon({
            iconUrl: 'resources/images/trafficSigns/block.png',
            iconSize: [35, 35], // size of the icon
        });
    } else if (selectedTrafficAction === "to-10") {
        isBlock = false;
        changedVelocity = 10;
        signIcon = L.icon({
            iconUrl: 'resources/images/trafficSigns/10.png',
            iconSize: [35, 35], // size of the icon
        });
    } else if (selectedTrafficAction === "to-20") {
        isBlock = false;
        changedVelocity = 20;
        signIcon = L.icon({
            iconUrl: 'resources/images/trafficSigns/20.png',
            iconSize: [35, 35], // size of the icon
        });
    } else if (selectedTrafficAction === "to-30") {
        isBlock = false;
        changedVelocity = 30;
        signIcon = L.icon({
            iconUrl: 'resources/images/trafficSigns/30.png',
            iconSize: [35, 35], // size of the icon
        });
    } else if (selectedTrafficAction === "to-50") {
        isBlock = false;
        changedVelocity = 50;
        signIcon = L.icon({
            iconUrl: 'resources/images/trafficSigns/50.png',
            iconSize: [35, 35], // size of the icon
        });
    } else if (selectedTrafficAction === "to-60") {
        isBlock = false;
        changedVelocity = 60;
        signIcon = L.icon({
            iconUrl: 'resources/images/trafficSigns/60.png',
            iconSize: [35, 35], // size of the icon
        });
    } else if (selectedTrafficAction === "to-70") {
        isBlock = false;
        changedVelocity = 70;
        signIcon = L.icon({
            iconUrl: 'resources/images/trafficSigns/70.png',
            iconSize: [35, 35], // size of the icon
        });
    } else if (selectedTrafficAction === "to-80") {
        isBlock = false;
        changedVelocity = 80;
        signIcon = L.icon({
            iconUrl: 'resources/images/trafficSigns/80.png',
            iconSize: [35, 35], // size of the icon
        });
    } else if (selectedTrafficAction === "to-90") {
        isBlock = false;
        changedVelocity = 90;
        signIcon = L.icon({
            iconUrl: 'resources/images/trafficSigns/90.png',
            iconSize: [35, 35], // size of the icon
        });
    } else if (selectedTrafficAction === "to-100") {
        isBlock = false;
        changedVelocity = 100;
        signIcon = L.icon({
            iconUrl: 'resources/images/trafficSigns/100.png',
            iconSize: [35, 35], // size of the icon
        });
    } else if (selectedTrafficAction === "to-120") {
        isBlock = false;
        changedVelocity = 120;
        signIcon = L.icon({
            iconUrl: 'resources/images/trafficSigns/120.png',
            iconSize: [35, 35], // size of the icon
        });
    }

    trafficActionObjects[trafficActionCount] = {
        "type": "polyline",
        "block": isBlock,
        maxspeed: changedVelocity,
        locations: currentPolyline
    };
    trafficActionCount++;

    for (i = 0; i < currentPolyline.length; i++) {
        drawLayer.push(new L.marker(currentPolyline[i], {icon: signIcon}));
    }
    updateDrawControl();
}


function modalAvgTime() {

    let modal = document.getElementById('myModalAvgTimeStatistics');
    let span = document.getElementsByClassName("closeAvgTimeModal")[0];

    modal.style.display = "block";

    span.onclick = function () {
        modal.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
}

function openModalDestinationDetails() {

    let modal = document.getElementById('myModalDestinationDetails');
    let span = document.getElementsByClassName("closeMyModalDestinationDetails")[0];

    modal.style.display = "block";

    span.onclick = function () {
        modal.style.display = "none";
    }

    window.onclick = function (event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
}


function openModalMarkerProps(eventObj) {
    let modal = document.getElementById('myModalMarkerProps');

    //initialise Select
    let list = ontologyProps.locationTypes;
    let selection = '<select id="markerPoiTypeSelect" name="markerPoiTypeSelect" style="height: 29px"> ';
    for (let item in list) {
        let val = list[item];
        selection = selection + '<option value="' + val + '">' + val + '</option>';
    }
    selection = selection + '</select>';
    $("#markerPoiType").html(selection);


    //set default values
    $('#markerName').val("");
    $('#markerCapacity').val(1000);
    $('#markerParkingArea').val(0);
    $('#markerPoiTypeSelect').val("Location");

    modal.style.display = "block";
    $('#markerName').focus();

    let markerPropsButton = document.getElementById("markerPropsButton");
    let markerCancelButton = document.getElementById("markerPropsCancelButton");

    let okfunction = () => {
        if ($('#markerName').val() == "") {
            $('#markerName').css("border-color", "red");
        } else {
            addMarker(eventObj);
            modal.style.display = "none";
        }
    };

    let cancelFunction = () => {
        modal.style.display = "none";
    };

    markerPropsButton.onclick = okfunction;
    markerCancelButton.onclick = cancelFunction;


}

$('#markerName').on('keypress', function (e) {
    if (e.which == 13) {
        document.getElementById("markerPropsButton").click()
    }
});

$('#markerCapacity').on('keypress', function (e) {
    if (e.which == 13) {
        document.getElementById("markerPropsButton").click()
    }
});

$('#markerParkingArea').on('keypress', function (e) {
    if (e.which == 13) {
        document.getElementById("markerPropsButton").click()
    }
});


function openModalNotification(message, okFn, buttonsHtml, focusButtonId) {
    let modal = document.getElementById('myModalNotification');
    let buttons;
    if (buttonsHtml) {
        buttons = buttonsHtml;

    } else {
        buttons = '<button id="notificationModalOKButton" class="button control-button" style="padding:10px; margin-top:2vh;">OK</button>';
    }

    document.getElementById('notificationModalBody').innerHTML = '<h4>' + message + '</h4></br>' + buttons;

    modal.style.display = "block";

    if (focusButtonId) {
        let fbID = '#' + focusButtonId
        $(fbID).focus();
    }

    let notificationModalOKButton = document.getElementById("notificationModalOKButton");

    let okfunction;
    if (okFn) {
        okfunction = okFn
    } else {
        okfunction = () => {
            modal.style.display = "none";
        };
    }
    if (notificationModalOKButton) {
        notificationModalOKButton.onclick = okfunction;
        $('#notificationModalOKButton').focus();
    }


}


function openModalNotificationPopulation() {
    let modal = document.getElementById('myModalNotificationPopulation');

    modal.style.display = "block";
    $('#notificationModalOKButton').focus();

    let notificationPopulationModalUsePopButton = document.getElementById("notificationPopulationModalUsePopButton");
    let notificationPopulationModalGenerateButton = document.getElementById("notificationPopulationModalGenerateButton");


    notificationPopulationModalUsePopButton.onclick = function () {
        modal.style.display = "none";
        fromPopulation = true;
        triggerBackendCalculations();
    };

    notificationPopulationModalGenerateButton.onclick = function () {
        modal.style.display = "none";
        triggerBackendCalculations();
    };


}


$('#myModalNotification').on('keypress', function (e) {
    if (e.which == 13) {
        document.getElementById("notificationModalOKButton").click()
    }
});


function openModalPersonaEdit(personaID) {
    let modal = document.getElementById('myModalPersonaEdit');
    let isNewPersona = false;
    if (personaID === -1) {
        isNewPersona = true;
    }

    let currentPersona;
    let personaPic = "";
    let personaName = "";
    let personaOccupation = "";
    let personaAge = [];
    let personaDescription = "";
    let personaGender = "";
    let personaMaritalStatus = "";
    let personaEducation = "";
    let personaHealthProperties = {keys: [], values: []};
    let personaShoppingProperties = {keys: [], values: []};
    let personaPercentage = 0;


    if (isNewPersona) {
        personaID = uuidv4();
        personaPic = randomItem(personaImages);
        currentPersona = {
            id: "",
            pic: "",
            name: "",
            occupation: "",
            age: [],
            description: "",
            gender: "",
            maritalStatus: "",
            education: "",
            healthProperties: {
                keys: [],
                values: []
            },
            shoppingProperties: {
                keys: [],
                values: []
            },
            percentage: 0
        }
    } else {
        currentPersona = personaProfiles.filter(persona => persona.id === personaID)[0];
        personaPic = currentPersona.pic;
        personaName = currentPersona.name;
        personaOccupation = currentPersona.occupation;
        personaAge = currentPersona.age;
        personaDescription = currentPersona.description;
        personaGender = currentPersona.gender;
        personaMaritalStatus = currentPersona.maritalStatus;
        personaEducation = currentPersona.education;
        personaHealthProperties = currentPersona.healthProperties;
        personaShoppingProperties = currentPersona.shoppingProperties;
        personaPercentage = currentPersona.percentage;
    }

    createPersonaSelection("Education", ontologyProps.censusProps.education.sort());
    createPersonaSelection("MaritalStatus", ontologyProps.censusProps.maritalStatus.sort());
    createPersonaSelection("Gender", ontologyProps.censusProps.gender.sort());
    createPersonaSelection("Occupation", ontologyProps.censusProps.occupation.sort());

    let ages = ontologyProps.censusProps.age.sort();

    let ageOptions = '';
    for (let item in ages) {
        ageOptions = ageOptions + '<div class="col-3">' +
            '<input type="checkbox" id="age' + ages[item] + '" name="' + ages[item] + '" value="' + ages[item] + '">' +
            '<label for="' + ages[item] + '">' + ages[item] + '</label>' +
            '</div>';
    }
    $('#personaEditModalAge').html(ageOptions);


    $('#personaEditModalPic').html('<div class="personaPicEditContainer"><img class="personaPic" src="' + personaPic + '">  <div class="overlayProfile" onclick="modalProfilePictures()"><div class="text">Change</div></div></div>');
    $('#personaEditModalDescription').val(personaDescription);
    $('#personaEditModalID').val(personaID);
    $('#personaEditModalName').val(personaName);
    $('#personaEditModalGender').val(personaGender);
    $('#personaEditModalMaritalStatus').val(personaMaritalStatus);
    $('#personaEditModalEducation').val(personaEducation);
    $('#personaEditModalOccupation').val(personaOccupation);
    $('#personaEditModalPercentage').val(personaPercentage);

    for (let item in personaAge) {
        let id = "age" + personaAge[item]
        document.getElementById(id).checked = true;
    }

    $('.personaEditModalPropsRightCol').empty();
    $('.personaEditModalPropsLeftCol').empty();

    $('.personaEditModalShoppingRightCol').empty();
    $('.personaEditModalShoppingLeftCol').empty();

    //generate PropSliders Health
    for (let i = 0; i < healthProperties.length; i++) {
        let healthProperty = healthProperties[i];
        let sliderElement = `
                        <div class="row">
                            <div class="col-4">
                                <label for="personaEditModalProp` + healthProperty + `">` + healthProperty + `</label>
                            </div>
                            <div class="col-8">
                                <input type="range" onmouseover="setTitleAttr(this,this.value)" min="0" max="5"  value="0" class="sliderRange" id="personaEditModalProp` + healthProperty + `" style="background: #2d2d2d;">
                            </div>
                        </div>
        `;

        if (!(i % 2)) {
            $('.personaEditModalPropsLeftCol').append(sliderElement);
        } else {
            $('.personaEditModalPropsRightCol').append(sliderElement);
        }
    }

    //set PropSlider Values
    for (let i = 0; i < personaHealthProperties.keys.length; i++) {
        let key = personaHealthProperties.keys[i];
        let val = personaHealthProperties.values[i];
        let propSliderId = '#personaEditModalProp' + key;

        $(propSliderId).val(val);

    }

    //generate PropSliders Health
    for (let i = 0; i < foodCategories.length; i++) {
        let foodProperty = foodCategories[i];
        let sliderElement = `
                        <div class="row">
                            <div class="col-4">
                                <label for="personaEditModalShopping` + foodProperty + `">` + foodProperty + `</label>
                            </div>
                            <div class="col-8">
                                <input type="range" onmouseover="setTitleAttr(this,this.value)" min="0" max="20"  value="0"  class="sliderRange" id="personaEditModalShopping` + foodProperty + `" style="background: #2d2d2d;">
                            </div>
                        </div>
        `;

        if (!(i % 2)) {
            $('.personaEditModalShoppingLeftCol').append(sliderElement);
        } else {
            $('.personaEditModalShoppingRightCol').append(sliderElement);
        }
    }

    //set PropSlider Values Shopping
    for (let i = 0; i < personaShoppingProperties.keys.length; i++) {
        let key = personaShoppingProperties.keys[i];
        let val = personaShoppingProperties.values[i];
        let propSliderId = '#personaEditModalShopping' + key;

        $(propSliderId).val(val);

    }

    modal.style.display = "block";
    $('#personaEditButton').focus();

    var markerPropsButton = document.getElementById("personaEditButton");
    var markerCancelButton = document.getElementById("personaEditCancelButton");

    var okfunction = () => {
        $('#personaEditModalName').css("border-color", "rgb(118, 118, 118)");
        $('#personaEditModalAge').css("border-color", "rgb(118, 118, 118)");
        $('#personaEditModalOccupation').css("border-color", "rgb(118, 118, 118)");
        $('#personaEditModalGender').css("border-color", "rgb(118, 118, 118)");
        $('#personaEditModalEducation').css("border-color", "rgb(118, 118, 118)");
        $('#personaEditModalMaritalStatus').css("border-color", "rgb(118, 118, 118)");


        if ($('#personaEditModalName').val() == "") {
            $('#personaEditModalName').css("border-color", "red");
        } else if ($('#personaEditModalGender').val() == null) {
            $('#personaEditModalGender').css("border-color", "red");
        } else if ($('#personaEditModalEducation').val() == null) {
            $('#personaEditModalEducation').css("border-color", "red");
        } else if ($('#personaEditModalOccupation').val() == null) {
            $('#personaEditModalOccupation').css("border-color", "red");
        } else if ($('#personaEditModalMaritalStatus').val() == null) {
            $('#personaEditModalMaritalStatus').css("border-color", "red");
        } else {
            currentPersona.pic = $('#personaEditModalPic img').attr('src');
            currentPersona.description = $('#personaEditModalDescription').val();
            currentPersona.id = $('#personaEditModalID').val();
            currentPersona.name = $('#personaEditModalName').val();
            currentPersona.occupation = $('#personaEditModalOccupation').val();
            currentPersona.gender = $('#personaEditModalGender').val();
            currentPersona.education = $('#personaEditModalEducation').val();
            currentPersona.maritalStatus = $('#personaEditModalMaritalStatus').val();
            currentPersona.percentage = $('#personaEditModalPercentage').val();

            let newAges = []
            for (let item in ages) {
                let id = "age" + ages[item]
                if (document.getElementById(id).checked == true) {
                    newAges.push(document.getElementById(id).name)
                }
            }
            currentPersona.age = newAges;

            let propValues = [];
            healthProperties.forEach(property => {
                let propSliderId = '#personaEditModalProp' + property;
                propValues.push($(propSliderId).val());
            });
            currentPersona.healthProperties = {keys: healthProperties, values: propValues};

            propValues = [];
            foodCategories.forEach(property => {
                let propSliderId = '#personaEditModalShopping' + property;
                propValues.push($(propSliderId).val());
            });
            currentPersona.shoppingProperties = {keys: foodCategories, values: propValues};


            if (isNewPersona) {
                personaProfiles.push(currentPersona);
            }
            updatePersonaCards();
            modal.style.display = "none";
        }
    };

    let cancelFunction = () => {
        modal.style.display = "none";
    };

    markerPropsButton.onclick = okfunction;
    markerCancelButton.onclick = cancelFunction;


}

function modalProfilePictures() {

    $('#myModalProfilePicturesBody').empty();
    personaImages.forEach((pic) => {
        let element = `
        <div  onclick="closeProfilePicturesModal('` + pic + `')">
            <img class="modalSelectionItemIcon" src="` + pic + `">
         </div>
    `;

        $('#myModalProfilePicturesBody').append(element);

    });


    let modal = document.getElementById('myModalProfilePictures');
    modal.style.display = "block";
}


function closeProfilePicturesModal(personaPic) {
    $('#personaEditModalPic').html('<div class="personaPicEditContainer"><img class="personaPic" src="' + personaPic + '">  <div class="overlayProfile" onclick="modalProfilePictures()"><div class="text">Change</div></div></div>');
    let modal = document.getElementById('myModalProfilePictures');
    modal.style.display = "none";
}


function createPersonaSelection(labelName, list) {
    let selection = '<select id="personaEditModal' + labelName + '" name="personaEditModal' + labelName + '" class="w-100" style="height: 29px"> <option value="" disabled selected>Select your option</option>';
    for (let item in list) {
        let val = list[item];
        selection = selection + '<option value="' + val + '">' + val + '</option>';
    }
    selection = selection + '</select>';
    $("#personaEditModal" + labelName + "Container").html(selection);
    $('#personaEditModal' + labelName + 'Label').html('<label for="personaEditModal' + labelName + '">' + labelName + '</label>');
}

function setTitleAttr(element, val) {
    element.setAttribute('title', val);
}

function openModalMarketEmigrants() {
    let modal = document.getElementById('myModalMarketEmigrants');

    modal.style.display = "block";
    let notificationModalOKButton = document.getElementById("marketEmigrantsModalOKButton");

    okfunction = () => {
        modal.style.display = "none";
    };

    notificationModalOKButton.onclick = okfunction;
    $('#notificationModalOKButton').focus();

}