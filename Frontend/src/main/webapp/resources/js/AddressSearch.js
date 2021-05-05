function addr_search() {
    document.getElementById('results').style.visibility = 'visible';
    document.getElementById('search').style.height = 'auto';
    var inp = document.getElementById("addr");

    $.getJSON('https://nominatim.openstreetmap.org/search?format=json&limit=5&q=' + inp.value, function (data) {
        var items = [];

        $.each(data, function (key, val) {
            items.push(
                "<li><a href='#' onclick='chooseAddr(" +
                val.lat + ", " + val.lon + ");return false;'>" + val.display_name +
                '</a></li>'
            );
        });

        $('#results').empty();
        if (items.length != 0) {
            $('<p>', {html: "Search results:"}).appendTo('#results');
            $('<ul/>', {
                'class': 'my-new-list',
                html: items.join('')
            }).appendTo('#results');
        } else {
            $('<p>', {html: "No results found"}).appendTo('#results');
        }
    });
}

$("#mapid").on('click', function () {
    document.getElementById('results').style.visibility = 'hidden';
    document.getElementById('search').style.height = '42px';
});

function chooseAddr(lat, lng, type) {
    let location = new L.LatLng(lat, lng);
    mymap.panTo(location);

    if (type == 'city' || type == 'administrative') {
        mymap.setZoom(11);
    } else {
        mymap.setZoom(13);
    }
    document.getElementById('results').style.visibility = 'hidden';
    document.getElementById('search').style.height = '42px';
}

$("#addr").on('keyup', function (e) {
    if (e.keyCode == 13) {
        addr_search();
    }
});

