const DTT_TYPE_VALUE = "value", DTT_TYPE_SHARE = "share";

/*default values for demo mode. In production values are replaced by backend information*/
var ontologyProps = {
    censusProps: {
        age: ["45-54", "55-65", "18-24", "35-44", "0-17", "25-34", "65+"],
        education: ["Lower_Secondary_Degree", "Upper_Secondary_Degree", "Post_Secondary_Non-Tertiary", "Academic_Degree", "No_degree"],
        gender: ["Female", "Male", "Other"],
        health: ["Vision", "Physical_Agility", "Cognition", "Hearing"],
        maritalStatus: ["Single", "Relationship", "Married", "Separated"],
        occupation: ["Young_Professional", "Unemployed", "Professional", "Self-employed", "Apprenticeship", "Management"]
    },
    foodCategories: ["Fats", "Dairy", "Fruit", "Grains", "Vegetable", "Protein"],
    locationTypes: ["Delicacies_Store", "Overseas_Store", "Supermarket", "Bakery", "Discounter", "Bio_Store", "Local_Vegetables_Store", "Location"]

};


/*########################### Jannik ###############################*/
function writeValueToMatrix(id, value) {
    const type = id.substring(0, id.indexOf("-"));
    id = id.replace(type + "-", "");
    if (destinationMatrix[id] === undefined) {

        let startLoc;
        let endLoc;
        let customStartLocation;
        let customEndLocation;

        let startEnd;
        if (type === "PD") {
            startLoc = findDestinationByName(id.substring(0, id.indexOf(";")));
            endLoc = findDestinationByName(id.substring(id.indexOf(";") + 1));
            customStartLocation = {name: startLoc.name, lat: startLoc.location.lat, lon: startLoc.location.lng};
            customEndLocation = {name: endLoc.name, lat: endLoc.location.lat, lon: endLoc.location.lng};
            startEnd = {
                numberOfCarsPoisson: value,
                numberOfCarsDTT: 0,
                numberOfCarsPoissonAC: 0,
                start: customStartLocation,
                end: customEndLocation
            };
        } else if (type === "DTT") {
            startLoc = findDestinationByName(id.substring(0, id.indexOf(";")));
            endLoc = findDestinationByName(id.substring(id.indexOf(";") + 1));
            customStartLocation = {name: startLoc.name, lat: startLoc.location.lat, lon: startLoc.location.lng};
            customEndLocation = {name: endLoc.name, lat: endLoc.location.lat, lon: endLoc.location.lng};
            startEnd = {
                numberOfCarsPoisson: 0,
                numberOfCarsDTT: value,
                numberOfCarsPoissonAC: 0,
                start: customStartLocation,
                end: customEndLocation
            };
        } else if (type === "AC") {
            let locName = id;//.substring(id.indexOf("-")+1,id.length);
            startLoc = findDestinationByName(locName);
            customStartLocation = {name: startLoc.name, lat: startLoc.location.lat, lon: startLoc.location.lng};
            customEndLocation = {
                name: dumpDestionation.name,
                lat: dumpDestionation.location.lat,
                lon: dumpDestionation.location.lng
            } //default location
            startEnd = {
                numberOfCarsPoisson: 0,
                numberOfCarsDTT: 0,
                numberOfCarsPoissonAC: value,
                start: customStartLocation,
                end: customEndLocation
            };
        }
        // add a item
        destinationMatrix[id] = startEnd;
    } else {
        if (type === "PD")
            destinationMatrix[id].numberOfCarsPoisson = value;
        else if (type === "DTT")
            destinationMatrix[id].numberOfCarsDTT = value;
        else if (type === "AC")
            destinationMatrix[id].numberOfCarsPoissonAC = value;
    }
}

function loadActivityTable() {
    let dynamicTableHeader = $("#dynamicTableActivityHeader");
    let dynamicTableContent = $('#dynamicTableActivityContent');

    dynamicTableHeader.empty();
    dynamicTableContent.empty();

    destinationsArray.forEach(destination => {
        let startEnd = destinationMatrix[destination.name];
        let valueAC = startEnd !== undefined ? startEnd.numberOfCarsPoissonAC : 0;

        let header = "<th>" + destination.name + "</th>"
        dynamicTableHeader.append(header);

        let inputField = '<td><input width="5" type="number" id="AC-' + destination.name + '" style="width: 50px" min="0" max="20" step="1" value="' + valueAC + '" onchange="writeValueToMatrix(this.id, this.value);"  placeholder="AC"></td>'
        dynamicTableContent.append(inputField);
    });
}


function loadMatrixTable() {
    let dynamicTableHeader = $("#dynamicTableHeader");
    let dynamicTableContent = $('#dynamicTableContent');

    dynamicTableHeader.empty();
    dynamicTableContent.empty();

    let rowHtml = "";
    dynamicTableHeader.append("<th></th>");
    destinationsArray.forEach(outerDestination => {

        dynamicTableHeader.append("<th>" + outerDestination.name + "</th>")
        rowHtml = "<tr>";
        rowHtml += "<th>" + outerDestination.name + "</th>";
        destinationsArray.forEach(innerDestination => {

            let startEnd = destinationMatrix[outerDestination.name + ";" + innerDestination.name];

            let valuePD = startEnd !== undefined ? startEnd.numberOfCarsPoisson : 0;
            let valueDTT = startEnd !== undefined ? startEnd.numberOfCarsDTT : 0;


            rowHtml += "<td>";

            rowHtml += "<input width='5' type='number' id='PD-" + outerDestination.name + ";" + innerDestination.name + "' " +
                "style='width: 50px' min='0' max='20'\n" +
                "step='1' value='" + valuePD + "'\n" +
                "onchange='writeValueToMatrix(this.id, this.value);'" +
                (innerDestination.name === outerDestination.name ? "disabled" : "enabled") +
                " placeholder='PD'>";

            rowHtml += "<input width='5' type='number' id='DTT-" + outerDestination.name + ";" + innerDestination.name + "' " +
                "style='width: 50px' min='0' max='20'\n" +
                "step='1' value='" + valueDTT + "'\n" +
                "onchange='writeValueToMatrix(this.id, this.value);'" +
                (innerDestination.name === outerDestination.name ? "disabled" : "enabled") +
                " placeholder='DTT'>";

            rowHtml += "</td>";
        });
        rowHtml += "</tr>";
        dynamicTableContent.append(rowHtml);

    });
}


function changedDesiredArrivalTime(value, from_name, to_name, input_index, dtt_type) {
    console.log(event);

    value = Number(value);
    let mapKey = from_name + ";" + to_name;

    let dtt_list = [];

    let data_obj = {id: input_index, value: -1, share: -1};

    if (desiredArrivalTimes[mapKey] !== undefined) {
        dtt_list = desiredArrivalTimes[mapKey];
        dtt_list.forEach(dtt_list_entry => {
            if (dtt_list_entry.id === input_index) data_obj = dtt_list_entry;
        })
        dtt_list = dtt_list.filter(dtt_list_entry => {
            return dtt_list_entry !== data_obj;
        })
    }

    if (dtt_type === DTT_TYPE_VALUE) data_obj.value = value;
    else if (dtt_type === DTT_TYPE_SHARE) data_obj.share = value;

    dtt_list.push(data_obj);
    desiredArrivalTimes[mapKey] = dtt_list;

}

function addDTTRowToContainer(event, from_name, to_name) {
    let containerSpanID = event.target.parentElement.id;
    let containerSpan = document.getElementById(containerSpanID);


    let divInputChildren = containerSpan.childElementCount;

    let container = document.createElement("div");
    let inputValue = document.createElement("input");

    inputValue.onchange = () => changedDesiredArrivalTime(inputValue.value, from_name, to_name, divInputChildren, DTT_TYPE_VALUE);
    inputValue.type = 'number';
    inputValue.style = 'width: 50px';
    inputValue.min = ('0');
    inputValue.max = ('40');
    inputValue.step = ('1');


    let inputShare = document.createElement("input");       // Create a <li> node

    inputShare.onchange = () => changedDesiredArrivalTime(inputShare.value, from_name, to_name, divInputChildren, DTT_TYPE_SHARE);
    inputShare.type = 'number';
    inputShare.style = 'width: 50px';
    inputShare.min = ('0');
    inputShare.max = ('1');
    inputShare.step = ('0.1');


    container.appendChild(inputValue);
    container.appendChild(inputShare);

    containerSpan.insertBefore(container, containerSpan.childNodes[0]);

    debugger;
}

function loadDTTSettingsArea() {

    let area = $("#DesiredArrivalTime_SettingsArea");
    area.empty();
    let rows = [];
    let row;

    let valueTick = 0;
    let valueShare = 0;

    if (destinationsArray.length > 1) {
        destinationsArray.forEach(outerDest => {
            row = "<p style='margin: 0;float: left; width: 100%; font-weight:bold; '>" + outerDest.name + "&#187;</p>";
            destinationsArray.forEach(innerDest => {
                if (outerDest !== innerDest) {
                    row += "<div class='dttRowElement'>" +
                        "<span style='margin-right: 1rem;'>" +
                        "<label for='" + outerDest.name + "->" + innerDest.name + "'>" + innerDest.name + ": </label>" +
                        "<span id='ContainerDTT:" + outerDest.name + "->" + innerDest.name + "'>";

                    let key = outerDest.name + ";" + innerDest.name;
                    if (desiredArrivalTimes[key] !== undefined) {
                        let entryArray = desiredArrivalTimes[key];

                        for (let entry in entryArray) {
                            row +=
                                "<div>" +
                                "<input onchange='changedDesiredArrivalTime(this.value,\"" + outerDest.name + "\", \"" + innerDest.name + "\"," + entryArray[entry].id + " , \"" + DTT_TYPE_VALUE + "\")' id = '" + outerDest.name + "->" + innerDest.name + ":" + 1 + "#" + DTT_TYPE_VALUE + "' width='5' type='number' id='" + outerDest.name + "->" + innerDest.name + "' " +
                                "           style='width: 50px' min='0' step='1' value = " + entryArray[entry].value + ">" +
                                "<input onchange='changedDesiredArrivalTime(this.value,\"" + outerDest.name + "\", \"" + innerDest.name + "\"," + entryArray[entry].id + " , \"" + DTT_TYPE_SHARE + "\")' width='5' type='number' id='" + outerDest.name + "->" + innerDest.name + "' style='width: 50px' min='0' max='1' step='0.1' value = " + entryArray[entry].share + ">" +
                                "</div>";
                        }
                    } else {

                        row +=
                            "<div>" +
                            "<input onchange='changedDesiredArrivalTime(this.value,\"" + outerDest.name + "\", \"" + innerDest.name + "\", 1, \"" + DTT_TYPE_VALUE + "\")' id = '" + outerDest.name + "->" + innerDest.name + ":" + 1 + "#" + DTT_TYPE_VALUE + "' width='5' type='number' id='" + outerDest.name + "->" + innerDest.name + "' style='width: 50px' min='0' step='1'>" +
                            "<input onchange='changedDesiredArrivalTime(this.value,\"" + outerDest.name + "\", \"" + innerDest.name + "\", 1, \"" + DTT_TYPE_SHARE + "\")' width='5' type='number' id='" + outerDest.name + "->" + innerDest.name + "' style='width: 50px' min='0' max='1' step='0.1'>" +
                            "</div>";

                    }

                    row += "<button style ='width: 100%' onclick='addDTTRowToContainer(event,\"" + outerDest.name + "\", \"" + innerDest.name + "\")'>Add</button>" +
                        "</span>" +
                        "</span>" +
                        "</div>";
                }
            })
            rows.push(row);
        })

        rows.forEach(row => {
            row = "<div style='width: fit-content;padding: 0.3rem;margin-bottom: 0.3rem; width: 100%;'>" + row + "</div>"
                + "<br>";
            area.append(row);
        })
    }
}

function findDestinationByName(name) {
    for (let i = 0; i < destinationsArray.length; i++) {
        if (destinationsArray[i].name === name) return destinationsArray[i];
    }
    return undefined;
}


/*function loadAgentTypeSettingsArea() {

    for (let agentTypeID in agentTypeMap) {

        $("#" + agentTypeID).val(agentTypeMap[agentTypeID].value);

        for (let mindsetEntry in agentTypeMap[agentTypeID].mindSets) {

            $("#" + agentTypeID + "-" + mindsetEntry).val(agentTypeMap[agentTypeID].mindSets[mindsetEntry]);

        }
    }
}*/


/*function createAgentTypeSection() {
    $.get("/getAgentTypes", {})
        .done(function (agentTypeData) {
            listAgentTypes = JSON.parse(agentTypeData);
            let mindSets = [];
            $.get("/getMindSets", {})
                .done(function (mindSetData) {
                    mindSets = JSON.parse(mindSetData);
                    for (var i = 0; i < listAgentTypes.length; i++) {
                        let innerHtml = "";
                        innerHtml +=
                            "<tr>"
                            + "<th>"
                            + "<h5 style = 'float:left;'>" + listAgentTypes[i] + "</h5>"
                            + "</th>"
                            + "<th>"
                            + "<input  onchange='addToAgentTypeMap(this.id, this.value)' type='number' id='" + listAgentTypes[i] + "' value='' style='width: 10rem' min='0' max='100' step='10'>"
                            + "</th>";

                        mindSets.forEach(mindSet => {
                            innerHtml +=
                                "<th>"
                                + "<input onchange='changeMindSetPercentage(\"" + listAgentTypes[i] + "\", \"" + mindSet + "\",this.value)' placeholder='" + mindSet + "' type='number' id='" + listAgentTypes[i] + "-" + mindSet + "' value='' style='width: 10rem' min='0' max='100' step='10'>"
                                + "</th>";
                        });

                        innerHtml += "</tr>";

                        document.getElementById("agentTypeInput").innerHTML += innerHtml;
                    }
                });
        });
}


function addToAgentTypeMap(agentTypeName, value) {
    let agentTypeObj = agentTypeMap[agentTypeName] !== undefined ? agentTypeMap[agentTypeName] : {
        value: -1,
        mindSets: {}
    };

    agentTypeObj.value = value;

    agentTypeMap[agentTypeName] = agentTypeObj;
}


function changeMindSetPercentage(agentTypeName, mindSet, value) {
    let agentTypeObj = agentTypeMap[agentTypeName] !== undefined ? agentTypeMap[agentTypeName] : {
        value: -1,
        mindSets: {}
    };

    agentTypeObj.mindSets[mindSet] = value;
    agentTypeMap[agentTypeName] = agentTypeObj;
    console.log(agentTypeObj);
} */

