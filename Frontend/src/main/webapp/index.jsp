<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<!DOCTYPE html>
<html lang="en">
<head>
    <title>AGADE Traffic Simulation</title>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" type="image/x-icon" href="resources/images/icons/A.PNG"/>

    <!--CSS-->
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.3/dist/leaflet.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet.draw/1.0.4/leaflet.draw.css"
          type="text/css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
    <link rel="stylesheet" href="resources/css/persona.css" type="text/css">
    <link rel="stylesheet" href="resources/css/MarkerCluster.css" type="text/css">
    <link rel="stylesheet" href="resources/css/searchCSS.css" type="text/css">
    <link rel="stylesheet" href="resources/css/customCSS.css" type="text/css">
    <link rel="stylesheet" href="resources/css/tooltipCSS.css" type="text/css">
    <link rel="stylesheet" href="resources/css/preloaderCSS.css" type="text/css" title="preloaderCSS">

    <!--Javascript-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@0.7.0"></script>
    <script src="https://d3js.org/d3.v4.js"></script>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
    <script src="https://unpkg.com/leaflet@1.3.3/dist/leaflet.js"></script>
    <script type="text/javascript"
            src="https://cdnjs.cloudflare.com/ajax/libs/leaflet.draw/1.0.4/leaflet.draw.js"></script>
    <!--script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"
            type="text/javascript"></script-->


</head>
<body onload="animatePreloader()">
<!--body-->

<!--Preloader-->
<div class="loader" id="loader" style="z-index: 9999;">
    <div class="loading-window">
        <img src="resources/images/icons/kite2.png" alt="back" height="75" width="75">
        <h2 id="header "
            style="font-family: Arial, Helvetica, sans-serif; color: #ffffff; float: right; margin-left: 4px; margin-top: 20px; ">
            AGADE Traffic</h2>
        <div class="car">
            <div class="strike"></div>
            <div class="strike strike2"></div>
            <div class="strike strike3"></div>
            <div class="strike strike4"></div>
            <div class="strike strike5"></div>
            <div class="car-detail spoiler"></div>
            <div class="car-detail back"></div>
            <div class="car-detail center"></div>
            <div class="car-detail center1"></div>
            <div class="car-detail front"></div>
            <div class="car-detail wheel"></div>
            <div class="car-detail wheel wheel2"></div>
        </div>
        <div class="text">
            <span id="preloaderLine1">Loading</span><span class="dots">...</span>
            <br><span id="preloaderLine2"></span>
        </div>
    </div>
</div>
<!--Preloader END-->

<!--Sidebar-->
<div class="sidenav" style="text-align: center; font-family: Arial, Helvetica, sans-serif; color: #ffffff;">
    <div id="logoBox">
        <img src="resources/images/icons/kite4.png" id="headerLogo" alt="back" height="75" width="75">
        <h1 id="header" style="font-size: 26px;margin-top: 20px; margin-bottom: 20px; ">AGADE Traffic</h1>
    </div>
    <br>

    <div><br><br><br></div>

    <div id="buttonContainer" style="bottom: 20px; position: absolute;">
        <!--button class="button" onclick="initialize()" id="initializeButton" style="align: center; width: 109px; float:left; margin-left: 11px; margin-bottom: 4px;" >Init</button>
        <button class="button" onclick="go()" id = "goButton" style="align: center; width: 109px; float:left; margin-left: 11px;" >Go</button-->
        <div style="margin-left: 5%; margin-right: 5%; margin-bottom: 20px;">
            <div id="fromTickLabel" class="timer">0</div>
            <input type="range"
                   min="0"
                   max="0"
                   value="0"
                   id="fromTickInput"
                   onchange="updateTickFromValue(this.value);"
                   class="sliderRange"
                   style="background: #2d2d2d;">
        </div>

        <div style="margin-bottom: 20px;">
            <button class="button control-button" id="initializeButton">Init</button>
            <!--button class="button control-button" id="atTickButton">Init at Tick</button-->
            <button class="button control-button" id="goButton">Go</button>
            <button class="button control-button" id="pauseButton" style="width: 230px">Pause</button>
        </div>

        <!--button class="buttonSide" onclick="modalTrafficAction()" id="myBtnTrafficAction" style="align: center;">Traffic Action</button-->
        <button class="buttonSide sidebuttonHalfLeft" onclick="modalSettings()" id="myBtnSettings"
                style="align: center;">Settings
        </button>
        <button class="buttonSide sidebuttonHalfRight" onclick="modalStatistics()" id="myBtn" style="align: center;">
            Statistics
        </button>
        <button class="buttonSide sidebuttonHalfLeft" id="avgTimeBtn" style="align: center;">Avg. Trav.Time</button>
        <button class="buttonSide sidebuttonHalfRight" onclick="populationMode()" id="btn_showStreetPop"
                style="align: center;">Street Pop.
        </button>
        <button class="buttonSide sidebuttonHalfLeft" id="btn_showDestDetails" style="align: center;">Dest. Details
        </button>
        <button class="buttonSide sidebuttonHalfRight" id="btn_showRouteChanges" style="align: center;">Route Changes
        </button>
        <button class="buttonSide sidebuttonHalfLeft" onclick="editMode()" id="myBtnEdit" style="align: center;">Edit
            Mode
        </button>
        <button class="buttonSide sidebuttonHalfRight" onclick="bikeMode()" id="myBtnBikeMode" style="align: center;">
            Bike Mode
        </button>
        <button class="buttonSide" onclick="modalOpen()" id="openButton" style="align: center; margin-top: 20px;">
            Import
        </button>
        <button class="buttonSide" onclick="downloadConfigJson()" id="ExportButton"
                style="align: center;">Export Configurations
        </button>
        <!--button class="buttonSide sidebuttonHalfRight" onclick="downloadSimJson()" id="ExportSimButton"
                style="align: center;">Exp. Sim.
        </button-->
        <!--button class="buttonSide" onclick="clearMap()" id="clearBtn" style="align: center;">Reset Map</button-->
    </div>
</div>

<div class="main" style=" height: 100%;">
    <div class="mapContainer" style="padding: 0em; height: 100%;">
        <div id="mapid" style=" height: 100vh; width: 100%; padding: 0em;"></div>
        <div id="search">
            <input type="text" name="addr" value="" id="addr" size="10" placeholder="Enter Location"/>
            <button type="button" onclick="addr_search();" class="buttonSide"
                    style="width: 51px; height: 20px; font-size: 13px; margin: 0px;">Search
            </button>
            <div id="results" style="margin-right: 35px;"/>
        </div>
    </div>
</div>
<!--Sidebar END-->


<!-- Agent Filters Accordion  -->
<div id="agentFilter">
    <button class="accordion"><img src="resources/images/icons/filter.svg" alt="Agent Filters" width="35" height="25">
    </button>
    <div class="panel">
        <p><b>Filter Agents by</b></p>
        <hr>

        <div id="agentFilterPersona">
        </div>
        <hr>
        <div id="agentFilterOrigin">
            <p>Origin</p>
        </div>
        <hr>
        <div id="agentFilterTargetLocation">
            <p>Target Location</p>
        </div>
        <hr>
        <div id="agentFilterMode">
            <p>Transportation Mode</p>
            <div class="row">
                <div class="col-12 col-sm-6 col-4 col-md-3  col-lg-2"><input type="checkbox" id="targetModeCAR"
                                                                             name="CAR" value="CAR"><label
                        for="targetModeCAR">Car</label></div>
                <div class="col-12 col-sm-6 col-4 col-md-3  col-lg-2"><input type="checkbox" id="targetModeBIKE"
                                                                             name="BIKE" value="BIKE"><label
                        for="targetModeBIKE">Bike</label></div>
                <div class="col-12 col-sm-6 col-4 col-md-3  col-lg-2"><input type="checkbox" id="targetModeFEET"
                                                                             name="FEET" value="FEET"><label
                        for="targetModeFEET">Feet</label></div>
                <div class="col-12 col-sm-6 col-4 col-md-3  col-lg-2"><input type="checkbox"
                                                                             id="targetModePUBLICTRANSPORT"
                                                                             name="PUBLICTRANSPORT"
                                                                             value="PUBLICTRANSPORT"><label
                        for="targetModePUBLICTRANSPORT">Public
                    Trans.</label></div>
            </div>
        </div>
        <hr>
        <div id="agentFilterID">
            <div class="row">
                <div class="col-12 col-sm-6 col-4 col-md-3  col-lg-2"><p>ID</p></div>
                <div class="col-12 col-sm-6 col-4 col-md-3  col-lg-2">
                    <input type="text" name="agentFilterIDInput" value="" id="agentFilterIDInput" size="10"
                           placeholder="Enter Agent ID"/>

                </div>
                <div class="col-12 col-sm-6 col-4 col-md-3  col-lg-2">
                    <button type="button" onclick="addToAgentIDFilterList()" class="buttonSide"
                            style="width: 70px;height: 30px;margin: 0px;border-radius: 4px;">Select
                    </button>
                </div>
                <div class="col-12 col-sm-6 col-4 col-md-3  col-lg-2" id="agentFilterIDError" style="color:red;">
                </div>
            </div>
            <div class="row">
                <div class="col-12" id="agentFilterIDTags">
                </div>
            </div>
        </div>
        <hr>
        <div id="agentFilterButtons">
            <button id="agentFilterALLButton" onclick="applyFiltersToVisualisation(true)" class="button control-button"
                    style="padding:10px; margin-top:2vh; float:left;">
                Reset
            </button>
            <button id="agentFilterCancelButton" onclick="openFilterAccordion(true)" class="button control-button"
                    style="padding:10px; margin-top:2vh;">
                Cancel
            </button>
            <button id="agentFilterApplyButton" onclick="applyFiltersToVisualisation()" class="button control-button"
                    style="padding:10px; margin-top:2vh;">
                Apply
            </button>
        </div>
    </div>
</div>
<!-- Agent Filters Accordion END -->

<!-- Statistics Modal  -->
<div id="myModal" class="modal">
    <div class="modal-content"
         style="background-image: url('resources/images/backgrounds/road3.png'); background-repeat: no-repeat;  -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; overflow-y: auto;">
        <div class="modal-header">
            <h2 class="closeStatistics" style="margin-top: 5px;"><img src="resources/images/icons/back.png" alt="back"
                                                                      height="30" width="26">
            </h2>
            <h2 style="text-align: center;">Statistics</h2>
        </div>

        <div class="modal-body container mb-5">
            <div class="row" style="text-align: center;">
                <div class="col-12"><p style="font-size: 18px;  font-weight: 700; margin-top: 15px;">Filtered KPIs
                    (Filtered Agents)</p></div>
            </div>
            <div class="row" style="text-align: center;">
                <div class="col-6 "><p style="font-size: 18px; margin-top: 15px;" id="statisticTotalNumberOfAgents">Total Number of Agents: 0</p></div>
                <div class="col-6"><p style="font-size: 18px; margin-top: 15px;" id="statisticAvgTravellerUtility">Avg. Traveller Utility: 0</p></div>
                <div class="col-6"><p style="font-size: 18px; margin-top: 15px;" id="statisticCombustionDist">Combustion Distance [km]: 0</p></div>
                <div class="col-6"><p style="font-size: 18px; margin-top: 15px;" id="statisticTotalTravelDist">Global Travel Distance [km]: 0</p></div>
            </div>
            <hr>

            <div class="row" style="text-align: center;">
                <div class="col-12"><p style="font-size: 18px;  font-weight: 700; margin-top: 15px;">Unfiltered KPIs
                    (All Agents)</p></div>
            </div>
            <div class="row" style="text-align: center;">
                <div class="col-6"><p style="font-size: 18px; margin-top: 15px;float: left;">Key Performance Index: </p>
                </div>
                <div class="col-6">
                    <select id="diagramPlot" style="float: left;" onchange="plot()">
                        <option value="" disabled selected>Select your option</option>
                        <option value="Created Agents Per Tick">Created Agents Per Tick</option>
                        <option value="Number Of Moving Agents Per Tick">Number Of Moving Agents Per Tick</option>
                        <option value="Initial Trans. Mode">Initial Trans. Mode</option>
                        <!--option value="Trans. Mode Progression">Trans. Mode Progression</option-->
                    </select>
                </div>
            </div>
            <div class="diagramContainer" style="height: 600px" id="diagramContainerMain">
                <canvas id="chart-area"></canvas>
            </div>

            <hr>
            <div class="row" style="text-align: center;">
                <div class="col-12"><p style="font-size: 18px;  font-weight: 700; margin-top: 15px;">Compare with
                    another simulation</p></div>
            </div>
            <div class="row mb-5" style="text-align: center;">
                <div class="col-12  col-lg-4">
                    <div class="row">
                        <div class="col-6 col-lg-12">
                            <p>Config</p>
                        </div>
                        <div class="col-6 col-lg-12">
                            <input type="file" id="fileCompConfig" style="margin-left:25px;">
                        </div>
                    </div>
                </div>
                <div class="col-12  col-lg-4">
                    <div class="row">
                        <div class="col-6 col-lg-12">
                            <p>Population</p>
                        </div>
                        <div class="col-6 col-lg-12">
                            <input type="file" id="fileCompPopulation" style="margin-left:25px;">
                        </div>
                    </div>
                </div>
                <div class="col-12  col-lg-4">
                    <div class="row">
                        <div class="col-6 col-lg-12">
                            <p>Simulation Result</p>
                        </div>
                        <div class="col-6 col-lg-12">
                            <input type="file" id="fileCompSimResult" style="margin-left:25px;">
                        </div>
                    </div>
                </div>
            </div>
            <div class="row mb-5 ">
                <div class="col-12 text-center">
                    <button style="padding:10px;" onclick=" modalCompare()">Compare</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Statistics Modal END -->

<!-- Statistics Compare Modal  -->
<div id="myModalCompare" class="modal">
    <div class="modal-content"
         style="background-image: url('resources/images/backgrounds/road3.png'); background-repeat: no-repeat;  -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; overflow-y: auto;">
        <div class="modal-header">
            <h2 class="closeCompareModal" style="float:left; margin-top: 5px;">
                <img src="resources/images/icons/back.png" id="back" alt="back" height="30" width="26">
            </h2>
            <h2 style="text-align: center;">Compare</h2>
        </div>

        <div class="modal-body container-fluid mb-5 text-center" style="font-size: 18px; max-width:100vw;">

            <div class="row mt-5 mb-5">
                <div class="container">
                    <div class="row">
                        <div class="col-4"><p style="font-weight: 700;"> KPI </p></div>
                        <div class="col-4"><p style="font-weight: 700;">Simulation A</p></div>
                        <div class="col-4"><p style="font-weight: 700;">Simulation B</p></div>
                    </div>
                    <div class="row">
                        <div class="col-4"><p>Total Number of Agents: </p></div>
                        <div class="col-4"><p id="compATotalNumberOfAgents">0</p></div>
                        <div class="col-4"><p id="compBTotalNumberOfAgents">0</p></div>
                    </div>
                    <div class="row">
                        <div class="col-4"><p>Global Travel Distance [km]: </p></div>
                        <div class="col-4"><p id="compATotalTravelDist">0</p></div>
                        <div class="col-4"><p id="compBTotalTravelDist">0</p></div>
                    </div>
                    <div class="row">
                        <div class="col-4"><p>Combustion Distance [km]: </p></div>
                        <div class="col-4"><p id="compACombustionDist">0</p></div>
                        <div class="col-4"><p id="compBCombustionDist">0</p></div>
                    </div>
                    <div class="row">
                        <div class="col-4"><p>Avg. Traveller Utility: </p></div>
                        <div class="col-4"><p id="compAAvgTravellerUtility">0</p></div>
                        <div class="col-4"><p id="compBAvgTravellerUtility">0</p></div>
                    </div>
                </div>
            </div>
            <hr>
            <div class="row mb-5">
                <div class="container">
                    <div class="row">
                        <div class="col-6">
                            <p style="font-size: 18px; margin-top: 15px;float: right;">KPI: </p>
                        </div>
                        <div class="col-6">
                            <select id="diagramPlotCompare" style="float: left;" onchange="plotCompare()">
                                <option value="" disabled>Select your option</option>
                                <option value="Created Agents Per Tick">Created Agents Per Tick</option>
                                <option value="Number Of Moving Agents Per Tick">Number Of Moving Agents Per Tick
                                </option>
                                <option value="Initial Trans. Mode" selected>Initial Trans. Mode</option>
                                <!--option value="Trans. Mode Progression">Trans. Mode Progression</option-->
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row mb-5">
                <div class="col-6"><p style="font-weight: 700;">Simulation A</p></div>
                <div class="col-6"><p style="font-weight: 700;">Simulation B</p></div>
                <div class="col-6 diagramContainer" style="height: 600px" id="diagramContainerCompareA">
                    <canvas id="canvasA"></canvas>
                </div>
                <div class="col-6 diagramContainer" style="height: 600px" id="diagramContainerCompareB">
                    <canvas id="canvasB"></canvas>
                </div>
            </div>
            <hr>
            <div class="row mt-5 mb-5">
                <div class="container text-center">
                    <div class="row">
                        <div class="col-12"><p style="font-weight: 700;">Preferences</p></div>
                    </div>
                    <div class="row">
                        <div class="col-6"><p style="font-weight: 700;">Number of Agents with differing preferences</p>
                        </div>
                        <div class="col-6"><p id="compNumAgentsChangedPref"></p></div>
                    </div>
                    <div class="row">
                        <div class="col-12"><p>Change Details</p></div>
                    </div>
                    <div class="row" id="prefChangedAgentAcc">
                    </div>

                    <div class="row mt-5">
                        <div class="col-12"><p style="font-weight: 700;">Journey</p></div>
                    </div>
                    <div class="row">
                        <div class="col-6"><p style="font-weight: 700;">Number of Agents with differing travel
                            distance</p></div>
                        <div class="col-6"><p id="compNumAgentsChangedDist"></p></div>
                    </div>
                    <div class="row">
                        <div class="col-12"><p>Change Details</p></div>
                    </div>
                    <div class="row mb-4" id="distChangedAgentAcc">
                    </div>
                    <div class="row">
                        <div class="col-6"><p style="font-weight: 700;">Number of Agents with differing initial
                            transportation mode</p></div>
                        <div class="col-6"><p id="compNumAgentsChangedInitMode"></p></div>
                    </div>
                    <div class="row">
                        <div class="col-12"><p>Change Details</p></div>
                    </div>
                    <div class="row mb-4" id="initModeChangedAgentAcc">
                    </div>
                </div>
            </div>
            <hr>
            <div class="row mb-5">
                <div class="container">
                    <div class="row">
                        <div class="col-12"><p style="font-weight: 700;">Target Locations Visited</p></div>
                        <div class="col-12 diagramContainer" style="height: 600px" id="diagramContainerCompareTargets">
                            <canvas id="canvasTargetsLoc"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row mt-5 mb-5">
                <div class="col-12"><p style="font-weight: 700;">Target Locations Emigrants Graph</p></div>
                <div class="col-12"><p>Hover over a node to see where agents have left to instead.</p></div>
                <div class="col-12" id="emigrantsGraphContainer">
                    <div id="emigrantsGraph"></div>
                </div>
                <div class="container">
                    <div class="row">
                        <p class="col-12">Details</p>
                    </div>
                    <div class="row" id="emigrantsDetails">

                    </div>
                    <div class="row" style="min-height: 300px;"></div>
                </div>
            </div>
            <hr>
        </div>
    </div>
</div>
<!-- Statistics Compare Modal END -->

<!-- MarketEmigrantsDetails Modal  -->
<div id="myModalMarketEmigrants" class="modal">
    <div class="modal-content"
         style="background-color:white; background-repeat: no-repeat;  -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; overflow-y: auto; width: 70vw;  left: 15vw;">
        <div class="modal-header">
            <h2 id="marketEmigrantsModalTitle" style="text-align: center;">0 Emigrants from X</h2>
        </div>
        <div class="modal-body container-fluid" id="marketEmigrantsModalBody"
             style="text-align: center; padding-bottom: 3vh; padding: 20px;">

            <div class="row mt-5 mb-5">
                <div class="container">
                    <div class="row">
                        <div class="col-4"><p style="font-weight: 700;"> KPI </p></div>
                        <div class="col-4"><p style="font-weight: 700;">Simulation A</p></div>
                        <div class="col-4"><p style="font-weight: 700;">Simulation B</p></div>
                    </div>
                    <div class="row">
                        <div class="col-4"><p>Global Travel Distance [km]: </p></div>
                        <div class="col-4"><p id="emDetailCompATotalTravelDist">0</p></div>
                        <div class="col-4"><p id="emDetailCompBTotalTravelDist">0</p></div>
                    </div>
                    <div class="row">
                        <div class="col-4"><p>Combustion Distance [km]: </p></div>
                        <div class="col-4"><p id="emDetailCompACombustionDist">0</p></div>
                        <div class="col-4"><p id="emDetailCompBCombustionDist">0</p></div>
                    </div>
                    <div class="row">
                        <div class="col-4"><p>Avg. Traveller Utility: </p></div>
                        <div class="col-4"><p id="emDetailCompAAvgTravellerUtility">0</p></div>
                        <div class="col-4"><p id="emDetailCompBAvgTravellerUtility">0</p></div>
                    </div>
                </div>
            </div>
            <hr>
            <div class="row mt-5 mb-5">
                <div class="container">
                    <div class="row">
                        <div class="col-12"><p style="font-weight: 700;">Preferences Changes</p></div>
                    </div>
                    <div class="row" id="emDetailPrefChangedAgentAcc">
                    </div>
                </div>
            </div>
            <hr>
            <div class="row mt-5 mb-5">
                <div class="col-12" style="height: 400px" id="emDetailsOriginPieContainer">
                    <canvas id="originPie"></canvas>
                </div>
            </div>
            <hr>
            <div class="row mt-5 mb-5">
                <div class="col-12" style="height: 400px" id="emDetailsPersonaPieContainer">
                    <canvas id="personaPie"></canvas>
                </div>
            </div>
            <hr>
            <div class="row mt-5 mb-5">
                <div class="col-12"><p style="font-weight: 700;">Agent IDs</p></div>
                <div class="col-12" id="emDetailAgentIDTags"></div>
            </div>
            <button id="marketEmigrantsModalOKButton" class="button control-button"
                    style="padding:10px; margin-top:2vh;">
                Close
            </button>
        </div>
    </div>
</div>
<!-- MarketEmigrantsDetails Modal END -->

<!-- Settings Modal -->
<div id="myModalSettings" class="modal">
    <div class="modal-content"
         style="background-image: url('resources/images/backgrounds/road3.png'); background-repeat: no-repeat;  -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; overflow-y: auto;">
        <div class="modal-header">
            <h2 class="closeSettings" style="margin-top: 5px;"><img src="resources/images/icons/back.png" alt="back"
                                                                    height="30" width="26">
            </h2>
            <h2 style="text-align: center;">Settings</h2>
        </div>

        <div class="modal-body">
            <h3 style="margin-left:25px; margin-top: 30px;">Simulation Time</h3>
            <div class="sliderInSettings" style="width: 35vw;">
                <div style="margin-left: 10%; margin-right: 10%; margin-bottom: 10px;">Time to Simulate in hours:
                    <input type="text" id="simulationTimeValue" value="1"
                           style="float: right;  width: 50px; text-align: center;" disabled>
                </div>
                <div class="slidecontainer" style="margin-left: 5%; margin-right: 5%;">
                    <input type="range" min="1" max="24" onchange="updateSimulationTimeValue(this.value);" value="1"
                           class="sliderRange" style="background: #2d2d2d;">
                </div>
            </div>


            <div><br><br></div>


            <!--h3 style="margin-left:25px;">Agent Types</h3>
            <p style="margin-left:25px">Input amount of drivers for a driver type in percent (10 = 10%). If the input
                values
                do not add up to 100, we will calculate the percentage of your values from their total sum. </p>
            <table id="agentTypeInput" style="margin-left:25px;"></table-->

            <!--Persona Carousel Start-->
            <h3 style="margin-left:25px;margin-top: 30px;">Traveller Persona</h3>
            <p style="margin-left:25px">Traveller persona describe different groups of travellers and their behaviour.
                You can create you own or edit exisiting persona for your simulation
                If you want to include a group of travellers to your simulation input the amount of drivers for a
                traveller persona in percent (10 = 10%).
                If the input values do not add up to 100, the percentage of the values are calculated based on their
                total sum. </p>
            <button style="padding:10px; margin-left:25px;" onclick="openModalPersonaEdit(-1)">Create New Persona
            </button>
            <div class="customPersonaContainer">
                <div class="card-container-base">
                    <div class="card-carousel">
                        <div class="nav-arrow-container pos-left">
                            <div class="nav-arrow">
                                <i class="fas fa-arrow-left"></i>
                            </div>
                        </div>
                        <div class="card-container" id="personaContainer">

                            <!--Cards generated here-->

                        </div>
                        <div class="nav-arrow-container pos-right">
                            <div class="nav-arrow">
                                <i class="fas fa-arrow-right"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--Persona Carousel End-->


            <!--DEMAND MODELLING-->
            <div id="destinationMatSetting" style="margin-top: 25px;">
                <div class="matrixContainer" style="height:450px; display:block;">

                    <h3 style="margin-left: 25px;">Demand Modelling</h3>
                    <p style="margin-left:25px">First you need to add destinations on the map. Once destinations have
                        been added, they will also be added to the demand modelling matrices.
                        You can model demand using either an activity based approach or using an Origin/Destination
                        matrix.
                    </p>

                    <div style="overflow: hidden; margin-top: 9px; margin-left:25px;  display: block;">
                        <!--button style="padding:10px;" onclick="addDestination()"> Add Destination</button-->
                        <button style="padding:10px;" onclick="fillRandom()"> Randomize Matrices</button>
                        <!--button onclick="openRemoveSetting();"> Delete a Destination</button-->
                    </div>

                    <div class="tab">
                        <button class="demand tablinks" name="Activity" onclick="openTab(event, 'Activity')"
                                id="defaultOpen">Activity Matrix
                        </button>
                        <button class="demand tablinks" name="OD" onclick="openTab(event, 'OD')">Origin Destination
                            Matrix
                        </button>
                    </div>

                    <div id="Activity" class="tabcontent">
                        You can configure the <b>number of travellers starting from a location</b>
                        by specifying the amount in the corresponding input field in the table. Time distribution is
                        determined based on Poisson.
                        <h4 style="margin-left:25px; margin-top: 30px;">Start Locations</h4>
                        <table id="dynamicTableActivity" style="margin: 25px;width: min-content;">
                            <tr id="dynamicTableActivityHeader" style="overflow: auto;">
                                <th></th>
                            </tr>
                            <tbody style="height:350px; overflow-y: auto;">
                            <tr id="dynamicTableActivityContent">
                                <th></th>
                            </tr>
                            </tbody>

                        </table>
                    </div>

                    <div id="OD" class="tabcontent">
                        You can configure the <b>number of travellers that are moving from a start location (row) to a
                        target location (column)</b>
                        by specifying the amount in the corresponding input field in the table.

                        <h4 style="margin-left:25px; margin-top: 30px;">From/To</h4>
                        <table id="dynamicTable" style="margin: 25px; width: min-content;">
                            <tr id="dynamicTableHeader" style="overflow: auto; ">
                                <th></th>
                            </tr>
                            <tbody id="dynamicTableContent" style="height:350px; overflow-y: auto;">

                            </tbody>
                        </table>

                        <h4 style="margin-left:25px;">Desired Target Time</h4>
                        <div id="DesiredArrivalTime_SettingsArea" style="margin-left:25px;">
                        </div>
                    </div>


                </div>
            </div>
            <!--DEMAND MODELLING END-->

        </div>
    </div>
</div>
<!-- Settings Modal END-->


<!-- Table Settings Modal -->
<div id="myModalTableSettings" class="modal2">
    <div class="modal-content"
         style="background-image: url('resources/images/backgrounds/road3.png'); background-repeat: no-repeat;  -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; overflow-y: auto;">
        <div class="modal-header">
            <h2 class="closeSettings">
                <img src="resources/images/icons/back2.png" id="back3" alt="back" height="30" width="26">
            </h2>
            <h2 style="text-align: center;">Add Destination by address search</h2>
        </div>

        <div class="modal-body">
            <div id="tabSettingsBox">
                <p>Placeholder for dynamic content</p>
            </div>
        </div>

    </div>
</div>
<!-- Table Settings Modal END-->

<!-- File Modal  -->
<div id="myModalOpen" class="modal">
    <div class="modal-content"
         style="background-image: url('resources/images/backgrounds/road3.png'); background-repeat: no-repeat;  -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; overflow-y: auto;">
        <div class="modal-header">
            <h2 class="closeOpenModal" style="float:left; margin-top: 5px;">
                <img src="resources/images/icons/back.png" id="back" alt="back" height="30" width="26">
            </h2>
            <h2 style="text-align: center;">Import</h2>
        </div>

        <div class="modal-body container">
            <h2 style="margin-left: 25px; margin-top: 30px;">Upload a File from your Computer</h2>
            <p style="margin-left:25px">You can upload a configurations file that you exported in a previous session as
                a
                json file. </p>
            <input type="file" id="files" style="margin-left:25px;">

            <p style="margin-left:25px;margin-top: 30px;">You can upload a population file compatible with your
                previously imported configuration.
                Please make sure that all locations and persona used in the population are also included in your
                configuration </p>
            <input type="file" id="filesPopulation" style="margin-left:25px;">

            <p style="margin-left:25px;margin-top: 30px;">You can upload a simulation result file that you exported in a
                previous session
                as a
                json file. </p>
            <input type="file" id="fileSimResult" style="margin-left:25px;">

        </div>
    </div>
</div>
<!-- FileUpload Modal END -->

<!-- TrafficAction Modal  -->
<div id="myModalTrafficAction" class="modal">
    <div class="modal-content"
         style="background-image: url('resources/images/backgrounds/road3.png'); background-repeat: no-repeat;  -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; overflow-y: auto; width: 50vw; height: auto; left: 25vw; top: 10vh;">
        <div class="modal-header">
            <h2 style="text-align: center;">Select Traffic Action</h2>
        </div>

        <div class="modal-body">
            <div class="container">
                <div class="row">
                    <div class="col-3 p-0">
                        <div class="to-blocked to-wrapper" onclick="closeTrafficModal(this.className)">
                            <img class="modalSelectionItemIcon" src="resources/images/trafficSigns/block.png">
                        </div>
                    </div>
                    <div class="col-3 p-0">
                        <div class="to-10 to-wrapper" onclick="closeTrafficModal(this.className)">
                            <img class="modalSelectionItemIcon" src="resources/images/trafficSigns/10.png">
                        </div>
                    </div>
                    <div class="col-3 p-0">
                        <div class="to-20 to-wrapper" onclick="closeTrafficModal(this.className)">
                            <img class="modalSelectionItemIcon" src="resources/images/trafficSigns/20.png">
                        </div>
                    </div>
                    <div class="col-3 p-0">
                        <div class="to-30 to-wrapper" onclick="closeTrafficModal(this.className)">
                            <img class="modalSelectionItemIcon" src="resources/images/trafficSigns/30.png">
                        </div>
                    </div>
                    <div class="col-3 p-0">
                        <div class="to-50 to-wrapper" onclick="closeTrafficModal(this.className)">
                            <img class="modalSelectionItemIcon" src="resources/images/trafficSigns/50.png">
                        </div>
                    </div>
                    <div class="col-3 p-0">
                        <div class="to-60 to-wrapper" onclick="closeTrafficModal(this.className)">
                            <img class="modalSelectionItemIcon" src="resources/images/trafficSigns/60.png">
                        </div>
                    </div>
                    <div class="col-3 p-0">
                        <div class="to-70 to-wrapper" onclick="closeTrafficModal(this.className)">
                            <img class="modalSelectionItemIcon" src="resources/images/trafficSigns/70.png">
                        </div>
                    </div>
                    <div class="col-3 p-0">
                        <div class="to-80 to-wrapper" onclick="closeTrafficModal(this.className)">
                            <img class="modalSelectionItemIcon" src="resources/images/trafficSigns/80.png">
                        </div>
                    </div>
                    <div class="col-3 p-0">
                        <div class="to-100 to-wrapper" onclick="closeTrafficModal(this.className)">
                            <img class="modalSelectionItemIcon" src="resources/images/trafficSigns/100.png">
                        </div>
                    </div>
                    <div class="col-3 p-0">
                        <div class="to-120 to-wrapper" onclick="closeTrafficModal(this.className)">
                            <img class="modalSelectionItemIcon" src="resources/images/trafficSigns/120.png">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Traffic Action Modal END -->


<!--  Average Traffic Time Modal  -->
<div id="myModalAvgTimeStatistics" class="modal">
    <div class="modal-content" id="myModalAvgTimeStatisticsContent"
         style="background-image: url('resources/images/backgrounds/road3.png'); background-repeat: no-repeat;  -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; overflow-y: auto; width: 30vw; ">
        <div class="modal-header" id="avgTravelTimeModalHeader">
            <h2 class="closeAvgTimeModal" style="float:left;  margin-top: 5px;">
                <img src="resources/images/icons/back.png" id="back" alt="back" height="30" width="26"></h2>
            <h2 style="text-align: center;">Average Travel Time</h2>
        </div>

        <div class="modal-body" id="avgTravelTimeModal">

        </div>
    </div>
</div>
<!-- Average Traffic Time END -->

<!--  Destination Details Modal  -->
<div id="myModalDestinationDetails" class="modal">
    <div class="modal-content" id="myModalDestinationDetailsContainer"
         style="background-image: url('resources/images/backgrounds/road3.png');
         background-repeat: no-repeat;  -webkit-background-size: cover;
         -moz-background-size: cover; -o-background-size: cover;
         background-size: cover; overflow-y: auto; width: 30vw; ">

        <div class="modal-header" id="myModalDestinationDetailsHeader">
            <h2 class="closeMyModalDestinationDetails" style="float:left;  margin-top: 5px;"><img
                    src="resources/images/icons/back.png"
                    id="back" alt="back"
                    height="30" width="26"></h2>

            <h2 style="text-align: center;">Destination Details</h2>
        </div>

        <div class="modal-body" id="myModalDestinationContent">
            <p>Hallo</p>
        </div>
    </div>
</div>
<!-- Destination Details Modal END -->

<!-- MarkerProps Modal  -->
<div id="myModalMarkerProps" class="modal">
    <div class="modal-content"
         style="background-image: url('resources/images/backgrounds/road3.png'); background-repeat: no-repeat;  -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; overflow-y: auto; width: 40vw; height: auto; left: 30vw; top: 20vh;">
        <div class="modal-header">
            <h2 style="text-align: center;">Please Enter Location Properties</h2>
        </div>

        <div class="modal-body" style="text-align: center; padding-bottom: 3vh;">

            <h4 class="pt-2">Location Name</h4>
            <input id="markerName" type="text" name="markerName" value="" placeholder="Location A">

            <h4 class="pt-2">Point Of Interest</h4>
            <div id="markerPoiType"></div>

            <h4 class="pt-2">Parking Capacity</h4>
            <input id="markerCapacity" type="number" name="markerName">

            <h4 class="pt-2">Parking Radius</h4>
            <input id="markerParkingArea" type="number" name="markerName">

            <br>
            <button id="markerPropsCancelButton" class="button control-button buttonSecondary"
                    style="padding:10px; margin-top:2vh;">Cancel
            </button>
            <button id="markerPropsButton" class="button control-button" style="padding:10px; margin-top:2vh;">Confirm
            </button>

        </div>
    </div>
</div>
<!-- MarkerProps Modal END -->


<!-- Notification Modal  -->
<div id="myModalNotification" class="modal">
    <div class="modal-content"
         style="background-color:white; background-repeat: no-repeat;  -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; overflow-y: auto; width: 40vw; height: auto; left: 30vw; top: 20vh;">
        <!--div class="modal-header">
            <h2 style="text-align: center;">Please Enter Location Properties</h2>
        </div-->
        <div class="modal-body" id="notificationModalBody"
             style="text-align: center; padding-bottom: 3vh; padding: 20px;">
            <button id="notificationModalOKButton" class="button control-button" style="padding:10px; margin-top:2vh;">
                OK
            </button>
        </div>
    </div>
</div>
<!-- Notification Modal END -->

<!-- Notification Modal  -->
<div id="myModalNotificationPopulation" class="modal">
    <div class="modal-content"
         style="background-color:white; background-repeat: no-repeat;  -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; overflow-y: auto; width: 40vw; height: auto; left: 30vw; top: 20vh;">
        <div class="modal-body" id="notificationPopulationModalBody"
             style="text-align: center; padding-bottom: 3vh; padding: 20px;">
            <h4>Do you want to create Agents from imported Population or generate new Agents?</h4>
            <button id="notificationPopulationModalUsePopButton" class="button control-button"
                    style="padding:10px; margin-top:2vh;">
                Population
            </button>
            <button id="notificationPopulationModalGenerateButton" class="button control-button"
                    style="padding:10px; margin-top:2vh;">
                Generate
            </button>
        </div>
    </div>
</div>
<!-- Notification Modal END -->

<!-- personaEditModal  -->
<div id="myModalPersonaEdit" class="modal">
    <div class="modal-content"
         style="background-image: url('resources/images/backgrounds/road3.png'); background-repeat: no-repeat;  -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; overflow-y: auto; width: 60vw; height: auto; max-height: 90vh; left: 20vw; top: 5vh;">
        <div class="modal-header">
            <h2 style="text-align: center;">Persona Details</h2>
        </div>

        <div class="modal-body" style="text-align: center; padding: 30px;">
            <div class="container">
                <div class="row mb-4">
                    <div class="col-6" id="personaEditModalPic"></div>
                    <div class="col-6">
                        <div class="row h-100">
                            <div class="col-4">
                                <label for="personaEditModalDescription">
                                    <p>Description</p>
                                </label>
                            </div>
                            <div class="col-8">
                                <textarea class="w-100 h-100" type="text" value=""
                                          name="personaEditModalDescription"
                                          id="personaEditModalDescription"> </textarea>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-6">
                        <div class="row">
                            <div class="col-4">
                                <label for="personaEditModalID">ID</label>
                            </div>
                            <div class="col-8">
                                <input class="w-100" type="text" value="" name="personaEditModalID"
                                       id="personaEditModalID" disabled/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-4">
                                <label for="personaEditModalName">Name</label>
                            </div>
                            <div class="col-8">
                                <input class="w-100" type="text" value="" name="personaEditModalName"
                                       id="personaEditModalName"/>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-4" id="personaEditModalGenderLabel"></div>
                            <div class="col-8" id="personaEditModalGenderContainer">
                            </div>
                        </div>

                    </div>
                    <div class="col-6">
                        <div class="row">
                            <div class="col-4" id="personaEditModalEducationLabel"></div>
                            <div class="col-8" id="personaEditModalEducationContainer">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-4" id="personaEditModalOccupationLabel"></div>
                            <div class="col-8 " id="personaEditModalOccupationContainer"></div>
                        </div>
                        <div class="row">
                            <div class="col-4" id="personaEditModalMaritalStatusLabel"></div>
                            <div class="col-8" id="personaEditModalMaritalStatusContainer">
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row mt-4">
                    <p class="w-100 font-weight-bold">Age</p>
                </div>
                <div class="row " id="personaEditModalAge">

                </div>

                <div class="row mt-4">
                    <p class="w-100 font-weight-bold">Health</p>
                </div>
                <div class="row " id="personaEditModalProps">
                    <div class="col-6 personaEditModalPropsLeftCol"></div>
                    <div class="col-6 personaEditModalPropsRightCol"></div>
                </div>

                <div class="row mt-4">
                    <p class="w-100 font-weight-bold">Shopping Basket</p>
                </div>
                <div class="row " id="personaEditModalShopping">
                    <div class="col-6 personaEditModalShoppingLeftCol"></div>
                    <div class="col-6 personaEditModalShoppingRightCol"></div>
                </div>

                <div class="row mt-4">
                    <label class="w-100 font-weight-bold" for="personaEditModalPercentage">Percentage</label>
                </div>
                <div class="row w-100" style="margin-left:0px">
                    <input style="width: 230px; display:block; margin:auto; text-align:center;" type="number" min="0"
                           max="100" value="0" name="personaEditModalPercentage" id="personaEditModalPercentage"/>
                </div>

                <div class="row mt-4">
                    <div class="w-100">
                        <button id="personaEditCancelButton" class="button control-button buttonSecondary"
                                style="padding:10px; margin-top:2vh;">
                            Cancel
                        </button>

                        <button id="personaEditButton" class="button control-button"
                                style="padding:10px; margin-top:2vh;">Save
                        </button>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<!-- personaEditModal END -->

<!-- Profile Picture Modal  -->
<div id="myModalProfilePictures" class="modal">
    <div class="modal-content"
         style="background-image: url('resources/images/backgrounds/road3.png'); background-repeat: no-repeat;  -webkit-background-size: cover; -moz-background-size: cover; -o-background-size: cover; background-size: cover; overflow-y: auto; width: 60vw; height: 90vh; left: 20vw; top: 5vh;">
        <div class="modal-header">
            <h2 style="text-align: center;">Select Profile Picture</h2>
        </div>

        <div class="modal-body">
            <div class="grid-container" id="myModalProfilePicturesBody">

            </div>
        </div>
    </div>
</div>
<!-- Profile Picture Modal END -->


<!--Edit Mode Color Legend START-->
<ul class="legend">
    <li><span class="smaller10"></span> <= 10</li>
    <li><span class="smaller20"></span> <= 20</li>
    <li><span class="smaller30"></span> <= 30</li>
    <li><span class="smaller50"></span> <= 50</li>
    <li><span class="smaller60"></span> <= 60</li>
    <li><span class="smaller70"></span> <= 70</li>
    <li><span class="smaller80"></span> <= 80</li>
    <li><span class="smaller100"></span> <= 100</li>
    <li><span class="smaller120"></span> <= 120</li>
    <li><span class="greater120"></span> > 120</li>
</ul>
<!--Edit Mode Color Legend END-->


<!--Javascript-->
<script type="text/javascript" src="resources/js/demo.js"></script>
<script type="text/javascript" src="resources/js/leaflet.markercluster.js"></script>
<script type="text/javascript" src="resources/js/Utilities.js"></script>
<script type="text/javascript" src="resources/js/MovingMarker.js"></script>
<script type="text/javascript" src="resources/js/Modal.js"></script>
<script type="text/javascript" src="resources/js/Preloader.js"></script>
<script type="text/javascript" src="resources/js/Simulation.js"></script>
<script type="text/javascript" src="resources/js/Plot.js"></script>
<script type="text/javascript" src="resources/js/InitialMap.js"></script>
<script type="text/javascript" src="resources/js/AddressSearch.js"></script>
<script type="text/javascript" src="resources/js/tableMaker.js"></script>
<script type="text/javascript" src="resources/js/RangeSlider.js"></script>
<script type="text/javascript" src="resources/js/editMode.js"></script>
<script type="text/javascript" src="resources/js/StreetPopulation.js"></script>
<script type="text/javascript" src="resources/js/RouteChangesHandler.js"></script>
<script type="text/javascript" src="resources/js/bikePath.js"></script>
<script type="text/javascript" src="resources/js/persona.js"></script>
<script type="text/javascript" src="resources/js/Tab.js"></script>
<script type="text/javascript" src="resources/js/filter.js"></script>


<script>


</script>


</body>
</html>
