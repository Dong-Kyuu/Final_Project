window.initMap = function () {
    const map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 47.931373223415115, lng: 8.040125385600229 },
        zoom: 5
    });
    console.log("Map initialized");

    const cities = [
        { label: "A", name: "London", lat: 51.50758961951471, lng: -0.14418159204325737 },
        { label: "B", name: "Paris", lat: 48.85814785824687, lng: 2.344285514464699 },
        { label: "C", name: "Strasbourg", lat: 48.572906962495715, lng: 7.750060085610563 },
        { label: "D", name: "Interlaken", lat: 46.68598660409454,  lng: 7.863097661369451 },
        { label: "E", name: "Milan", lat: 45.46824230104158,  lng: 9.181162398031773 },
        { label: "F", name: "Munchen", lat: 48.141048872624445,  lng: 11.562423439680797 },
        { label: "G", name: "Prague", lat: 50.07453854318431,  lng: 14.431280372357937 }
    ];

    const bounds = new google.maps.LatLngBounds();
    const infoWindow = new google.maps.InfoWindow();

    cities.forEach(({ label, name, lat, lng }) => {
        const marker = new google.maps.Marker({
            position: {lat, lng},
            label,
            map
        });
        bounds.extend(marker.position);

        marker.addListener("click", () => {
            map.panTo(marker.position);
            infoWindow.setContent(name);
            infoWindow.open({
                anchor: marker,
                map
            });
        });
    });

    const flightPath = new google.maps.Polyline({
        path: cities,
        geodesic: true,
        strokeColor: "#FF0000",
        strokeOpacity: 1.0,
        strokeWeight: 2,
    });

    flightPath.setMap(map);

    map.fitBounds(bounds);

};