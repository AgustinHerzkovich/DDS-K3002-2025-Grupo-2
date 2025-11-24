document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById("modal-crear-coleccion")
    const openModalBtn = document.getElementById("menu-crear-coleccion")
    const crearBtn = document.getElementById("crear-coleccion");
    const cancelarBtn = document.getElementById("salir-crear-coleccion");
    const agregarFuenteBtn = document.getElementById("crear-coleccion-agregar-fuente");
    const tipoCriterio = document.getElementById('criterio-tipo');
    const container = document.getElementById('fuentes-container');
    let contadorFuentesObject = {contadorFuentes : 0};

    listenModalToggle(modal, openModalBtn, cancelarBtn, () => cerrarModalCrearColecciones(modal, contadorFuentesObject, tipoCriterio, container));
    listenModalConfirm(crearBtn, crearColeccion);
    listenAgregarFuente(agregarFuenteBtn, contadorFuentesObject, container);
    listenMostrarCamposCriterio(tipoCriterio);
    listenMostrarCamposFuente();
});

function cerrarModalCrearColecciones(modal, object, tipoCriterio, container) {
    modal.querySelectorAll('input, select, textarea').forEach(campo => {
        campo.value = "";
    });
    tipoCriterio.dispatchEvent(new Event("change"));
    container.innerHTML = "";
    object.contadorFuentes = 0;
}

function crearColeccion() {
    const titulo = document.getElementById('titulo-coleccion').value.trim();
    const descripcion = document.getElementById('descripcion-coleccion').value.trim();
    if (!titulo || !descripcion) {
        alert('Por favor, complete el título y la descripción.');
        return;
    }

    // Construir el objeto Criterio
    const criterioTipoSelect = document.getElementById('criterio-tipo').value;
    let criterioObjeto = null;
    if (criterioTipoSelect === 'DISTANCIA') {
        const lat = document.getElementById('criterio-latitud').value;
        const lon = document.getElementById('criterio-longitud').value;
        const dist = document.getElementById('criterio-distancia-minima').value;
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
        const fechaIni = document.getElementById('criterio-fecha-inicial').value;
        const fechaFin = document.getElementById('criterio-fecha-final').value;
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
    const fuenteItems = document.querySelectorAll('#fuentes-container .fuente-item');
    if (fuenteItems.length === 0) {
        alert('Debe agregar al menos una fuente.');
        return;
    }
    try {
        fuenteItems.forEach((item, index) => {
            const i = item.id.split('-')[1];
            const tipoSelect = item.querySelector(`#fuente-tipo-${i}`).value;
            if (!tipoSelect) {
                throw new Error(`Debe seleccionar un tipo para la Fuente ${index + 1}.`);
            }
            const nombreInput = item.querySelector(`#fuente-nombre-${i}`);
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
    const algoritmo = document.getElementById('algoritmo-consenso').value;

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

            const salirBtn = document.getElementById("salir-crear-coleccion");
            salirBtn.click()

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