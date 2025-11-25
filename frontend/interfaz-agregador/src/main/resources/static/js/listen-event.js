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
        const latitud = document.getElementById('filtroLatitud')?.value;
        const longitud = document.getElementById('filtroLongitud')?.value;
        const radio = document.getElementById('filtroRadio')?.value;

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

        // Enviar latitud, longitud y radio solo si hay coordenadas
        if (latitud && longitud) {
            params.set('latitud', latitud);
            params.set('longitud', longitud);
            // Convertir radio de km a grados (1 grado ≈ 111 km)
            const radioKm = radio || 5;
            const radioGrados = radioKm / 111.0;
            params.set('radio', radioGrados.toString());
        } else {
            params.delete('latitud');
            params.delete('longitud');
            params.delete('radio');
        }

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
            if (input.type === 'range') {
                input.value = "5"; // Reset radio to default
                const radioValue = document.getElementById('radio-value');
                if (radioValue) radioValue.textContent = "5";
            } else {
                input.value = "";
            }
        });
    });
}

function listenRadioSlider() {
    const radioSlider = document.getElementById('filtroRadio');
    const radioValue = document.getElementById('radio-value');

    if (radioSlider && radioValue) {
        radioSlider.addEventListener('input', function () {
            radioValue.textContent = this.value;
        });
    }
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