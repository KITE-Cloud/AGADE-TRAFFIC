/*Initial empty Chart*/
createEmptyChart("chart-area");
createEmptyChart("canvasA");
createEmptyChart("canvasB");
createEmptyChart("canvasTargetsLoc");

function createEmptyChart(canvasId) {
    new Chart(document.getElementById(canvasId), {
        //type: 'line',
        type: "line",
        data: {
            labels: [1500, 1600, 1700, 1750, 1800, 1850, 1900, 1950, 1999, 2050],
            datasets: [{
                data: [],
                fill: false
            }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    });
}

function plot() {
    $('#chart-area').remove();
    $('#diagramContainerMain').prepend('<canvas id="chart-area"></canvas>');

    let selectOption = document.getElementById("diagramPlot");
    let selectedValue = selectOption.options[selectOption.selectedIndex].value;
    let diagramData = null;
    let chartType = 'line';

    if (selectedValue == "Number Of Moving Agents Per Tick") {
        diagramData = simulation.statisticalKPIs.numberOfMovingAgentsPerTick;
        chartType = "line";
    } else if (selectedValue == "Created Agents Per Tick") {
        diagramData = simulation.statisticalKPIs.createdAgentsPerTick;
        chartType = "line";
    } else if (selectedValue == "Initial Trans. Mode") {
        diagramData = countVehicles(agents);
        chartType = "pie";
    } else if (selectedValue == "Trans. Mode Progression") {
        diagramData = countVehicleProgression(agents);
        chartType = "pie";
    }

    if (diagramData != null) {
        diagramData = processDiagramData(chartType, diagramData);
        createGraph("chart-area", diagramData, chartType)
    }


}

function processDiagramData(chartType, diagramData) {
    if (chartType != 'pie') {
        let adaptedXCoords = [];
        let adaptedYCoords = [];
        for (var k = 0; k <= lastTickInSimulation; k++) {
            adaptedXCoords[k] = k;
            if (diagramData.xcoordinates.includes(k)) {
                var index = diagramData.xcoordinates.indexOf(k)
                adaptedYCoords[k] = diagramData.ycoordinates[index];
            } else {
                adaptedYCoords[k] = 0;
            }
        }

        console.log(adaptedXCoords)
        console.log(adaptedYCoords)
        diagramData.xcoordinates = adaptedXCoords;
        diagramData.ycoordinates = adaptedYCoords;
    }

    return diagramData;

}

function createGraph(canvasId, diagramData, chartType, isMultiData) {
    let backgroundColorArray = ["#407B24", "#05527D", "#DD803F", "#5F0929", "#65E44E", "#CAF91C", "#E0AEAE", "#DC2508", "#A5D1CB", "#A7D821", "#4F38AF", "#7145FC", "#267FC7", "#F4B06D", "#1050EB", "#33B8F1", "#87C96E", "#FBE08B", "#11355C", "#03F921", "#F31AB8", "#3CA7C7", "#7D4D31", "#283623", "#C206E9", "#FBEF7D", "#15C08D", "#E589F5", "#8569EF", "#91E721", "#214952", "#6BDFFC", "#6C41DE", "#B706BA", "#6DEE3E", "#9BD490", "#8AD2D6", "#3DB86A", "#FED0DE", "#2DB39A", "#4D47E6", "#DD9EDC", "#442B73", "#8EA04B", "#9D50DC", "#FE266B", "#345F96", "#3EEAD3", "#13588F", "#E00EE8", "#4C20DC", "#71B4A9", "#902181", "#7FC190", "#096C41", "#298194", "#492431", "#4BF580", "#2742D4", "#40A36C", "#69CC26", "#399893", "#45CC8A", "#14A53B", "#5F8642", "#A40A25", "#55682A", "#E20E5C", "#A2D3D8", "#F9A692", "#352FCC", "#6C00E3", "#DB87F5", "#873C16", "#AFF9C3", "#BDC67E", "#98C162", "#06B71C", "#983DD6", "#7C57CD", "#01935E", "#F99547", "#42AF27", "#AE5C02", "#1708AC", "#4638F6", "#F17D94", "#056D46", "#4A531B", "#B0F8DE", "#C1AFC1", "#069366", "#57D8CC", "#2EC93D", "#463996", "#56E66D", "#74DAAC", "#8E7804", "#F3BBB1", "#ABD2C2"];
    let pieLables = {
        labels: {
            render: 'percentage',
            fontColor: 'white',
            precision: 2
        }
    }

    if (isMultiData) {
        new Chart(document.getElementById(canvasId), {
            type: chartType,
            data: diagramData,
            options: {
                responsive: true,
                maintainAspectRatio: false,
                tooltips: {
                    mode: 'index',
                    intersect: true
                },
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                },
                plugins: (chartType === 'bar') ? {
                    datalabels: {
                        anchor: 'center',
                        align: top,
                        offset: 20,
                        color: '#000000',
                        backgroundColor: 'rgba(255, 255, 255, 0.8)',
                        borderRadius: 3,
                    }
                } : {
                    datalabels: {
                        color: 'rgba(255, 255, 255, 0)',
                        display: false,
                    }
                },
            }
        });
    } else {
        new Chart(document.getElementById(canvasId), {
            type: chartType,
            data: {
                labels: diagramData.xcoordinates,
                datasets: [{
                    data: diagramData.ycoordinates,
                    label: "Data1",
                    borderColor: "#3e95cd",
                    backgroundColor: (chartType === 'pie') ? backgroundColorArray : "#3e95cd",
                    fill: false
                }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: (chartType === 'pie') ? {
                    datalabels: {
                        formatter: (value, ctx) => {
                            let sum = 0;
                            let dataArr = ctx.chart.data.datasets[0].data;
                            dataArr.map(data => {
                                sum += data;
                            });
                            let percentage = (value * 100 / sum).toFixed(2) + "%";
                            return percentage;
                        },
                        color: '#fff',
                    }
                } : {
                    datalabels: {
                        color: 'rgba(255, 255, 255, 0)',
                        display: false,
                    }
                },
            }
        });
    }
}


/*################## Compare #####################*/

function plotCompare() {

    $('#canvasA').remove();
    $('#diagramContainerCompareA').prepend('<canvas id="canvasA"></canvas>');

    $('#canvasB').remove();
    $('#diagramContainerCompareB').prepend('<canvas id="canvasB"></canvas>');

    let selectOption = document.getElementById("diagramPlotCompare");
    let selectedValue = selectOption.options[selectOption.selectedIndex].value;
    let diagramDataA = null;
    let diagramDataB = null;

    if (selectedValue == "Number Of Moving Agents Per Tick") {
        diagramDataA = simulation.statisticalKPIs.numberOfMovingAgentsPerTick;
        diagramDataB = compSimResult.statisticalKPIs.numberOfMovingAgentsPerTick;
        chartType = "line";
    } else if (selectedValue == "Created Agents Per Tick") {
        diagramDataA = simulation.statisticalKPIs.createdAgentsPerTick;
        diagramDataB = compSimResult.statisticalKPIs.createdAgentsPerTick;
        chartType = "line";
    } else if (selectedValue == "Initial Trans. Mode") {
        diagramDataA = countVehicles(simulation.agents);
        diagramDataB = countVehicles(compSimResult.agents);
        chartType = "pie";
    } else if (selectedValue == "Trans. Mode Progression") {
        diagramDataA = countVehicleProgression(simulation.agents);
        diagramDataB = countVehicleProgression(compSimResult.agents);
        chartType = "pie";
    }

    if (diagramDataA != null) {
        diagramDataA = processDiagramData(chartType, diagramDataA);
        createGraph("canvasA", diagramDataA, chartType)
    }

    if (diagramDataB != null) {
        diagramDataB = processDiagramData(chartType, diagramDataB);
        createGraph("canvasB", diagramDataB, chartType)
    }

}

//var prefChangedAgents = [];

function examineAgentsForChangedPrefs(agentsA, agentsB) {
    let prefChangedAgents = [];
    for (agentIndex in agentsA) {
        let agentA = agentsA[agentIndex];
        let agentB = agentsB.filter(compAgent => compAgent.id === agentA.id)[0];
        let changedPrefs = [];

        for (prefIndex in agentA.decisionFactors) {
            let prefA = agentA.decisionFactors[prefIndex];
            let prefB = agentB.decisionFactors.filter(df => df.key === prefA.key)[0];
            if (prefA.value !== prefB.value) {
                changedPrefs.push({prefName: prefA.key, valA: prefA.value, valB: prefB.value})
            }
        }
        if (changedPrefs.length > 0) {
            prefChangedAgents.push({agentID: agentA.id, changedPrefs: changedPrefs});
        }
    }

    return prefChangedAgents;
}

var initModeChangedAgents = [];

function examineAgentsForChangedInitMode() {
    initModeChangedAgents = [];
    for (agentIndex in population.agents) {
        let agentA = simulation.agents[agentIndex];
        let agentB = compSimResult.agents.filter(compAgent => compAgent.id === agentA.id)[0];
        let changedInitMode = [];

        if (agentA.journey && agentB.journey) {
            if (agentA.journey.transportationMode && agentA.journey.transportationMode.length > 0 && agentB.journey.transportationMode && agentB.journey.transportationMode.length > 0) {
                let modeA = agentA.journey.transportationMode[0];
                let modeB = agentB.journey.transportationMode[0];
                if (modeA !== modeB) {
                    changedInitMode.push({valA: modeA, valB: modeB})
                }

                if (changedInitMode.length > 0) {
                    initModeChangedAgents.push({agentID: agentA.id, changedInitMode: changedInitMode});
                }
            }

        }

    }
}

var distChangedAgents = [];

function examineAgentsForChangedDist() {
    distChangedAgents = [];
    for (agentIndex in population.agents) {
        let agentA = simulation.agents[agentIndex];
        let agentB = compSimResult.agents.filter(compAgent => compAgent.id === agentA.id)[0];
        let changedDist = [];

        if (agentA.journey && agentB.journey) {
            let distA = agentA.journey.totalTravelDistance;
            let distB = agentB.journey.totalTravelDistance;
            if (distA !== distB) {
                changedDist.push({valA: distA, valB: distB})
            }

            if (changedDist.length > 0) {
                distChangedAgents.push({agentID: agentA.id, changedDist: changedDist});
            }
        }

    }
}

/*################## Compare END #####################*/

/*############################### Target Graphs ##############################*/

function plotTargetGraphs() {
    $('#canvasTargetsLoc').remove();
    $('#diagramContainerCompareTargets').prepend('<canvas id="canvasTargetsLoc"></canvas>');

    let targetsSimA = countAgentsPassingAtLocations(simulation.agents);
    let targetsSimB = countAgentsPassingAtLocations(compSimResult.agents);
    let labels = [];
    let targetsA = [];
    let targetsB = []

    for (const [key, value] of targetsSimA.entries()) {
        if (value > 0 && targetsSimB.get(key) > 0) {
            labels.push(key)
            targetsA.push(value)
            targetsB.push(targetsSimB.get(key))
        }
    }

    let barChartData = {
        labels: labels,
        datasets: [{
            label: 'Simulation A',
            backgroundColor: "#05527D",
            //yAxisID: 'y-axis-1',
            data: targetsA
        }, {
            label: 'Simulation B',
            backgroundColor: "#DD803F",
            data: targetsB
        }]
    };
    createEmigrantsGraph(labels);
    createGraph('canvasTargetsLoc', barChartData, 'bar', true);
}

function createEmigrantsGraph(nodeLabels){
    //emigrants graph
    let nodes=[];
    for(let i=0; i<nodeLabels.length; i++){
        let node = {
            "id": i,
            "name": nodeLabels[i]
        }
        nodes.push(node)
    }
    let links = createArcGraphLinks(nodeLabels, nodes);

    let graphData = {
        "nodes": nodes,
        "links": links
    }
    createArcGraph(graphData)
}

var linksAgentIDs = new Map();
function createArcGraphLinks(nodeLabels, nodes){
    let links = [];
    let diffList = [];
    for (let locIndex in nodeLabels) {
        let locName = nodeLabels[locIndex];
        let agentList = simulation.agents;
        for (let index in agentList) {
            let agent = agentList[index];
            let agentB =  compSimResult.agents.filter(compAgent => compAgent.id === agent.id)[0]
            let origin = agent.origin;
            let trips = agent.journey.trips;

            let prunedJourney = [];
            let prunedJourneyLocationNames = [];
            let agentBPrunedJourney = [];
            let agentBPrunedJourneyLocationNames = [];

            trips.forEach(trip => {
                if(trip.end.locationName !== origin.locationName){
                    prunedJourney.push(trip.end);
                    prunedJourneyLocationNames.push(trip.end.locationName)
                }
            })
            agentB.journey.trips.forEach(trip => {
                if(trip.end.locationName !== origin.locationName){
                    agentBPrunedJourney.push(trip.end);
                    agentBPrunedJourneyLocationNames.push(trip.end.locationName)
                }
            })

            if (prunedJourneyLocationNames.includes(locName) && !agentBPrunedJourneyLocationNames.includes(locName)){
                for(let i=0; i<agentBPrunedJourneyLocationNames.length;i++){
                    if(!prunedJourneyLocationNames.includes(agentBPrunedJourneyLocationNames[i])){
                        diffList.push({from: locName, to: agentBPrunedJourney[i], agentID: agent.id}  )
                    }
                }
            }
        }
    }

    for (let locIndex in nodeLabels) {
        let sourceName = nodeLabels[locIndex];
        nodeLabels.forEach(targetNodeName => {
            if(targetNodeName !== sourceName){
                let diffAgentTripList = diffList.filter(element => element.from === sourceName && element.to.locationName === targetNodeName)
                let count = diffAgentTripList.length;
                let agentIDs = [];
                diffAgentTripList.forEach(a => {
                    agentIDs.push(a.agentID);
                })
                if(count>0){
                    let link = {
                        "source": nodes.filter(node => node.name === sourceName)[0].id,
                        "target": nodes.filter(node => node.name === targetNodeName)[0].id,
                        "count":  count,
                        "agentIDs": agentIDs,
                    }
                    links.push(link);
                }
            }
        });
    }

    return links;
}

$(window).resize(function() {
    if (compSimResult.agents){
        plotTargetGraphs();
    }
});

function createArcGraph(data) {
    $('#emigrantsGraph').remove();
    $('#emigrantsGraphContainer').prepend('<div id="emigrantsGraph"></div>');

    // List of node names
    var allNodes = data.nodes.map(function (d) { return d.name })
    // set the dimensions and margins of the graph
    let margin = { top: 30, right: 200, bottom: 0, left: 50 },
        width = 120*allNodes.length;
        height = 500 - margin.top - margin.bottom;

    // append the svg object to the body of the page
    let svg = d3.select("#emigrantsGraph")
        .append("svg")
        .attr("width", 110*allNodes.length )
        .attr("height", "600")
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
        //.attr("transform", "scale(0.7)");

    // A linear scale to position the nodes on the X axis
    var x = d3.scalePoint()
        .range([margin.left, width-margin.right])
        .domain(allNodes)

    // Add the circle for the nodes
    var nodes = svg
        .selectAll("mynodes")
        .data(data.nodes)
        .enter()
        .append("circle")
        .attr("cx", function (d) { return (x(d.name)) })
        .attr("cy", height -20)
        .attr("r", 10)
        .style("fill", "#b8b8b8")


    // And give them a label
    var labels = svg
        .selectAll("mylabels")
        .data(data.nodes)
        .enter()
        .append("text")
        .attr("x", function (d) { return (x(d.name)) })
        .attr("y", height + 20 )
        .text(function (d) { return (d.name) })
        .style("text-anchor", "middle")
        .style("font-size", "16px")

    // Add links between nodes. Here is the tricky part.
    // In my input data, links are provided between nodes -id-, NOT between node names.
    // So I have to do a link between this id and the name
    var idToNode = {};
    data.nodes.forEach(function (n) {
        idToNode[n.id] = n;
    });
    // Cool, now if I do idToNode["2"].name I've got the name of the node with id 2

    // Add the links
    var links = svg
        .selectAll('mylinks')
        .data(data.links)
        .enter()
        .append('path')
        .attr('d', function (d) {
            start = x(idToNode[d.source].name)    // X position of start node on the X axis
            end = x(idToNode[d.target].name)      // X position of end node
            return ['M', start, height - 30,    // the arc starts at the coordinate x=start, y=height-30 (where the starting node is)
                'A',                            // This means we're gonna build an elliptical arc
                (start - end) / 2, ',',    // Next 2 lines are the coordinates of the inflexion point. Height of this point is proportional with start - end distance
                (start - end) / 2, 0, 0, ',',
                start < end ? 1 : 0, end, ',', height - 30] // We always want the arc on top. So if end is before start, putting 0 here turn the arc upside down.
                .join(' ');
        })
        .style("fill", "none")
        .attr("stroke", "#b8b8b8")

    var selectedNode;
    links
        .on('click', function (d) {
            console.log('source: ' + idToNode[selectedNode].name);
            let targetNode = (selectedNode == d.source) ? idToNode[d.target].name : idToNode[d.source].name
            console.log('target: ' + targetNode);
            console.log('count:' + d.count);
        })

    // Add the highlighting functionality
    nodes
        .on('mouseover', function (d) {
            selectedNode = d.id;
            links
                .style('stroke', '#b8b8b8')
                .style('stroke-width', '1')
            // Highlight the nodes: every node is green except of him
            nodes.style('fill', "#B8B8B8")
            d3.select(this).style('fill', '#80ba24')
            // Highlight the connections
            $('#emigrantsDetails').empty()
            links
                .style('stroke', function (link_d) {
                    return link_d.source === d.id ? '#80ba24' : '#b8b8b8';
                })
                .style('stroke-width', function (link_d) {
                    if (link_d.source === d.id) {
                        let s = '<div class="col-6"><button class="comp-accordion" onclick="agentDetailsOnLink(`' + btoa(JSON.stringify(link_d.agentIDs)) + '`, `' + idToNode[link_d.source].name + '`,`' + idToNode[link_d.target].name + '`,`' + link_d.count + '`)">' + idToNode[link_d.source].name + ' &rarr; ' + idToNode[link_d.target].name + '; | count: (' + link_d.count + ')</button><div class="comp-panel"></div></div>'
                        $('#emigrantsDetails').append(s);
                    }
                    return link_d.source === d.id ? 4 : 1;
                })
        })
}

function agentDetailsOnLink(agentIDs, sourceName, targetName, count) {
    let agentIDList = JSON.parse(atob(agentIDs));

    let agentsSimA = []
    let agentsSimB = []
    let popAgentsSimA = []
    let popAgentsSimB = []

    agentIDList.forEach(id => {
        simulation.agents.forEach(ag => {
            if (ag.id === id) {
                agentsSimA.push(ag);
            }
        })
        compSimResult.agents.forEach(ag => {
            if (ag.id === id) {
                agentsSimB.push(ag);
            }
        })
        population.agents.forEach(ag => {
            if (ag.id === id) {
                popAgentsSimA.push(ag);
            }
        })
        compPopulation.agents.forEach(ag => {
            if (ag.id === id) {
                popAgentsSimB.push(ag);
            }
        })

    })


    //fill modal content
    $('#marketEmigrantsModalTitle').html(count + ' Emigrants from ' + sourceName + ' to ' + targetName);

    $('#emDetailCompACombustionDist').html(calculateCombustionKilometers(agentsSimA));
    $('#emDetailCompATotalTravelDist').html(calculateGlobalTravelKilometers(agentsSimA));
    $('#emDetailCompAAvgTravellerUtility').html(calculateAvgAgentUtility(agentsSimA));

    $('#emDetailCompBCombustionDist').html(calculateCombustionKilometers(agentsSimB));
    $('#emDetailCompBTotalTravelDist').html(calculateGlobalTravelKilometers(agentsSimB));
    $('#emDetailCompBAvgTravellerUtility').html(calculateAvgAgentUtility(agentsSimB));

    $('#emDetailAgentIDTags').empty();
    agentIDList.forEach(id => {
        let tag = '<span class="personaPropTag" style="display:inline-block;">AgentID: ' + id + '</span>'
        $('#emDetailAgentIDTags').append(tag);
    })

    let prefChangedAgents = examineAgentsForChangedPrefs(popAgentsSimA, popAgentsSimB);

    let acchtml = "";
    let countPrefChangesMap = {};
    for (let agentIndex in prefChangedAgents) {
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

    $('#emDetailPrefChangedAgentAcc').html(acchtml);

    let countMap = new Map();
    let alreadyCaptured = []; //avoiding duplicates
    let xCoord = []
    let yCoord = []

    agentsSimB.forEach(ag => {
        let persona = ag.personaProfile;
        if (!alreadyCaptured.includes(persona)) {
            countMap.set(persona, 1);
        } else {
            let updatedCount = countMap.get(persona) + 1;
            countMap.set(persona, updatedCount);
        }
        alreadyCaptured.push(persona);
    })
    for (const [key, value] of countMap.entries()) {
        xCoord.push(key)
        yCoord.push(value)
    }
    let diagramData = {xcoordinates: xCoord, ycoordinates: yCoord}
    $('#personaPie').remove();
    $('#emDetailsPersonaPieContainer').prepend('<canvas id="personaPie"></canvas>');
    createGraph('personaPie', diagramData, 'pie')


    countMap = new Map();
    alreadyCaptured = []; //avoiding duplicates
    xCoord = []
    yCoord = []

    agentsSimB.forEach(ag => {
        let origin = ag.origin.locationName;
        if (!alreadyCaptured.includes(origin)) {
            countMap.set(origin, 1);
        } else {
            let updatedCount = countMap.get(origin) + 1;
            countMap.set(origin, updatedCount);
        }
        alreadyCaptured.push(origin);
    })
    for (const [key, value] of countMap.entries()) {
        xCoord.push(key)
        yCoord.push(value)
    }
    diagramData = {xcoordinates: xCoord, ycoordinates: yCoord}
    $('#originPie').remove();
    $('#emDetailsOriginPieContainer').prepend('<canvas id="originPie"></canvas>');
    createGraph('originPie', diagramData, 'pie')

    openModalMarketEmigrants();
}

/*############################### Target Graphs END ##############################*/


//avg travel Time
function avgTimeTable() {
    $("#avgTravelTimeModal").empty();

    let travelTimeDetail = simulation.travelTimeDetails
        .filter(
            travelTimeDetail => {
                return (
                    travelTimeDetail.locationFrom.lat === selectedMarkers[0].latlng.lat &&
                    travelTimeDetail.locationFrom.lon === selectedMarkers[0].latlng.lng &&
                    travelTimeDetail.locationTo.lat === selectedMarkers[1].latlng.lat &&
                    travelTimeDetail.locationTo.lon === selectedMarkers[1].latlng.lng
                )
            }
        );


    let col1 = [];
    let col2 = [];
    let col3 = [];
    let col4 = [];
    $("#avgTravelTimeModal").empty();

    for (var i = 0; i < travelTimeDetail.length; i++) {
        col1.push(travelTimeDetail[i].phase);
        col2.push(travelTimeDetail[i].driverType);
        col3.push(travelTimeDetail[i].numberAgents);
        col4.push(travelTimeDetail[i].avgTravelTime);
    }

    let fromTitle = 'From: ' + getDestinationName(selectedMarkers[0].latlng) + ' (' + selectedMarkers[0].latlng.lat + ', ' + selectedMarkers[0].latlng.lng + ')';
    let toTitle = 'To: ' + getDestinationName(selectedMarkers[1].latlng) + ' (' + selectedMarkers[1].latlng.lat + ', ' + selectedMarkers[1].latlng.lng + ')';
    $("#avgTravelTimeModal").append("<h3 style='margin-top: 30px;'>" + fromTitle + "</h3>");
    $("#avgTravelTimeModal").append("<h3>" + toTitle + "</h3>");
    $("#avgTravelTimeModal").append("<div class='table-wrapper'><table class='fl-table'>");
    $(".fl-table").append("<thead class='thead'></thead>");
    //document.write("<table border='1' width='200'>")
    $(".thead").append("<tr><th>Phase</th><th>Agent Type</th><th>Count</th><th>Avg. Time</th></tr>");

    for (let j = 0; j < col1.length; j++) {
        $(".thead").append("<tr><td>" + col1[j] + "</td><td>" + col2[j] + "</td><td>" + col3[j] + "</td><td>" + col4[j] + "</td></tr>");
    }
}

function destinationDetailTable() {
    $("#myModalDestinationContent").empty();

    let locationDetail = simulation.locationDetails
        .filter(loc => {
                return (
                    selectedMarkers[0].latlng.lat === loc.location.lat
                    && selectedMarkers[0].latlng.lng === loc.location.lon
                )
            }
        )[0];

    if (locationDetail !== undefined) {

        $("#myModalDestinationContent").append("<div class='table-wrapper'><table class='fl-table'>");
        $(".fl-table").append("<thead class='thead'></thead>");

        let variableName = "";
        let odd = 0;
        for (let i = 0; i < Object.keys(locationDetail).length; i++) {
            let key = Object.keys(locationDetail)[i];
            console.log(key, locationDetail[key]);
            if (key !== "simId") {
                let value = "";

                switch (key) {
                    case "location":
                        value = 'lat:' + locationDetail[key].lat + '<br>lon:' + locationDetail[key].lon + '<br>Location Name: ' + locationDetail[key].locationName;
                        variableName = "Location";
                        break;
                    case "locationType":
                        variableName = "Location Type";
                        break;
                    case "bikesParked":
                        variableName = "Bikes Parked";
                        break;
                    case "directParkingSlots":
                        variableName = "Parking Slots";
                        break;
                    case "directParkedCars":
                        variableName = "Occupied Parking Slots";
                        break;
                    case "parkingArea":
                        variableName = "Parking Area Diameter";
                        break;
                    case "surroundingParkingSlots":
                        variableName = "Parking Slots (Parking Area)";
                        break;
                    case "surroundingParkedCars":
                        variableName = "Occupied Parking Slots (Parking Area)";
                        break;

                }

                if (key !== "location") value = locationDetail[key];

                if (odd % 2 == 0) {
                    $(".thead").append("<tr><th style='background: #6d8940;'>" + variableName + "</th><td>" + value + "</td>");
                } else {
                    $(".thead").append("<tr><th>" + variableName + "</th><td>" + value + "</td>");
                }
                odd++;
            }
        }

    }


}


$('#avgTimeBtn').click(function () {
    if (!isDemo) {
        if (JSON.stringify(simulation) === "{}") {
            openModalNotification("You need to run a simulation first before using this option");
        } else {
            if (selectedMarkers.length === 2) {
                avgTimeTable();
                modalAvgTime();
            } else {
                openModalNotification("Please select 2 marker on the map");
            }
        }
    } else {
        openModalNotification("This option is not available in the AGADE Traffic demo. See Github for installing AGADE Traffic on your local machine.");
    }

});


$('#btn_showDestDetails').click(function () {
    if (!isDemo) {
        if (JSON.stringify(simulation) === "{}") {
            openModalNotification("You need to run a simulation first before using this option");
        } else {
            if (selectedMarkers.length === 1 || selectedMarkers.length === 2) {
                destinationDetailTable();
                openModalDestinationDetails();
            } else {
                openModalNotification("You must select 1 marker.");
            }
        }
    } else {
        openModalNotification("This option is not available in the AGADE Traffic demo. See Github for installing AGADE Traffic on your local machine.");
    }
});


$('#btn_showRouteChanges').click(function () {
    if (!isDemo) {
        if (JSON.stringify(simulation) === "{}") {
            openModalNotification("You need to run a simulation first before using this option");
        } else {
            if (selectedMarkers.length === 2) {
                routeChangeMode();
            } else {
                openModalNotification("You must select 1 marker.");
            }
        }
    } else {
        openModalNotification("This option is not available in the AGADE Traffic demo. See Github for installing AGADE Traffic on your local machine.");
    }
});


//on click highlight selected marker, only two markes can be selected at the same time
//border: 3px solid green;
var selectedMarkers = [];

function selectMarker(m) {
    console.log(m);
    let oldMarker;
    if (selectedMarkers.length >= 2) {
        oldMarker = selectedMarkers.shift();
        //   $(oldMarker.sourceTarget._icon).css("border", "0px solid green");
    }
    selectedMarkers.push(m);
    // $(m.sourceTarget._icon).css("border", "4px solid green");
    // $(m.sourceTarget._icon).css("border-radius", "10px");

    determineSelectedMarkerColor(oldMarker);
}


function determineSelectedMarkerColor(oldMarker) {
    let fromMarker = selectedMarkers[0].sourceTarget;
    setLocationMarkerIcon(fromMarker, 'resources/images/icons/marker-icon-red.png');

    if (selectedMarkers.length > 1) {
        let toMarker = selectedMarkers[1].sourceTarget;
        setLocationMarkerIcon(toMarker, 'resources/images/icons/marker-icon-green.png');
    }

    if (oldMarker) {
        if (oldMarker.target.options.id == -1) {
            setLocationMarkerIcon(oldMarker.sourceTarget, 'resources/images/icons/marker-icon-grey.png')
        } else if (markerIsSupermarket(oldMarker)) {
            setLocationMarkerIcon(oldMarker.sourceTarget, 'resources/images/icons/supermarket.svg')

        } else {
            setLocationMarkerIcon(oldMarker.sourceTarget, 'resources/images/icons/marker-icon-blue.png')
        }
    }

}


function setLocationMarkerIcon(marker, iconUrl) {
    icon = L.icon({
        iconUrl: iconUrl,
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [0, -41]
    });
    marker.setIcon(icon);
}

function getDestinationName(loc) {
    var locName = '';
    for (var i = 0; i < destinationsArray.length; i++) {
        var element = destinationsArray[i];
        if (element.location.lat === loc.lat && element.location.lng === loc.lng) {
            locName = element.name;
        }
    }

    return locName;

}

function quicksort(array) {
    if (array.length <= 1) {
        return array;
    }

    var pivot = array[0];

    var left = [];
    var right = [];

    for (var i = 1; i < array.length; i++) {
        array[i].key < pivot.key ? left.push(array[i]) : right.push(array[i]);
    }

    return quicksort(left).concat(pivot, quicksort(right));
};

function countVehicleProgression(agentList) {
    let transportationHistoryList = [];

    for (let i in agentList) {
        let transportationHistory = [];

        let sortedRouteChangeList = quicksort(agentList[i].routeHasChanged.entry);
        for (let j = 0; j < sortedRouteChangeList.length; j++) {

            let value = sortedRouteChangeList[j].value;

            if (transportationHistory.length == 0) {
                transportationHistory.push(value.vehicle);
            } else {
                let histSize = transportationHistory.length;

                if (transportationHistory[histSize - 1] !== value.vehicle) {
                    transportationHistory.push(value.vehicle);
                }
            }

        }

        transportationHistoryList.push(transportationHistory);
    }

    let res = [];

    transportationHistoryList.forEach(entry => {

        let key = "";
        for (let i = 0; i < entry.length; i++) {
            key += entry[i];
            if (!i == entry.length - 1) {
                key += ";";
            }
        }

        if (res[key] === undefined) {
            res[key] = 1;
        } else {
            res[key] = res[key] + 1;
        }
    })

    let xcoord = [];
    let ycoord = [];

    for (let key in res) {
        xcoord.push(key);
        ycoord.push(res[key]);
    }

    return {kpiname: "Transportation Mode", xcoordinates: xcoord, ycoordinates: ycoord};
}

function countVehicles(agentList) {
    let ycoord = [];
    let xcoord = [];

    for (let i = 0; i < agents.length; i++) {
        if (agentList[i].routeHasChanged.entry[0] != 'undefined') {
            if (xcoord.indexOf(agentList[i].journey.transportationMode[0]) > -1) {
                let index = xcoord.indexOf(agentList[i].journey.transportationMode[0]); // (agents[i].journey.transportationMode[0]
                let currentYval = ycoord[index];
                ycoord[index] = currentYval + 1;
            } else {
                xcoord.push(agentList[i].journey.transportationMode[0]);
                ycoord.push(1);
            }
        }
    }

    let sortObject = {}
    for (let i = 0; i < ycoord.length; i++) {
        sortObject[xcoord[i]] = ycoord[i];
    }

    let ordered = Object.keys(sortObject).sort().reduce(
        (obj, key) => {
            obj[key] = sortObject[key];
            return obj;
        },
        {}
    );

    ycoord = [];
    xcoord = [];
    for (var key in ordered) {
        if (ordered.hasOwnProperty(key)) {
            xcoord.push(key);
            ycoord.push(sortObject[key])
        }
    }


    return {kpiname: "Transportation Mode", xcoordinates: xcoord, ycoordinates: ycoord};
}

function calculateGlobalTravelKilometers(agentList) {
    let globalTravelMeters = 0;
    agentList.forEach(agent => {

        for (index in agent.journey.trips) {

            let trip = agent.journey.trips[index]
            let start = trip.start;
            let end = trip.end;

            let tripDist = calculateDistancesForTrip(agent.routeCoordinates, start, end);
            globalTravelMeters = globalTravelMeters + tripDist;
        }
    });
    return parseFloat(globalTravelMeters / 1000).toFixed(2);
}

function calculateCombustionKilometers(agentList) {
    let combustionMeters = 0;
    agentList.forEach(agent => {

        for (index in agent.journey.trips) {

            let trip = agent.journey.trips[index]
            let start = trip.start;
            let end = trip.end;

            if (agent.journey.transportationMode[index] === "CAR") {
                let tripDist = calculateDistancesForTrip(agent.routeCoordinates, start, end);
                combustionMeters = combustionMeters + tripDist;
            }
            //in case of BIKE, FEET no there is no combustion
            //todo: maybe do something for PT

        }
    });
    return parseFloat(combustionMeters / 1000).toFixed(2);
}

function calculateDistancesForTrip(routeCoordinates, start, end) {
    let coordInTrip = false;
    let dist = 0;

    for (coordIndex in routeCoordinates) {

        let currentCoord = routeCoordinates[coordIndex];
        let nextCoord = routeCoordinates[parseInt(coordIndex) + 1];

        if (nextCoord == undefined) { //for agents that are still on their way and that have not arrived yet
            break;
        }

        if (!coordInTrip) { //find start Loc in Trip
            if (currentCoord.location.lat === start.lat && currentCoord.location.lon === start.lon) {
                coordInTrip = true;
            }
        } else if (coordInTrip) {
            //perform calulation
            let val = distFrom(currentCoord.location.lat, currentCoord.location.lon, nextCoord.location.lat, nextCoord.location.lon);
            dist = dist + val;
            if (nextCoord.location.lat === end.lat && nextCoord.location.lon === end.lon) {
                break;
            }
        }
    }

    return dist;
}




















