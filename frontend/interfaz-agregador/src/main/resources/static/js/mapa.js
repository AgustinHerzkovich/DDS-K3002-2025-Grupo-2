document.addEventListener('DOMContentLoaded', () => {
    const map = L.map('map', {zoomControl: false})
        .setView([-34.6037, -58.3816], 5);

    L.control.zoom({ position: 'topright' }).addTo(map);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
        noWrap: true
    }).addTo(map);

    const customIcon = L.icon({
        iconUrl: 'data:image/svg+xml;base64,' + btoa(`
                    <svg xmlns="http://www.w3.org/2000/svg" width="25" height="41" viewBox="0 0 25 41">
                        <path fill="#DC2626" stroke="#ffffff" stroke-width="1.5"
                              d="M12.5 0C5.596 0 0 5.596 0 12.5c0 1.933.439 3.761 1.217 5.394L12.5 41l11.283-23.106A12.42 12.42 0 0 0 25 12.5C25 5.596 19.404 0 12.5 0z"/>
                        <circle fill="#ffffff" cx="12.5" cy="12.5" r="5"/>
                    </svg>
                    `),
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [0, -41]
    });

    const markersLayer = L.featureGroup().addTo(map);

    hechos.forEach(h => {
        if (h.latitud && h.longitud) {
            const popupContent = `
                            <div style="text-align: center; max-width: 200px;">
                                <b style="word-wrap: break-word;">${h.titulo}</b>
                                <br><br>
                                <a href="/hechos/${h.id}"
                                   class="inline-block px-3 py-1.5 bg-red-700 text-white no-underline rounded text-sm hover:bg-red-800">
                                    Ver hecho
                                </a>
                            </div>
                        `;
            try {
                L.marker([h.latitud, h.longitud], {icon: customIcon})
                    .addTo(markersLayer)
                    .bindPopup(popupContent);
            } catch (e) {
                console.error("Error al crear marcador para hecho:", h, e);
            }
        } else {
            console.warn("Hecho sin ubicación válida:", h);
        }
    });

    if (markersLayer.getLayers().length > 0) {
        map.fitBounds(markersLayer.getBounds().pad(0.1));
    }

    map.on('click', function(e) {
        const lat = e.latlng.lat.toFixed(6);
        const lng = e.latlng.lng.toFixed(6);

        const filtroLatitud = document.getElementById('filtroLatitud');
        const filtroLongitud = document.getElementById('filtroLongitud');

        if (filtroLatitud) filtroLatitud.value = lat;
        if (filtroLongitud) filtroLongitud.value = lng;
    });
})