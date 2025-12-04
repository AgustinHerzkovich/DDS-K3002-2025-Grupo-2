function validarFormularioModalColeccion() {
    const titulo = document.getElementById('titulo-coleccion')
    const descripcion = document.getElementById('descripcion-coleccion')
    const tipoCriterio = document.getElementById('criterio-tipo-coleccion');
    const lat = document.getElementById('criterio-latitud');
    const lon = document.getElementById('criterio-longitud');
    const dist = document.getElementById('criterio-distancia-minima');
    const fechaIni = document.getElementById('criterio-fecha-inicial');
    const fechaFin = document.getElementById('criterio-fecha-final');
    const algoritmo = document.getElementById('algoritmo-coleccion');
    const tiposFuentes = Array.from(document.querySelectorAll('#fuentes-container-coleccion .fuente-item select[id^="fuente-tipo-"]'));
    const nombresFuentes = Array.from(document.querySelectorAll('#fuentes-container-coleccion .fuente-item select[id^="fuente-nombre-"]'));
    const inputsObligatoriosEstaticos = {
        titulo,
        descripcion,
        tipoCriterio,
        lat,
        lon,
        dist,
        fechaIni,
        fechaFin,
        algoritmo
    }

    // Filtrar los nombres de fuentes para validar solo los que no son dinámicas o están visibles
    const nombresParaValidar = nombresFuentes.filter((select, index) => {
        const tipoFuente = tiposFuentes[index]?.value;
        return tipoFuente === "estatica" || tipoFuente === "proxy";
    });

    validarInputsObligatorios([...Object.values(inputsObligatoriosEstaticos), ...tiposFuentes, ...nombresParaValidar])

    return {
        ...inputsObligatoriosEstaticos,
        tiposFuentes,
        nombresFuentes
    }
}

function limpiarModalColeccion(modal) {
    modal.querySelectorAll('input, select, textarea').forEach(campo => {
        campo.value = "";
        campo.classList.remove("form-not-completed");
        campo.classList.add("form-input");
    });

    document.getElementById('criterio-tipo-coleccion').dispatchEvent(new Event("change"));
    document.getElementById('fuentes-container-coleccion').innerHTML = "";
}

function agregarFuenteColeccion(numeroFuente) {
    const mensajeSinFuentes = document.getElementById("sin-fuentes-coleccion")
    if(!mensajeSinFuentes.classList.contains("hidden")) {
        mensajeSinFuentes.classList.add("hidden")
    }

    const fuenteDiv = document.createElement('div');
    fuenteDiv.className = 'fuente-item border border-gray-300 rounded-md py-2 px-3 bg-gray-50 mb-2';
    fuenteDiv.id = `fuente-${numeroFuente}`;

    fuenteDiv.innerHTML = `
        <div class="flex justify-between items-center mb-1">
            <span class="block text-xs font-medium text-gray-600">Tipo</span>
            <button id="eliminar-fuente-${numeroFuente}" type="button" data-id="${numeroFuente}" class="btn-eliminar py-1">
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
    document.getElementById('fuentes-container-coleccion').appendChild(fuenteDiv);

    const removeBtn = document.getElementById(`eliminar-fuente-${numeroFuente}`);
    const elemento = document.getElementById(`fuente-${numeroFuente}`);

    removeBtn.addEventListener("click", () => elemento.remove());
}

/*function agregarCriterioAColeccion(numeroCriterio) {
    const criterioDiv = document.createElement('div');
    criterioDiv.id = `criterio-${numeroCriterio}`;

    criterioDiv.innerHTML = `
        <div class="flex justify-between items-center mb-1">
            <span class="block text-xs font-medium text-gray-600">Tipo</span>
            <button id="eliminar-fuente-${numeroFuente}" type="button" data-id="${numeroFuente}" class="btn-eliminar">
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
                <label th:for="fuente-nombre-${numeroFuente}" class="block text-xs font-medium text-gray-600 mb-1">
                    Nombre
                    <span class="max-char-span">
						(Máx.255 caracteres)
					</span>
                </label>
                <input type="text" id="fuente-nombre-${numeroFuente}" maxlength="255"
                       class="form-input"
                       placeholder="Ingrese el nombre">
            </div>
        </div>
        `;
    document.getElementById('fuentes-container-coleccion').appendChild(fuenteDiv);

    const removeBtn = document.getElementById(`eliminar-fuente-${numeroFuente}`);
    const elemento = document.getElementById(`fuente-${numeroFuente}`);

    removeBtn.addEventListener("click", () => elemento.remove());
}*/