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

function listenCloseModal(modal, closeBtn, closeAction = null) {
    closeBtn.addEventListener('click', () => {
        document.body.classList.remove("overflow-hidden");
        modal.classList.add("hidden");

        if (closeAction) {
            closeAction();
        }
    });
}

function listenOpenModal(modal, openBtn, openAction = null) {
    openBtn.addEventListener('click', async function() {
        if(openAction) {
            await openAction()
        }

        document.body.classList.add("overflow-hidden");
        modal.classList.remove("hidden");
    });
}

function listenLimpiarFiltrosMapa(inputsContainer) {
    const limpiarBtn = document.getElementById('btn-limpiar-filtros');

    limpiarBtn.addEventListener("click", function () {
        // Limpiar los inputs del modal
        inputsContainer.querySelectorAll('input, select, textarea').forEach(input => {
            if (input.type === 'range') {
                input.value = "5"; // Reset radio to default
                const radioValue = document.getElementById('radio-value');
                if (radioValue) radioValue.textContent = "5";
            } else {
                input.value = "";
            }
        });

        // Quitar los query params de filtros específicos
        const params = new URLSearchParams(window.location.search);
        const filtrosToRemove = ['categoria', 'fechaAcontecimientoDesde', 'fechaAcontecimientoHasta',
                                  'fechaReporteDesde', 'fechaReporteHasta', 'latitud', 'longitud', 'radio'];

        filtrosToRemove.forEach(param => params.delete(param));

        // Construir la nueva URL manteniendo el pathname actual y los demás query params
        const newUrl = window.location.pathname + (params.toString() ? '?' + params.toString() : '');
        window.location.href = newUrl;
    });
}

function listenRadioSliderMapa() {
    const radioSlider = document.getElementById('filtroRadio');
    const radioValue = document.getElementById('radio-value');

    if (radioSlider && radioValue) {
        // Función para actualizar el progreso visual del slider
        const updateSliderProgress = () => {
            const min = radioSlider.min || 0;
            const max = radioSlider.max || 100;
            const value = radioSlider.value;
            const percentage = ((value - min) / (max - min)) * 100;
            radioSlider.style.setProperty('--range-progress', `${percentage}%`);
        };

        // Actualizar el valor y el progreso visual
        radioSlider.addEventListener('input', function () {
            radioValue.textContent = this.value;
            updateSliderProgress();
        });

        // Inicializar el progreso visual
        updateSliderProgress();
    }
}

function listenCriteriosColeccion(agregarBtn, sufix) {
    let cantidadCriterios = 0

    agregarBtn.addEventListener("click", function () {
        cantidadCriterios++
        agregarCriterioColeccion(cantidadCriterios, sufix)
        listenCriterioColeccion(cantidadCriterios, sufix)
    });
}

function listenFuentesColeccion(agregarBtn, sufix) {
    let cantidadFuentes = 0
    const fuentesContainer = document.getElementById(`fuentes-container-${sufix}`)

    agregarBtn.addEventListener("click", function () {
        if(fuentesContainer.childElementCount === 0) {
            document.getElementById(`sin-fuentes-${sufix}`).classList.add("hidden")
        }
        cantidadFuentes++

        const containerFuenteCreada = crearContainerFuenteColeccion(cantidadFuentes)
        fuentesContainer.appendChild(containerFuenteCreada)

        listenEliminarFuenteColeccion(cantidadFuentes, sufix)
        listenCambiosTipoFuenteColeccion(cantidadFuentes)
    });
}

function listenFuentesActualesColeccion() {
    document.getElementById("agregar-fuente-editar-coleccion").addEventListener("click", function () {
        const select = document.getElementById("fuente-nombre-0");
        const alias = select.options[select.selectedIndex].textContent;

        const fuenteIngresada = {
            id: document.getElementById("fuente-nombre-0").value,
            alias,
            tipo: document.getElementById("fuente-tipo-0").value
        }

        cargarFuenteColeccion(fuenteIngresada)

        document.getElementById("fuente-tipo-0").selectedIndex = 0;
        document.getElementById("fuente-nombre-0").innerHTML = "";
        document.getElementById("nombre-container-0").classList.add("hidden");
    });
}

function listenCambiosTipoFuenteColeccion(numeroFuente) {
    const nombreContainer = document.getElementById(`nombre-container-${numeroFuente}`);

    document.getElementById(`fuente-tipo-${numeroFuente}`).addEventListener('change', async function(e) {
        const tipoFuente = e.target.value

        if (tipoFuente === "estatica" || tipoFuente === "proxy" || tipoFuente === "dinamica") {
            nombreContainer.classList.remove("hidden");
            await cargarFuentesPorTipo(numeroFuente, tipoFuente);
        } else {
            nombreContainer.classList.add("hidden");
        }
    })
}

function listenEliminarFuenteColeccion(numeroFuente, sufix) {
    document.getElementById(`eliminar-fuente-${numeroFuente}`).addEventListener("click", () => {
        document.getElementById(`fuente-${numeroFuente}`).remove();

        if (document.querySelectorAll(`#fuentes-container-${sufix} .fuente-item`).length === 0) {
            document.getElementById(`sin-fuentes-${sufix}`).classList.remove("hidden");
        }
    });
}

function listenCriterioColeccion(numeroCriterio, sufix) {
    document.getElementById(`eliminar-criterio-${numeroCriterio}`).addEventListener("click", () => {
        document.getElementById(`criterio-${numeroCriterio}`).remove();

        if (document.querySelectorAll(`#criterios-container-${sufix} .criterio-item`).length === 0) {
            document.getElementById(`sin-criterios-${sufix}`).classList.remove("hidden");
        }
    });

    document.getElementById(`criterio-tipo-${numeroCriterio}`).addEventListener("change", () => {
        if(document.getElementById(`criterio-tipo-${numeroCriterio}`).value === "DISTANCIA") {
            document.getElementById(`campos-distancia-${numeroCriterio}`).classList.remove("hidden");
            document.getElementById(`campos-fecha-${numeroCriterio}`).classList.add("hidden");
        } else if(document.getElementById(`criterio-tipo-${numeroCriterio}`).value === "FECHA") {
            document.getElementById(`campos-fecha-${numeroCriterio}`).classList.remove("hidden");
            document.getElementById(`campos-distancia-${numeroCriterio}`).classList.add("hidden");
        } else {
            document.getElementById(`campos-distancia-${numeroCriterio}`).classList.add("hidden");
            document.getElementById(`campos-fecha-${numeroCriterio}`).classList.add("hidden");
        }
    })
}

function listenScrollableArrowHome(scrollArrowBtn) {
    scrollArrowBtn.addEventListener("click", function() {
        const target = document.querySelector('.home-content');
        const targetPosition = target.getBoundingClientRect().top + window.scrollY + 10;

        window.scrollTo({
            top: targetPosition,
            behavior: 'smooth'
        });
    });
}

function listenAgregarMultimediaModalHecho(agregarBtn) {
    let cantidadFuentes = 0

    agregarBtn.addEventListener("click", function() {
        cantidadFuentes++
        agregarMultimediaModalHecho(cantidadFuentes)
    });
}

function listenUbicacionInputsCrearHecho(usarCoordenadasCheck) {
    usarCoordenadasCheck.addEventListener("click", function() {
        const direccionContainer = document.getElementById("direccion-container");
        const coordenadasContainer = document.getElementById("modal-hecho-coordenadas-container");
        const latitud = document.getElementById("modal-hecho-latitud");
        const longitud = document.getElementById("modal-hecho-longitud");
        const pais = document.getElementById("modal-hecho-pais");
        const provincia = document.getElementById("modal-hecho-provincia");
        const ciudad = document.getElementById("modal-hecho-ciudad");
        const calle = document.getElementById("modal-hecho-calle");
        const altura = document.getElementById("modal-hecho-altura");

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

function listenCamposUbicacionCrearHecho() {
    const camposDireccion = document.getElementById("modal-hecho-direccion-container")
    const camposCoordenadas = document.getElementById("modal-hecho-coordenadas-container")
    const usarCoordenadasBtn = document.getElementById("modal-hecho-usar-coordenadas")

    usarCoordenadasBtn.addEventListener("click", () => {
        if(usarCoordenadasBtn.checked) {
            camposDireccion.classList.add("hidden")
            camposCoordenadas.classList.remove("hidden")
        } else {
            camposCoordenadas.classList.add("hidden")
            camposDireccion.classList.remove("hidden")
        }
    })
}