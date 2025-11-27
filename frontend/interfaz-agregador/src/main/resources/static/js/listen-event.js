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
        openBtn.addEventListener('click', () => {
            const menuBtn = document.getElementById("menu-button");
            document.body.classList.add("overflow-hidden");
            menuBtn.click()
            modal.classList.remove("hidden");
        });

        closeBtn.addEventListener('click', () => {
            document.body.classList.remove("overflow-hidden");
            modal.classList.add("hidden");
            if (closingAction) {
                closingAction();
            }
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

function listenCleanFilters(inputsContainer) {
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

function listenEliminarFuenteDeColeccion() {
    document.querySelectorAll('.btn-eliminar-fuente').forEach(btn => {
        btn.addEventListener('click', () => {
            const coleccionId = btn.dataset.coleccionId;
            const fuenteId = btn.dataset.fuenteId;

            if (coleccionId && fuenteId) {
                eliminarFuente(coleccionId, fuenteId);
            }
        });
    });
}

function listenAgregarFuenteCrearColeccion(addBtn, object, containerElement) {
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

            <div id="nombre-container-${object.contadorFuentes}" class="hidden">
                <label class="block text-xs font-medium text-gray-600 mb-1">
                    Nombre
                    <span class="max-char-span">
						(Máx.255 caracteres)
					</span>
                </label>
                <input type="text" id="fuenteNombre-${object.contadorFuentes}" maxlength="255"
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
        if (e.target.matches("select[id^='fuente-tipo']")) {
            const id = e.target.dataset.id;
            const tipo = document.getElementById(`fuente-tipo-${id}`).value;
            const nombreContainer = document.getElementById(`nombre-container-${id}`);
            nombreContainer.classList.toggle("hidden", !(tipo === "ESTATICA" || tipo === "PROXY"));
        }
    });
}

function listenHomeScrollableArrow(scrollArrowBtn) {
    scrollArrowBtn.addEventListener("click", function() {
        const target = document.querySelector('.home-content');
        const targetPosition = target.getBoundingClientRect().top + window.scrollY + 10;

        window.scrollTo({
            top: targetPosition,
            behavior: 'smooth'
        });
    });
}

function listenAgregarMultimedia(object) {
    const agregarMultimediaBtn = document.getElementById("agregar-multimedia");
    const container = document.getElementById('multimedia-container');

    agregarMultimediaBtn.addEventListener("click", function() {
        object.multimediaCount++;

        const multimediaDiv = document.createElement('div');
        multimediaDiv.className = 'multimedia-item flex items-center gap-2 mb-[1rem]';
        multimediaDiv.id = `multimedia-${object.multimediaCount}`;

        multimediaDiv.innerHTML = `
                <input type="text" class="form-control" maxlength="500"
                       id="url-${object.multimediaCount}"
                       placeholder="https://ejemplo.com/imagen.jpg" required>
                <button id="eliminar-multimedia-${object.multimediaCount}" type="button" class="text-red-500 hover:text-red-700 p-1 rounded-full bg-red-100 hover:bg-red-200">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                    </svg>
                </button>
        `;

        container.appendChild(multimediaDiv);

        const eliminarMultimediaBtn = document.getElementById(`eliminar-multimedia-${object.multimediaCount}`)
        const element = document.getElementById(`multimedia-${object.multimediaCount}`);
        eliminarMultimediaBtn.addEventListener("click", () => element.remove());
    });
}

function listenMostrarUbicacionInputs(usarCoordenadasCheck) {
    usarCoordenadasCheck.addEventListener("click", function() {
        const direccionContainer = document.getElementById("direccion-container");
        const coordenadasContainer = document.getElementById("coordenadas-container");
        const latitud = document.getElementById("crear-hecho-latitud");
        const longitud = document.getElementById("crear-hecho-longitud");
        const pais = document.getElementById("crear-hecho-pais");
        const provincia = document.getElementById("crear-hecho-provincia");
        const ciudad = document.getElementById("crear-hecho-ciudad");
        const calle = document.getElementById("crear-hecho-calle");
        const altura = document.getElementById("crear-hecho-altura");

        if (usarCoordenadasCheck.checked) {
            // Mostrar coordenadas
            direccionContainer.classList.add("hidden");
            coordenadasContainer.classList.remove("hidden");

            // Requeridos
            latitud.required = true;
            longitud.required = true;

            pais.required = false;
            provincia.required = false;
            ciudad.required = false;
            calle.required = false;
            altura.required = false;

        } else {
            // Mostrar dirección
            direccionContainer.classList.remove("hidden");
            coordenadasContainer.classList.add("hidden");

            // Requeridos
            pais.required = true;
            provincia.required = true;
            ciudad.required = true;
            calle.required = true;
            altura.required = true;

            latitud.required = false;
            longitud.required = false;
        }
    });
}

function listenEditarHecho(open) {
    const editarHechBtn = document.getElementById("btn-editar-hecho")
    const misHechosBtn = document.getElementById("salir-mis-hechos")

    misHechosBtn.click()

    // Abrir el modal de crear/editar hecho
    openBtn.click()

    // Rellenar el formulario con los datos del hecho
    document.getElementById('titulo').value = hecho.titulo;
    document.getElementById('descripcion').value = hecho.descripcion;
    document.getElementById('categoria').value = hecho.categoria.nombre;
    document.getElementById('contenido-texto').value = hecho.contenidoTexto;
    document.getElementById('fecha').value = hecho.fechaAcontecimiento.slice(0, 16);
    document.getElementById('anonimato').checked = hecho.anonimato;

    // Coordenadas
    document.getElementById('btn-usar-coordenadas').checked = true;
    toggleUbicacionInputs();
    document.getElementById('latitud').value = hecho.ubicacion.latitud;
    document.getElementById('longitud').value = hecho.ubicacion.longitud;

    // Multimedias
    if (hecho.contenidoMultimedia && hecho.contenidoMultimedia.length > 0) {
        hecho.contenidoMultimedia.forEach(url => {
            multimediaCount++;
            const container = document.getElementById('multimedia-container');
            const multimediaDiv = document.createElement('div');
            multimediaDiv.className = 'multimedia-item flex items-center gap-2';
            multimediaDiv.id = `multimedia-${multimediaCount}`;
            multimediaDiv.innerHTML = `
                <input type="text" style="color:black" maxlength="500"
                       class="form-multimedia-input flex-grow border-gray-300 rounded-md shadow-sm text-sm"
                       id="url-${multimediaCount}"
                       value="${url}" required>
                <button type="button" class="text-red-500 hover:text-red-700 p-1 rounded-full bg-red-100 hover:bg-red-200" 
                        onclick="eliminarMultimedia(${multimediaCount})">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" 
                         stroke="currentColor" class="w-4 h-4">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                    </svg>
                </button>
            `;
            container.appendChild(multimediaDiv);
        });
    }

    // Cambiar el botón de "Publicar" a "Guardar Edición"
    const botonPublicar = document.querySelector('button[onclick="publicarHecho(false)"]');
    botonPublicar.textContent = 'Guardar Edición';
    botonPublicar.onclick = () => guardarEdicion(hecho.id);
}

function listenIrACrearHecho(closeMisHechosBtn) {
    const openModalCrearHechoBtn = document.getElementById("open-modal-crear-hecho")
    const crearHechoMenuBtn = document.getElementById("menu-crear-hecho")

    openModalCrearHechoBtn.addEventListener("click", function() {
        closeMisHechosBtn.click();
        crearHechoMenuBtn.click();
    })
}

function allElementsFound(elementsList, desiredAction) {
    if(elementsList.some(e => e === null)) {
        console.log("No se pudo encontrar algún elemento para " + desiredAction)
    }

    return !elementsList.some(e => e === null);
}