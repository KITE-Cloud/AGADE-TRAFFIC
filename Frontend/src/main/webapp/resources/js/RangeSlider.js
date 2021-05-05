/*Range Slider*/
function updateSimulationTimeValue(val) {
    document.getElementById('simulationTimeValue').value = val;
}

function updateTickFromValue(val) {
    $('div.timer').html(val);
    timerTick = val;


    initialize(val);
    timer.reset(val);
    timer.stop();

    /*document.getElementById('fromTickLabel').innerHTML = val;*/
}
