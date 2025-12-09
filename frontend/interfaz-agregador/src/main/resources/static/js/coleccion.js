function validarFormularioModalCrearColeccion() {
    const titulo = document.getElementById('titulo-crear-coleccion')
    const descripcion = document.getElementById('descripcion-crear-coleccion')
    const algoritmo = document.getElementById('algoritmo-crear-coleccion');
    const inputsObligatoriosBasicos = [titulo, descripcion, algoritmo];

    const criteriosItems = Array.from(document.querySelectorAll('#criterios-container-crear-coleccion .criterio-item'));
    const tiposFuentes = Array.from(document.querySelectorAll('#fuentes-container-crear-coleccion .fuente-item select[id^="fuente-tipo-"]'));
    const nombresFuentes = Array.from(document.querySelectorAll('#fuentes-container-crear-coleccion .fuente-item select[id^="fuente-nombre-"]'));

    const criteriosParaValidar = [];
    criteriosItems.forEach(criterioItem => {
        const criterioId = criterioItem.dataset.id
        const tipoCriterio = document.getElementById(`criterio-tipo-${criterioId}`);
        criteriosParaValidar.push(tipoCriterio);

        if (tipoCriterio.value === 'DISTANCIA') {
            const lat = document.getElementById(`criterio-latitud-${criterioId}`);
            const lon = document.getElementById(`criterio-longitud-${criterioId}`);
            const dist = document.getElementById(`criterio-distancia-minima-${criterioId}`);
            criteriosParaValidar.push(lat, lon, dist);
        } else if (tipoCriterio.value === 'FECHA') {
            const fechaIni = document.getElementById(`criterio-fecha-inicial-${criterioId}`);
            const fechaFin = document.getElementById(`criterio-fecha-final-${criterioId}`);
            criteriosParaValidar.push(fechaIni, fechaFin);
        }
    });

    // Filtrar los nombres de fuentes para validar solo los que no son dinámicas o están visibles
    const nombresParaValidar = nombresFuentes.filter((select, index) => {
        const tipoFuente = tiposFuentes[index]?.value;
        return tipoFuente === "estatica" || tipoFuente === "proxy";
    });

    validarInputsObligatorios([...inputsObligatoriosBasicos, ...criteriosParaValidar, ...tiposFuentes, ...nombresParaValidar])

    return {
        titulo,
        descripcion,
        algoritmo,
        criteriosItems,
        tiposFuentes,
        nombresFuentes
    }
}

function limpiarModalColeccion(modal, sufix) {
    document.getElementById(`criterios-container-${sufix}`).innerHTML = "";
    document.getElementById(`sin-criterios-${sufix}`).classList.remove("hidden")

    document.getElementById(`fuentes-container-${sufix}`).innerHTML = "";
    document.getElementById(`sin-fuentes-${sufix}`).classList.remove("hidden")

    document.getElementById(`form-modal-${sufix}`).reset();
}

function agregarCriterioColeccion(numeroCriterio, sufix) {
    const mensajeSinCriterios = document.getElementById(`sin-criterios-${sufix}`)
    if(!mensajeSinCriterios.classList.contains("hidden")) {
        mensajeSinCriterios.classList.add("hidden")
    }

    const criterioDiv = document.createElement('div');
    criterioDiv.className = 'criterio-item border border-gray-300 rounded-md py-2 px-3 bg-gray-50 mb-2';
    criterioDiv.dataset.id = numeroCriterio;
    criterioDiv.id = `criterio-${numeroCriterio}`;

    criterioDiv.innerHTML = `
        <div class="flex justify-between items-center mb-1">
            <label for="criterio-tipo-${numeroCriterio}" class="block text-xs font-medium text-gray-600">
                Tipo de Criterio
            </label>
            <button id="eliminar-criterio-${numeroCriterio}" type="button" data-id="${numeroCriterio}" class="btn-eliminar-container">
                Eliminar
            </button>
        </div>

        <div class="space-y-2">
            <select id="criterio-tipo-${numeroCriterio}" class="form-input criterio-tipo-select" data-id="${numeroCriterio}">
                <option value="">Seleccione un criterio</option>
                <option value="DISTANCIA">Distancia</option>
                <option value="FECHA">Fecha</option>
            </select>

            <div id="campos-distancia-${numeroCriterio}" class="space-y-2 hidden">
                <div class="grid grid-cols-2 gap-2">
                    <div>
                        <label for="criterio-latitud-${numeroCriterio}" class="block text-xs font-medium text-gray-600 mb-1">
                            Latitud Base
                            <span class="form-obligatory-icon">
                                *
                            </span>
                        </label>
                        <input type="number" step="any" id="criterio-latitud-${numeroCriterio}" class="form-input" placeholder="-34.603722">
                    </div>
                    <div>
                        <label for="criterio-longitud-${numeroCriterio}" class="block text-xs font-medium text-gray-600 mb-1">
                            Longitud Base
                            <span class="form-obligatory-icon">
                                *
                            </span>
                        </label>
                        <input type="number" step="any" id="criterio-longitud-${numeroCriterio}" class="form-input" placeholder="-58.381592">
                    </div>
                </div>
                <div>
                    <label for="criterio-distancia-minima-${numeroCriterio}" class="block text-xs font-medium text-gray-600 mb-1">
                        Distancia Mínima (km)
                        <span class="form-obligatory-icon">
                            *
                        </span>
                    </label>
                    <input type="number" step="any" id="criterio-distancia-minima-${numeroCriterio}" class="form-input" placeholder="100">
                </div>
            </div>

            <div id="campos-fecha-${numeroCriterio}" class="space-y-2 hidden">
                <div>
                    <label for="criterio-fecha-inicial-${numeroCriterio}" class="block text-xs font-medium text-gray-600 mb-1">
                        Fecha Inicial
                        <span class="form-obligatory-icon">
                            *
                        </span>
                    </label>
                    <input type="datetime-local" id="criterio-fecha-inicial-${numeroCriterio}" class="form-input hover:cursor-pointer">
                </div>
                <div>
                    <label for="criterio-fecha-final-${numeroCriterio}" class="block text-xs font-medium text-gray-600 mb-1">
                        Fecha Final
                        <span class="form-obligatory-icon">
                            *
                        </span>
                    </label>
                    <input type="datetime-local" id="criterio-fecha-final-${numeroCriterio}" class="form-input hover:cursor-pointer">
                </div>
            </div>
        </div>
    `;

    document.getElementById(`criterios-container-${sufix}`).appendChild(criterioDiv);
}

function crearContainerFuenteColeccion(numeroFuente) {
    const fuenteDiv = document.createElement('div');
    fuenteDiv.className = 'fuente-item border border-gray-300 rounded-md py-2 px-3 bg-gray-50 mb-2';
    fuenteDiv.id = `fuente-${numeroFuente}`;

    fuenteDiv.innerHTML = `
        <div class="flex justify-between items-center mb-1">
            <label for="fuente-tipo-${numeroFuente}" class="block text-xs font-medium text-gray-600">Tipo</label>
            <button id="eliminar-fuente-${numeroFuente}" type="button" data-id="${numeroFuente}" class="btn-eliminar-container">
                Eliminar
            </button>
        </div>
        <div class="space-y-2">
            <select id="fuente-tipo-${numeroFuente}" class="form-input" data-id="${numeroFuente}">
                <option value="">Seleccione el tipo de fuente</option>
                <option value="estatica">Estática</option>
                <option value="dinamica">Dinámica</option>
                <option value="proxy">Proxy</option>
            </select>

            <div id="nombre-container-${numeroFuente}" class="hidden">
                <label for="fuente-nombre-${numeroFuente}" class="block text-xs font-medium text-gray-600 mb-1">
                    ID de la Fuente
                </label>
                <select id="fuente-nombre-${numeroFuente}" class="form-input">
                    <option value="">Cargando fuentes...</option>
                </select>
            </div>
        </div>
        `;

    return fuenteDiv;
}

async function cargarFuentesPorTipo(numero, tipo) {
    const selectNombre = document.getElementById(`fuente-nombre-${numero}`);

    if (!selectNombre) return;

    try {
        selectNombre.innerHTML = '<option value="">Cargando fuentes...</option>';
        selectNombre.disabled = true;

        const response = await fetch(`http://api-administrativa:8086/apiAdministrativa/fuentes?tipo=${tipo}&limit=100`, {
            headers: { 'Authorization': 'Bearer ' + jwtToken }
        });

        if (!response.ok) {
            throw new Error('Error al cargar las fuentes');
        }

        const data = await response.json();
        const fuentes = data.content || [];

        if (tipo === "dinamica") {
            selectNombre.innerHTML = ''
            const option = document.createElement('option');
            option.value = fuentes[0].id;
            option.textContent = `${fuentes[0].alias !== 'Fuente sin titulo' ? fuentes[0].alias : ''}`;
            selectNombre.appendChild(option);
        } else {
            selectNombre.innerHTML = '<option value="">Seleccione una fuente</option>';
            fuentes.forEach(fuente => {
                const option = document.createElement('option');
                option.value = fuente.id;
                option.textContent = `${fuente.alias !== 'Fuente sin titulo' ? fuente.alias : ''}`;
                selectNombre.appendChild(option);
            });
            selectNombre.disabled = false;
        }
    } catch (error) {
        console.error('Error al cargar fuentes:', error);
        selectNombre.innerHTML = '<option value="">Error al cargar fuentes</option>';
        selectNombre.disabled = true;
    }
}

function autocompletarFuentesColeccion(coleccion, sufix) {
    coleccion.fuentes.forEach((fuente, index) => {
        document.getElementById(`sin-fuentes-${sufix}`).classList.add("hidden")
        const containerFuenteCreada = crearContainerFuenteColeccion(index)
        document.getElementById(`fuentes-container-${sufix}`).appendChild(containerFuenteCreada)


        const fuenteTipo = document.getElementById(`fuente-tipo-${index}`)
        fuenteTipo.value = fuente.tipo
        fuenteTipo.disabled = true

        if (fuente.tipo !== "dinamica") {
            const fuenteNombreSelect = document.getElementById(`fuente-nombre-${index}`)

            const nuevaOpcion = document.createElement("option");
            nuevaOpcion.value = fuente.id;
            nuevaOpcion.innerText = fuente.id;
            nuevaOpcion.selected = true;

            fuenteNombreSelect.appendChild(nuevaOpcion);

            fuenteNombreSelect.disabled = true

            document.getElementById(`nombre-container-${index}`).classList.remove("hidden")
        }

        document.getElementById(`eliminar-fuente-${index}`).classList.add("hidden")
    })
}

function autocompletarCriteriosColeccion(coleccion, sufix) {
    coleccion.criteriosDePertenencia.forEach((criterioDePertenencia, index) => {
        agregarCriterioColeccion(index, sufix)

        const criterioTipo = document.getElementById(`criterio-tipo-${index}`);
        criterioTipo.value = criterioDePertenencia.tipo
        criterioTipo.disabled = true;

        if(criterioDePertenencia.tipo === 'DISTANCIA') {
            const latitud = document.getElementById(`criterio-latitud-${index}`)
            latitud.value = criterioDePertenencia.ubicacionBase.latitud
            latitud.disabled = true;

            const longitud = document.getElementById(`criterio-longitud-${index}`)
            longitud.value = criterioDePertenencia.ubicacionBase.longitud
            longitud.disabled = true;

            const distanciaMinima = document.getElementById(`criterio-distancia-minima-${index}`)
            distanciaMinima.value = criterioDePertenencia.distanciaMinima;
            distanciaMinima.disabled = true

            document.getElementById(`campos-distancia-${index}`).classList.remove("hidden");

        } else  if(criterioDePertenencia.tipo === 'FECHA') {
            const fechaInicial = document.getElementById(`criterio-fecha-inicial-${index}`)
            fechaInicial.value = criterioDePertenencia.fechaInicial;
            fechaInicial.disabled = true;

            const fechaFinal = document.getElementById(`criterio-fecha-final-${index}`)
            fechaFinal.value = criterioDePertenencia.fechaFinal;
            fechaFinal.disabled = true;

            document.getElementById(`campos-fecha-${index}`).classList.remove("hidden");
        }

        document.getElementById(`eliminar-criterio-${index}`).remove()
        document.querySelectorAll(`#modal-${sufix} .form-obligatory-icon`).forEach(icon => icon.remove())
    })
}

function cargarFuenteColeccion(fuente) {
    const fuentesActualesContainer = document.getElementById("fuentes-actuales-editar-coleccion")
    const fuenteLi = document.createElement('li');
    fuenteLi.className = 'flex justify-between items-center';
    fuenteLi.id = `fuente-actual-${fuente.id}`;
    fuenteLi.innerHTML = `
        <p>
            ${fuente.alias} (${fuente.tipo})
        </p>
        <button id="quitar-fuente-actual-${fuente.id}" type="button" data-id="${fuente.id}" class="btn-eliminar-container">
            X Quitar
        </button>
    `;
    fuentesActualesContainer.appendChild(fuenteLi);

    window.coleccionActual.fuentes.push(fuente)

    document.getElementById(`quitar-fuente-actual-${fuente.id}`).addEventListener('click', (e) => {
        const idFuenteQuitada = e.target.dataset.id;
        window.coleccionActual.fuentes = window.coleccionActual.fuentes.filter(fuente => fuente.id !== idFuenteQuitada);
        fuenteLi.remove()
    })
}

function limpiarModalEditarColeccion(agregarFuenteBtn) {
    document.getElementById('fuentes-actuales-editar-coleccion').innerHTML = '';
    document.getElementById("fuente-nueva-container-editar-coleccion").innerHTML = '';
    document.getElementById('form-editar-coleccion').reset();
}