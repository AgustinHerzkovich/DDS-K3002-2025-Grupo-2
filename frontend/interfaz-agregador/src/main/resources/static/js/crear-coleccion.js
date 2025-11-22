let contadorFuentes = 0;

function mostrarCamposCriterio() {
    const tipo = document.getElementById('criterioTipo').value;
    document.getElementById('camposDistancia').style.display = tipo === 'DISTANCIA' ? 'block' : 'none';
    document.getElementById('camposFecha').style.display = tipo === 'FECHA' ? 'block' : 'none';
}

function agregarFuente() {
    contadorFuentes++;

    const container = document.getElementById('fuentesContainer');

    const fuenteDiv = document.createElement('div');
    fuenteDiv.className = 'fuente-item border border-gray-300 rounded-md p-3 bg-gray-50 mb-2';
    fuenteDiv.id = `fuente-${contadorFuentes}`;

    fuenteDiv.innerHTML = `
        <div class="flex justify-between items-center mb-2">
            <span class="text-sm font-medium text-gray-700">Fuente ${contadorFuentes}</span>
            <button type="button" data-id="${contadorFuentes}" class="btn-eliminar text-red-500 hover:text-red-700 text-sm">
                Eliminar
            </button>
        </div>

        <div class="space-y-2">
            <div>
                <label class="block text-xs font-medium text-gray-600 mb-1">Tipo</label>
                <select id="fuenteTipo-${contadorFuentes}"
                        class="w-full px-2 py-1 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-gray-300"
                        data-id="${contadorFuentes}">
                    <option value="">Seleccione el tipo de fuente</option>
                    <option value="ESTATICA">Estática</option>
                    <option value="DINAMICA">Dinámica</option>
                    <option value="PROXY">Proxy</option>
                </select>
            </div>

            <div id="nombreContainer-${contadorFuentes}" class="hidden">
                <label class="block text-xs font-medium text-gray-600 mb-1">Nombre</label>
                <input type="text" id="fuenteNombre-${contadorFuentes}"
                       class="w-full px-2 py-1 border border-gray-300 rounded text-sm focus:outline-none focus:ring-2 focus:ring-gray-300"
                       placeholder="Ingrese el nombre">
            </div>
        </div>
    `;

    container.appendChild(fuenteDiv);
}


document.addEventListener("change", e => {
    if (e.target.matches("select[id^='fuenteTipo']")) {
        const id = e.target.dataset.id;
        toggleNombreFuente(id);
    }
});

document.addEventListener('click', function(e) {
    const modal = document.getElementById("modalCrearColeccion")

    if (e.target.closest(".buttonCloseModal")) {
        modal.classList.add('hidden');
    }
});

function toggleNombreFuente(id) {
    const tipo = document.getElementById(`fuenteTipo-${id}`).value;
    const nombreContainer = document.getElementById(`nombreContainer-${id}`);
    nombreContainer.classList.toggle("hidden", !(tipo === "ESTATICA" || tipo === "PROXY"));
}

function eliminarFuente(id) {
    const elemento = document.getElementById(`fuente-${id}`);
    if (elemento) {
        elemento.remove();
    }
}

function toggleModalColeccion() {
    const modal = document.getElementById('modalCrearColeccion');
    const container = document.getElementById('fuentesContainer');
    container.innerHTML = "";
    contadorFuentes = 0;

    if(modal) {
        modal.classList.toggle('hidden');
        modal.querySelectorAll('input, select, textarea').forEach(campo => {
            campo.value = "";
        });
    }
}

document.addEventListener("click", function(e) {
    const btnEliminar = e.target.closest(".btn-eliminar");
    if (btnEliminar) {
        const id = btnEliminar.dataset.id;
        eliminarFuente(id);
    }
});

// --- Función para Crear la Colección (CORREGIDA) ---
function crearColeccion() {
    const titulo = document.getElementById('tituloColeccion').value.trim();
    const descripcion = document.getElementById('descripcionColeccion').value.trim();
    if (!titulo || !descripcion) {
        alert('Por favor, complete el título y la descripción.');
        return;
    }

    // Construir el objeto Criterio
    const criterioTipoSelect = document.getElementById('criterioTipo').value;
    let criterioObjeto = null;
    if (criterioTipoSelect === 'DISTANCIA') {
        const lat = document.getElementById('criterioLatitud').value;
        const lon = document.getElementById('criterioLongitud').value;
        const dist = document.getElementById('criterioDistanciaMinima').value;
        if (!lat || !lon || !dist) {
            alert('Complete todos los campos del criterio de distancia.');
            return;
        }
        criterioObjeto = {
            tipo: 'distancia', // CORREGIDO: minúscula
            ubicacionBase: { latitud: parseFloat(lat), longitud: parseFloat(lon) },
            distanciaMinima: parseFloat(dist)
        };
    } else if (criterioTipoSelect === 'FECHA') {
        const fechaIni = document.getElementById('criterioFechaInicial').value;
        const fechaFin = document.getElementById('criterioFechaFinal').value;
        if (!fechaIni || !fechaFin) {
            alert('Complete ambas fechas del criterio.');
            return;
        }
        // Formato esperado por backend: "YYYY-MM-DDTHH:mm:ss.SSS"
        // Los inputs datetime-local devuelven "YYYY-MM-DDTHH:mm"
        // Podemos añadir segundos y milisegundos si es necesario, o ajustar el backend
        const fechaInicialFormatted = fechaIni + ":00.000"; // Añade segundos y milisegundos
        const fechaFinalFormatted = fechaFin + ":00.000";

        criterioObjeto = {
            tipo: 'fecha', // CORREGIDO: minúscula
            fechaInicial: fechaInicialFormatted,
            fechaFinal: fechaFinalFormatted
        };
    } else {
        alert('Seleccione un criterio de pertenencia.');
        return;
    }

    // CORREGIDO: Envolver el criterio en un array
    const criteriosDePertenencia = [criterioObjeto];

    // Recopilar las Fuentes
    const fuentes = [];
    const fuenteItems = document.querySelectorAll('#fuentesContainer .fuente-item');
    if (fuenteItems.length === 0) {
        alert('Debe agregar al menos una fuente.');
        return;
    }
    try {
        fuenteItems.forEach((item, index) => {
            const i = item.id.split('-')[1];
            const tipoSelect = item.querySelector(`#fuenteTipo-${i}`).value;
            if (!tipoSelect) {
                throw new Error(`Debe seleccionar un tipo para la Fuente ${index + 1}.`);
            }
            const nombreInput = item.querySelector(`#fuenteNombre-${i}`);
            const nombre = nombreInput ? nombreInput.value.trim() : null;
            // Validar: solo se requiere nombre si es estática o proxy
            if ((tipoSelect === 'ESTATICA' || tipoSelect === 'PROXY') && !nombre) {
                throw new Error(`Debe ingresar un nombre para la Fuente ${index + 1} (${tipoSelect}).`);
            }

            fuentes.push({
                tipo: tipoSelect.toLowerCase(),
                id: nombre || "e359660d-9459-4312-9db6-59e4f9c935d4"
            });
        });
    } catch (error) {
        alert(error.message);
        return;
    }

    // Obtener Algoritmo de Consenso
    const algoritmo = document.getElementById('algoritmoConsenso').value;

    // Construir el Payload Final
    const payload = {
        titulo: titulo,
        descripcion: descripcion,
        criteriosDePertenencia: criteriosDePertenencia, // CORREGIDO: nombre y es un array
        fuentes: fuentes,
        tipoAlgoritmoConsenso: algoritmo // CORREGIDO: nombre
    };

    console.log("Enviando Payload:", JSON.stringify(payload, null, 2));

    // Enviar la Petición POST
    fetch('http://localhost:8086/apiAdministrativa/colecciones', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload)
    })
        .then(response => {
            if (response.ok) {
                return response.text().then(text => {
                    console.log('Respuesta del servidor (texto):', text);
                    return text || {};
                });
            } else {
                return response.text().then(text => { throw new Error('Error al crear colección: ' + response.status + (text ? ' - ' + text : '')) });
            }
        })
        .then(data => {
            console.log('Colección creada:', data);
            toggleModalColeccion();
            alert('¡Colección creada con éxito!');
            // Usar setTimeout para que el alert se muestre antes del reload
            setTimeout(() => {
                window.location.reload();
            }, 100);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al crear la colección. Verifique los datos o consulte la consola.\nDetalle: ' + error.message);
        });
}

document.addEventListener('DOMContentLoaded', mostrarCamposCriterio);