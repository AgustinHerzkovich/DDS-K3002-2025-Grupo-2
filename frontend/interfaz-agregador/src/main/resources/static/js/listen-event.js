function listenPanelToggle(toggleBtn, content, separator, chevron) {
    let isExpanded = true;

    toggleBtn.addEventListener('click', function () {
        if(isExpanded) {
            content.classList.add("hidden");
            separator.classList.add('hidden');
            chevron.classList.add('-rotate-90');
        } else {
            content.classList.remove("hidden");
            separator.classList.remove('hidden');
            chevron.classList.remove('-rotate-90');
        }
        isExpanded = !isExpanded
    });
}

function listenModalToggle(modal, openBtn, closeBtn = null, closingAction = null) {
    if (closeBtn) {
        // Modal normal
        openBtn.addEventListener('click', () => {
            const menuBtn = document.getElementById("menu-button");
            menuBtn.click()
            modal.classList.remove("hidden");
        });

        closeBtn.addEventListener('click', () => {
            modal.classList.add("hidden");
            if (closingAction) closingAction();
        });
    }
    else {
        // Dropdown
        openBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            modal.classList.toggle("hidden");
        });

        modal.addEventListener('click', (e) => {
            e.stopPropagation();
        });

        document.addEventListener('click', () => {
            modal.classList.add("hidden");
        });
    }
}

// Convierte una cadena de ubicación a lat/lon usando Nominatim
async function geocodeUbicacion(pais, provincia, ciudad) {
    const parts = [ciudad, provincia, pais].filter(Boolean);
    if (parts.length === 0) return null;
    const q = parts.join(', ');
    const url = 'https://nominatim.openstreetmap.org/search?format=json&limit=1&q=' + encodeURIComponent(q);

    // Nota: evite llamados masivos a Nominatim desde el cliente en producción.
    const resp = await fetch(url, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
            // No es posible setear User-Agent desde el navegador; Nominatim recibe el UA del navegador.
        }
    });

    if (!resp.ok) return null;
    const data = await resp.json();
    if (Array.isArray(data) && data.length > 0) {
        return { lat: data[0].lat, lon: data[0].lon };
    }
    return null;
}

function listenAplicarFilter() {
    const aplicarBtn = document.getElementById('btn-aplicar-filtros');
    const form = document.getElementById('form-filtros');

    aplicarBtn.addEventListener('click', async function (e) {
        e.preventDefault();

        const params = new URLSearchParams(window.location.search);

        // Tomar directamente de los inputs
        const acontecimientoDesde = document.getElementById('fechaAcontecimientoDesde').value;
        const acontecimientoHasta = document.getElementById('fechaAcontecimientoHasta').value;
        const reporteDesde = document.getElementById('fechaReporteDesde').value;
        const reporteHasta = document.getElementById('fechaReporteHasta').value;
        const categoria = document.getElementById('categoria').value;
        const pais = document.getElementById('filtroPais')?.value?.trim();
        const provincia = document.getElementById('filtroProvincia')?.value?.trim();
        const ciudad = document.getElementById('filtroCiudad')?.value?.trim();

        // Limpiar posibles valores previos
        const inputLat = document.getElementById('filtroLatitud');
        const inputLon = document.getElementById('filtroLongitud');
        if (inputLat) inputLat.value = '';
        if (inputLon) inputLon.value = '';

        // Si el usuario puso al menos un campo de ubicación, intentar geocodificar
        if (pais || provincia || ciudad) {
            try {
                const geo = await geocodeUbicacion(pais, provincia, ciudad);
                if (geo) {
                    if (inputLat) inputLat.value = geo.lat;
                    if (inputLon) inputLon.value = geo.lon;
                    console.log('Geocodificado exitosamente:', geo);
                } else {
                    console.warn('Nominatim no devolvió coordenadas para la ubicación indicada.');
                }
            } catch (err) {
                console.error('Error geocodificando la ubicación:', err);
            }
        }

        // Validaciones
        if (acontecimientoDesde && acontecimientoHasta) {
            if (new Date(acontecimientoDesde) >= new Date(acontecimientoHasta)) {
                alert("La fecha de acontecimiento 'desde' no puede ser mayor o igual que 'hasta'.");
                return;
            }
        }

        if (reporteDesde && reporteHasta) {
            if (new Date(reporteDesde) >= new Date(reporteHasta)) {
                alert("La fecha de reporte 'desde' no puede ser mayor o igual que 'hasta'.");
                return;
            }
        }

        // Actualizar parámetros con los valores correctos
        if (categoria) params.set('categoria', categoria);
        else params.delete('categoria');

        if (acontecimientoDesde) params.set('fechaAcontecimientoDesde', acontecimientoDesde);
        else params.delete('fechaAcontecimientoDesde');

        if (acontecimientoHasta) params.set('fechaAcontecimientoHasta', acontecimientoHasta);
        else params.delete('fechaAcontecimientoHasta');

        if (reporteDesde) params.set('fechaReporteDesde', reporteDesde);
        else params.delete('fechaReporteDesde');

        if (reporteHasta) params.set('fechaReporteHasta', reporteHasta);
        else params.delete('fechaReporteHasta');

        // Enviar latitud y longitud si fueron geocodificadas
        if (inputLat && inputLat.value) params.set('latitud', inputLat.value);
        else params.delete('latitud');

        if (inputLon && inputLon.value) params.set('longitud', inputLon.value);
        else params.delete('longitud');

        window.location.href = '/mapa?' + params.toString();
    });

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        aplicarBtn.click();
    });
}

function listenLimpiarFilter(inputsContainer) {
    const limpiarBtn = document.getElementById('btn-limpiar-filtros');

    limpiarBtn.addEventListener("click", function () {
        inputsContainer.querySelectorAll('input, select, textarea').forEach(input => {
            input.value = "";
        });
    });
}

function listenAgregarFuente(addBtn, object, containerElement) {
    addBtn.addEventListener("click", function () {
        object.contadorFuentes++;

        const fuenteDiv = document.createElement('div');
        fuenteDiv.className = 'fuente-item border border-gray-300 rounded-md p-3 bg-gray-50 mb-2';
        fuenteDiv.id = `fuente-${object.contadorFuentes}`;

        fuenteDiv.innerHTML = `
        <div class="flex justify-between items-center mb-2">
            <span class="text-sm font-medium text-gray-700">Fuente ${object.contadorFuentes}</span>
            <button id="eliminar-fuente-${object.contadorFuentes}" type="button" data-id="${object.contadorFuentes}" class="btn-eliminar">
                Eliminar
            </button>
        </div>

        <div class="space-y-2">
            <div>
                <label class="block text-xs font-medium text-gray-600 mb-1">Tipo</label>
                <select id="fuente-tipo-${object.contadorFuentes}"
                        class="w-full px-2 py-1 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-gray-300"
                        data-id="${object.contadorFuentes}">
                    <option value="">Seleccione el tipo de fuente</option>
                    <option value="ESTATICA">Estática</option>
                    <option value="DINAMICA">Dinámica</option>
                    <option value="PROXY">Proxy</option>
                </select>
            </div>

            <div id="nombre-nontainer-${object.contadorFuentes}" class="hidden">
                <label class="block text-xs font-medium text-gray-600 mb-1">Nombre</label>
                <input type="text" id="fuenteNombre-${object.contadorFuentes}"
                       class="w-full px-2 py-1 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-gray-300"
                       placeholder="Ingrese el nombre">
            </div>
        </div>
        `;
        containerElement.appendChild(fuenteDiv);

        const removeBtn = document.getElementById(`eliminar-fuente-${object.contadorFuentes}`);
        const elemento = document.getElementById(`fuente-${object.contadorFuentes}`);

        removeBtn.addEventListener("click", function() {elemento.remove()});
    });
}

function listenMostrarCamposCriterio(tipoCriterio) {
    const camposDistancia = document.getElementById("campos-distancia");
    const camposFecha = document.getElementById("campos-fecha");

    tipoCriterio.addEventListener("change", function() {
        const criterio = tipoCriterio.value;

        camposDistancia.classList.add("hidden");
        camposFecha.classList.add("hidden");

        if (criterio === "DISTANCIA") {
            camposDistancia.classList.remove("hidden");
        }

        if (criterio === "FECHA") {
            camposFecha.classList.remove("hidden");
        }
    });
}

function listenMostrarCamposFuente() {
    document.addEventListener("change", e => {
        if (e.target.matches("select[id^='fuenteTipo']")) {
            const id = e.target.dataset.id;
            const tipo = document.getElementById(`fuenteTipo-${id}`).value;
            const nombreContainer = document.getElementById(`nombreContainer-${id}`);
            nombreContainer.classList.toggle("hidden", !(tipo === "ESTATICA" || tipo === "PROXY"));
        }
    });
}