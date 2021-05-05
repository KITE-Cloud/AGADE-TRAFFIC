function animatePreloader() {

    if ($(window).width() < 960) {
        openModalNotification('This application does not work for devices with a screen width of less than 960px. Please use another device.');
    } else {
        setTimeout(function () {
            closeLoadingScreen();
        }, 1000);

        if (isDemo) {
            openDemoNotification()
        } else {
            getOntologyProps();
        }
        updatePersonaCards();
    }
}


function openLoadingScreen(msgLine1, msgLine2) {
    if (msgLine1 != null)
        document.getElementById("preloaderLine1").innerHTML = msgLine1;

    if (msgLine2 != null)
        document.getElementById("preloaderLine2").innerHTML = msgLine2;


    $('link[title="preloaderCSS"]')[0].disabled = false;
    $(".loader").css({"display": "block"});
}

function closeLoadingScreen() {
    $('link[title="preloaderCSS"]')[0].disabled = true;
    $(".loader").css({"display": "none"});
}