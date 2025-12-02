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
    openBtn.addEventListener('click', () => {
        if(openAction) {
            openAction()
        }

        document.body.classList.add("overflow-hidden");
        modal.classList.remove("hidden");
    });
}

function listenLimpiarFiltrosMapa(inputsContainer) {
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

function listenRadioSliderMapa() {
    const radioSlider = document.getElementById('filtroRadio');
    const radioValue = document.getElementById('radio-value');

    if (radioSlider && radioValue) {
        radioSlider.addEventListener('input', function () {
            radioValue.textContent = this.value;
        });
    }
}

function listenCambiarAlgoritmoEditarColeccion() {
    const cambiarAlgoritmoBtn = document.getElementById('btnCambiarAlgoritmo');

    if(allElementsFound([cambiarAlgoritmoBtn], "confirmar el cambio de algoritmo")) {
        cambiarAlgoritmoBtn.addEventListener('click', async function() {
            try {
                mostrarCargando("cambiar-algoritmo-coleccion");
                const coleccionId = cambiarAlgoritmoBtn.dataset.coleccionId;

                if (!coleccionId) {
                    console.error('ID de colección inválido');
                    return;
                }

                const select = document.getElementById('coleccion-algoritmo-select');
                const nuevoAlgoritmo = select.value;

                const response = await fetch(`http://localhost:8086/apiAdministrativa/colecciones/${coleccionId}/algoritmo`, {
                    method: 'PATCH',
                    headers: { 'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + jwtToken },
                    body: JSON.stringify({ algoritmoConsenso: nuevoAlgoritmo })
                })

                if (!response.ok) {
                    const errorText = await response.text()
                    throw new Error(errorText ? `Error al cambiar el algoritmo:\n${errorText}` : "Error al cambiar el algoritmo")
                }

                alert('Algoritmo actualizado exitosamente');
                window.location.reload();
            } catch (error){
                console.error('Error en cambiarAlgoritmoEditarColeccion:', error);
                alert(error.message);
            } finally {
                ocultarCargando("cambiar-algoritmo-coleccion");
            }
        });
    }
}

function listenEliminarFuenteEditarColeccion() {
    document.querySelectorAll('.btn-eliminar-fuente').forEach(btn => {
        btn.addEventListener('click', async function() {
            try {
                mostrarCargando(btn.id);

                const coleccionId = btn.dataset.coleccionId;
                const fuenteId = btn.dataset.fuenteId;

                if(!allElementsFound([coleccionId, fuenteId], "eliminar fuentes")) {
                    return;
                }

                if (!confirm(`¿Estás seguro de eliminar la fuente "${fuenteId}"?`)) return;

                const response = await fetch(`http://localhost:8086/apiAdministrativa/colecciones/${coleccionId}/fuentes/${fuenteId}`, {
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + jwtToken }
                })

                if (!response.ok) {
                    const errorText = await response.text();
                    throw new Error(errorText ? `Error al eliminar la fuente:\n${errorText}` : "Error al eliminar la fuente")
                }

                alert('Fuente eliminada exitosamente');
                window.location.reload();
            } catch (error) {
                console.error('Error en eliminarFuente:', error);
                alert(error.message);
            } finally {
                ocultarCargando(btn.id);
            }
        });
    });
}

function listenAgregrFuenteEditarColeccion() {
    const agregarBtn = document.getElementById('btnAgregarFuente');

    agregarBtn.addEventListener('click', () => {
        const coleccionId = agregarBtn.dataset.coleccionId;

        if(!allElementsFound([coleccionId], "agregar fuentes")) {
            return;
        }

        const tipo = document.getElementById('newFuenteTipo')?.value;
        const id = document.getElementById('newFuenteId')?.value?.trim();
        const ip = document.getElementById('newFuenteIp')?.value?.trim();
        const puerto = document.getElementById('newFuentePuerto')?.value?.trim();

        if (!tipo || !id || !ip || !puerto) {
            alert('Todos los campos son obligatorios');
            return;
        }

        const puertoNum = parseInt(puerto, 10);
        if (isNaN(puertoNum) || puertoNum <= 0) {
            alert('El puerto debe ser un número válido mayor a 0');
            return;
        }

        const nuevaFuente = { tipo, id, ip, puerto: puertoNum };

        fetch(`http://localhost:8086/apiAdministrativa/colecciones/${coleccionId}/fuentes`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + jwtToken },
            body: JSON.stringify(nuevaFuente)
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(errorText => {throw new Error(errorText || 'Error al agregar la fuente')})
                }

                alert('Fuente agregada exitosamente');

                // Limpiar campos
                document.getElementById('newFuenteId').value = '';
                document.getElementById('newFuenteIp').value = '';
                document.getElementById('newFuentePuerto').value = '';

                window.location.reload();
            })
            .catch(error => {
                console.error('Error en agregarFuente:', error);
                alert(`Error al agregar fuente: ${error.message}`);
            })
    });
}

function listenAgregarFuenteModalColeccion(agregarBtn) {
    const contadorFuentesObject = {cantidadFuentes: 0}

    agregarBtn.addEventListener("click", function () {
        contadorFuentesObject.cantidadFuentes++
        agregarFuenteAColeccion(contadorFuentesObject.cantidadFuentes)
    });
}

/*function listenAgregarCriterioModalColeccion(agregarBtn) {
    const contadorCriteriosObject = {cantidadCriterios: 0}

    agregarBtn.addEventListener("click", function () {
        agregarCriterioAColeccion(contadorCriteriosObject)
    });
}*/

function listenCamposCriterioModalColeccion() {
    const camposDistancia = document.getElementById("campos-distancia-coleccion");
    const camposFecha = document.getElementById("campos-fecha-coleccion");

    document.getElementById('criterio-tipo-coleccion').addEventListener("change", function(e) {
        const criterio = e.target.value;

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

function listenCamposFuenteModalColeccion() {
    document.addEventListener("change", e => {
        if (e.target.matches("select[id^='fuente-tipo']")) {
            const id = e.target.dataset.id;
            const tipo = document.getElementById(`fuente-tipo-${id}`).value;
            const nombreContainer = document.getElementById(`nombre-container-${id}`);

            nombreContainer.classList.toggle("hidden", !(tipo === "estatica" || tipo === "proxy"));
        }
    });
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

function listenAgregarMultimediaModalHecho(object) {
    const agregarMultimediaBtn = document.getElementById("modal-hecho-agregar-multimedia");
    const container = document.getElementById('modal-hecho-multimedia-container');

    agregarMultimediaBtn.addEventListener("click", function() {
        object.multimediaCount++;

        const multimediaDiv = document.createElement('div');
        multimediaDiv.className = 'flex items-center gap-2 mb-[1rem]';
        multimediaDiv.id = `multimedia-${object.multimediaCount}`;

        multimediaDiv.innerHTML = `
                <input type="text" class="form-input multimedia-input" maxlength="500"
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