document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById("modal-crear-coleccion")
    const openModalBtn = document.getElementById("menu-crear-coleccion")
    const crearBtn = document.getElementById("crear-coleccion");
    const cancelarBtn = document.getElementById("salir-crear-coleccion");
    const agregarFuenteBtn = document.getElementById("crear-coleccion-agregar-fuente");
    const tipoCriterio = document.getElementById('criterio-tipo');
    const container = document.getElementById('fuentes-container');
    let contadorFuentesObject = {contadorFuentes : 0};

    if(allElementsFound([modal, openModalBtn, crearBtn, cancelarBtn, agregarFuenteBtn, tipoCriterio, container], "crear colección")) {
        listenModalToggle(modal, openModalBtn, cancelarBtn, () => cerrarModalCrearColecciones(modal, contadorFuentesObject, tipoCriterio, container));
        listenAgregarFuenteCrearColeccion(agregarFuenteBtn, contadorFuentesObject, container);
        listenMostrarCamposCriterio(tipoCriterio);
        listenMostrarCamposFuente();
        crearBtn.addEventListener("click", crearColeccion)
    }
});

function cerrarModalCrearColecciones(modal, object, tipoCriterio, container) {
    modal.querySelectorAll('input, select, textarea').forEach(campo => {
        campo.value = "";
    });
    tipoCriterio.dispatchEvent(new Event("change"));
    container.innerHTML = "";
    object.contadorFuentes = 0;
}

async function crearColeccion() {
    try {
        mostrarCargando("crear-coleccion");

        const titulo = document.getElementById('titulo-coleccion').value.trim();
        const descripcion = document.getElementById('descripcion-coleccion').value.trim();
        if (!titulo || !descripcion) {
            alert('Por favor, complete el título y la descripción.');
            return;
        }

        // --- Construcción de Criterio ---
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
                tipo: 'distancia',
                ubicacionBase: { latitud: parseFloat(lat), longitud: parseFloat(lon) },
                distanciaMinima: parseFloat(dist)
            };
        }
        else if (criterioTipoSelect === 'FECHA') {
            const fechaIni = document.getElementById('criterio-fecha-inicial').value;
            const fechaFin = document.getElementById('criterio-fecha-final').value;

            if (!fechaIni || !fechaFin) {
                alert('Complete ambas fechas del criterio.');
                return;
            }

            const fechaInicialFormatted = fechaIni + ":00.000";
            const fechaFinalFormatted = fechaFin + ":00.000";

            criterioObjeto = {
                tipo: 'fecha',
                fechaInicial: fechaInicialFormatted,
                fechaFinal: fechaFinalFormatted
            };
        }
        else {
            alert('Seleccione un criterio de pertenencia.');
            return;
        }

        const criteriosDePertenencia = [criterioObjeto];

        // --- Fuentes ---
        const fuentes = [];
        const fuenteItems = document.querySelectorAll('#fuentes-container .fuente-item');

        if (fuenteItems.length === 0) {
            alert('Debe agregar al menos una fuente.');
            return;
        }

        fuenteItems.forEach((item, index) => {
            const i = item.id.split('-')[1];
            const tipoSelect = item.querySelector(`#fuente-tipo-${i}`).value;

            if (!tipoSelect) {
                throw new Error(`Debe seleccionar un tipo para la Fuente ${index + 1}.`);
            }

            const nombreInput = item.querySelector(`#fuente-nombre-${i}`);
            const nombre = nombreInput ? nombreInput.value.trim() : null;

            if ((tipoSelect === 'ESTATICA' || tipoSelect === 'PROXY') && !nombre) {
                throw new Error(`Debe ingresar un nombre para la Fuente ${index + 1} (${tipoSelect}).`);
            }

            fuentes.push({
                tipo: tipoSelect.toLowerCase(),
                id: nombre || "e359660d-9459-4312-9db6-59e4f9c935d4"
            });
        });

        const algoritmo = document.getElementById('algoritmo-consenso').value;

        const payload = {
            titulo,
            descripcion,
            criteriosDePertenencia,
            fuentes,
            tipoAlgoritmoConsenso: algoritmo
        };

        console.log("Enviando Payload:", JSON.stringify(payload, null, 2));

        // --- Petición POST ---
        const response = await fetch('http://localhost:8086/apiAdministrativa/colecciones', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const textResponse = await response.text();
        console.log("Respuesta:", textResponse);

        if (!response.ok) {
            throw new Error(`Error al crear colección (${response.status}): ${textResponse}`);
        }

        // Exitoso:
        document.getElementById("salir-crear-coleccion").click();
        alert("¡Colección creada con éxito!");

        //setTimeout(() => window.location.reload(), 100);

    } catch (error) {
        console.error(error);
        alert(`Error al crear la colección:\n${error.message}`);
    } finally {
        ocultarCargando("crear-coleccion");
    }
}