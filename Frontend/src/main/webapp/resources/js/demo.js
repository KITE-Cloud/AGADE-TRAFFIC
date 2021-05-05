var isDemo = false;
var defaultSimulationDirectory = "resources\\example\\"
var defaultSimulationConfig = "Configuration.json";
var defaultSimulationPopulation = "Population.json";
var defaultSimulation = "SimulationResult.json";

function openDemoNotification() {
    let msg = 'AGADE Traffic has been started in demo mode. Some options have been disabled. ' +
        'You can import provided example simulations from the Downloads section or load the default example. ' +
        'Once simulation data is loaded, press \'Go\' to animate traveller movement or use the \'Statistics\' functions to have a look at calculated KPIs. ' +
        'You can also use filters to look at specific groups of travellers.'

    let importButton = '<button  class="button control-button" onclick="demoImport()"  style="padding:10px; margin-top:2vh;margin-right: 0.5em;">Import</button>';
    let defaultButton = '<button id="demoExample" class="button control-button" onclick="loadDefaultExample()"  style="padding:10px; margin-top:2vh; ">Default</button>';
    let buttons = importButton + defaultButton;

    openModalNotification(msg, undefined, buttons, 'demoExample');
}

function loadDefaultExample() {
    document.getElementById('myModalNotification').style.display = "none";
    openLoadingScreen("Processing")
    //import config
    $.getJSON(defaultSimulationDirectory + defaultSimulationConfig, function (data) {
        initConfig(data);
    });
    //import population
    $.getJSON(defaultSimulationDirectory + defaultSimulationPopulation, function (data) {
        population = data;
    });
    $.getJSON(defaultSimulationDirectory + defaultSimulation, function (data) {
        simulation = data;
        processSimulationJson(simulation.agents);
        closeLoadingScreen()
        openModalNotification("Default example has been successfully imported and initialised.")
    });

}

function demoImport() {
    modalOpen()
    document.getElementById('myModalNotification').style.display = "none";
}