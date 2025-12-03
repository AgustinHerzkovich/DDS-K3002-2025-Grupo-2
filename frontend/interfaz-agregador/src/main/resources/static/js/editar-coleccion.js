document.addEventListener('DOMContentLoaded', function () {
    const editarBtns = Array.from(document.querySelectorAll(".coleccion-card .editar-coleccion"));
    const modal = document.getElementById("modal-editar-coleccion");
    const closeBtn = document.getElementById("salir-editar-coleccion");

    if(allElementsFound([modal, closeBtn], "editar colecciones") && editarBtns.length > 0) {
        colecciones.forEach((coleccion, index) => {
            listenOpenModal(modal, editarBtns[index], () => {
                abrirModalEditarColeccion(coleccion);
            });
        });

        listenCloseModal(modal, closeBtn, () => {
            limpiarModalEditarColeccion();
        });

        // Event listener para agregar nueva fuente
        const btnAgregarFuente = document.getElementById('btn-agregar-fuente-editar');
        if (btnAgregarFuente) {
            const contadorFuentesObject = { cantidadFuentes: 0 };
            btnAgregarFuente.addEventListener('click', function() {
                contadorFuentesObject.cantidadFuentes++;
                agregarFuenteAColeccionEditar(contadorFuentesObject.cantidadFuentes);
            });
        }

        // Event listener para cambio de tipo de fuente (delegado)
        listenCamposFuenteModalEditarColeccion();

        // Event listener para cambio de algoritmo
        const selectAlgoritmo = document.getElementById('modal-coleccion-algoritmo');
        if (selectAlgoritmo) {
            selectAlgoritmo.addEventListener('change', function() {
                window.algoritmoActual = this.value;
            });
        }

        // Event listener para guardar cambios
        const btnGuardar = document.getElementById('guardar-editar-coleccion');
        if (btnGuardar) {
            btnGuardar.addEventListener('click', guardarCambiosColeccion);
        }
    }
});

function abrirModalEditarColeccion(coleccion) {
    // Guardar datos originales en variables globales window
    window.coleccionActual = coleccion;
    window.fuentesOriginales = [...(coleccion.fuentes || [])];
    window.fuentesActuales = [...window.fuentesOriginales];
    window.operacionesPendientes = [];
    window.algoritmoOriginal = coleccion.tipoAlgoritmoConsenso;
    window.algoritmoActual = window.algoritmoOriginal;
    window.fuentesNuevas = []; // Para las fuentes agregadas con el nuevo sistema

    // Rellenar información de solo lectura
    document.getElementById('modal-coleccion-titulo').textContent = coleccion.titulo || '-';

    // Seleccionar algoritmo actual
    document.getElementById('modal-coleccion-algoritmo').value = coleccion.tipoAlgoritmoConsenso || '';

    // Limpiar container de nuevas fuentes
    document.getElementById('fuentes-container-editar-coleccion').innerHTML = '';

    // Renderizar fuentes actuales
    renderizarFuentesActuales();
    renderizarOperacionesPendientes();
}

function limpiarModalEditarColeccion() {
    document.getElementById('form-editar-coleccion').reset();
    document.getElementById('fuentes-container-editar-coleccion').innerHTML = '';
    window.coleccionActual = null;
    window.fuentesOriginales = [];
    window.fuentesActuales = [];
    window.operacionesPendientes = [];
    window.algoritmoOriginal = null;
    window.algoritmoActual = null;
    window.fuentesNuevas = [];
}

function agregarFuenteAColeccionEditar(numeroFuente) {
    const fuenteDiv = document.createElement('div');
    fuenteDiv.className = 'fuente-item border border-gray-300 rounded-md py-2 px-3 bg-gray-50 mb-2';
    fuenteDiv.id = `fuente-editar-${numeroFuente}`;

    fuenteDiv.innerHTML = `
        <div class="flex justify-between items-center mb-1">
            <span class="block text-xs font-medium text-gray-600">Nueva Fuente</span>
            <button id="eliminar-fuente-editar-${numeroFuente}" type="button" data-id="${numeroFuente}" class="btn-eliminar py-1">
                Eliminar
            </button>
        </div>

        <div class="space-y-2">
            <label class="block text-xs font-medium text-gray-600 mb-1">Tipo</label>
            <select id="fuente-tipo-editar-${numeroFuente}" class="form-input" data-id="editar-${numeroFuente}">
                <option value="">Seleccione el tipo de fuente</option>
                <option value="estatica">Estática</option>
                <option value="dinamica">Dinámica</option>
                <option value="proxy">Proxy</option>
            </select>

            <div id="nombre-container-editar-${numeroFuente}" class="hidden">
                <label for="fuente-nombre-editar-${numeroFuente}" class="block text-xs font-medium text-gray-600 mb-1">
                    ID de la Fuente
                    <span class="max-char-span">
                        (Máx.255 caracteres)
                    </span>
                </label>
                <input type="text" id="fuente-nombre-editar-${numeroFuente}" maxlength="255"
                       class="form-input"
                       placeholder="Ingrese el ID de la fuente">
            </div>
        </div>
    `;

    document.getElementById('fuentes-container-editar-coleccion').appendChild(fuenteDiv);

    const removeBtn = document.getElementById(`eliminar-fuente-editar-${numeroFuente}`);
    removeBtn.addEventListener("click", () => {
        fuenteDiv.remove();
        // Remover de la lista de fuentes nuevas si existía
        window.fuentesNuevas = window.fuentesNuevas.filter(f => f.numero !== numeroFuente);
    });
}

function listenCamposFuenteModalEditarColeccion() {
    document.addEventListener("change", e => {
        if (e.target.matches("select[id^='fuente-tipo-editar']")) {
            const id = e.target.dataset.id;
            const tipo = e.target.value;
            const nombreContainer = document.getElementById(`nombre-container-${id}`);

            if (nombreContainer) {
                nombreContainer.classList.toggle("hidden", !(tipo === "estatica" || tipo === "proxy"));
            }
        }
    });
}

function renderizarFuentesActuales() {
    const container = document.getElementById('lista-fuentes-actuales-editar');

    if (!window.fuentesActuales || window.fuentesActuales.length === 0) {
        container.innerHTML = '<p class="text-gray-500 mb-0">No hay fuentes asociadas</p>';
        return;
    }

    container.innerHTML = window.fuentesActuales.map(fuente => `
        <div class="d-flex justify-content-between align-items-center p-2 border-bottom">
            <span class="text-black">${fuente.nombre || fuente.id || fuente} (${fuente.tipo || 'N/A'})</span>
            <button type="button" class="btn btn-sm btn-danger" onclick="quitarFuente('${fuente.id || fuente}')">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                </svg>
                Quitar
            </button>
        </div>
    `).join('');
}

function renderizarOperacionesPendientes() {
    const container = document.getElementById('operaciones-pendientes-container');
    const lista = document.getElementById('lista-operaciones-pendientes');

    if (!window.operacionesPendientes || window.operacionesPendientes.length === 0) {
        container.classList.add('hidden');
        return;
    }

    container.classList.remove('hidden');
    lista.innerHTML = window.operacionesPendientes.map((op, index) => `
        <div class="d-flex justify-content-between align-items-center p-2 border-bottom">
            <span class="text-black">
                <strong>${op.tipo === 'AGREGAR' ? '➕ Agregar' : '➖ Quitar'}:</strong> ${op.fuenteId} (${op.tipoFuente || 'N/A'})
            </span>
            <button type="button" class="btn btn-sm btn-secondary" onclick="cancelarOperacion(${index})">
                Cancelar
            </button>
        </div>
    `).join('');
}

function quitarFuente(fuenteId) {
    // Eliminar de la lista actual
    window.fuentesActuales = window.fuentesActuales.filter(f => (f.id || f) !== fuenteId);

    // Si estaba en las fuentes originales, registrar operación DELETE
    if (window.fuentesOriginales.some(f => (f.id || f) === fuenteId)) {
        // Cancelar operación POST si existía
        window.operacionesPendientes = window.operacionesPendientes.filter(op =>
            !(op.tipo === 'AGREGAR' && op.fuenteId === fuenteId)
        );

        window.operacionesPendientes.push({
            tipo: 'QUITAR',
            fuenteId: fuenteId
        });
    } else {
        // Era una fuente recién agregada, solo cancelar la operación POST
        window.operacionesPendientes = window.operacionesPendientes.filter(op =>
            !(op.tipo === 'AGREGAR' && op.fuenteId === fuenteId)
        );
    }

    renderizarFuentesActuales();
    renderizarOperacionesPendientes();
}

function cancelarOperacion(index) {
    const operacion = window.operacionesPendientes[index];

    if (operacion.tipo === 'AGREGAR') {
        // Quitar de la lista actual
        window.fuentesActuales = window.fuentesActuales.filter(f => (f.id || f) !== operacion.fuenteId);
    } else {
        // Volver a agregar a la lista actual
        const fuenteOriginal = window.fuentesOriginales.find(f => (f.id || f) === operacion.fuenteId);
        window.fuentesActuales.push(fuenteOriginal);
    }

    window.operacionesPendientes.splice(index, 1);
    renderizarFuentesActuales();
    renderizarOperacionesPendientes();
}

async function guardarCambiosColeccion() {
    const btn = document.getElementById('guardar-editar-coleccion');
    const spinner = btn.querySelector('.spinner-border');

    try {
        btn.disabled = true;
        if (spinner) spinner.classList.remove('hidden');

        // Recopilar las nuevas fuentes del formulario
        const fuentesNuevasContainer = document.getElementById('fuentes-container-editar-coleccion');
        const fuentesDivs = fuentesNuevasContainer.querySelectorAll('[id^="fuente-editar-"]');

        fuentesDivs.forEach(div => {
            const id = div.id.replace('fuente-editar-', '');
            const tipo = document.getElementById(`fuente-tipo-editar-${id}`)?.value;

            if (tipo) {
                let fuenteId;

                if (tipo === 'dinamica') {
                    // Para dinámica, siempre es el ID fijo
                    fuenteId = '675ad24ea9cdd83046fe9ccf';
                } else if (tipo === 'estatica' || tipo === 'proxy') {
                    // Para estática o proxy, usar el ID ingresado
                    fuenteId = document.getElementById(`fuente-nombre-editar-${id}`)?.value;

                    if (!fuenteId || fuenteId.trim() === '') {
                        throw new Error(`Debe ingresar el ID de la fuente ${tipo}`);
                    }
                }

                // Agregar a operaciones pendientes
                window.operacionesPendientes.push({
                    tipo: 'AGREGAR',
                    fuenteId: fuenteId,
                    tipoFuente: tipo
                });
            }
        });

        // 1. Actualizar algoritmo de consenso si cambió
        if (window.algoritmoActual !== window.algoritmoOriginal) {
            console.log('Actualizando algoritmo de consenso...');
            const responseAlgoritmo = await fetch(`http://localhost:8086/apiAdministrativa/colecciones/${window.coleccionActual.id}/algoritmo`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + jwtToken
                },
                body: JSON.stringify({ algoritmoConsenso: window.algoritmoActual })
            });

            if (!responseAlgoritmo.ok) {
                throw new Error('Error al actualizar el algoritmo de consenso');
            }
            console.log('✓ Algoritmo actualizado');
        }

        // 2. Ejecutar operaciones de fuentes
        for (const operacion of window.operacionesPendientes) {
            if (operacion.tipo === 'AGREGAR') {
                console.log(`Agregando fuente ${operacion.fuenteId}...`);
                const tipoFuente = document.getElementById(`fuente-tipo-editar-${id}`)?.value;
                const responseAgregar = await fetch(`http://localhost:8086/apiAdministrativa/colecciones/${window.coleccionActual.id}/fuentes`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + jwtToken
                    },
                    body: JSON.stringify({ tipo: tipoFuente, id: operacion.fuenteId })
                });

                if (!responseAgregar.ok) {
                    throw new Error(`Error al agregar la fuente ${operacion.fuenteId}`);
                }
                console.log(`✓ Fuente ${operacion.fuenteId} agregada`);
            } else if (operacion.tipo === 'QUITAR') {
                console.log(`Quitando fuente ${operacion.fuenteId}...`);
                const responseQuitar = await fetch(`http://localhost:8086/apiAdministrativa/colecciones/${window.coleccionActual.id}/fuentes/${operacion.fuenteId}`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': 'Bearer ' + jwtToken
                    }
                });

                if (!responseQuitar.ok) {
                    throw new Error(`Error al quitar la fuente ${operacion.fuenteId}`);
                }
                console.log(`✓ Fuente ${operacion.fuenteId} quitada`);
            }
        }

        alert('✓ Colección actualizada exitosamente');
        window.location.reload();
    } catch (error) {
        console.error('Error al guardar cambios:', error);
        alert('Error al guardar los cambios: ' + error.message);
    } finally {
        btn.disabled = false;
        if (spinner) spinner.classList.add('hidden');
    }
}

