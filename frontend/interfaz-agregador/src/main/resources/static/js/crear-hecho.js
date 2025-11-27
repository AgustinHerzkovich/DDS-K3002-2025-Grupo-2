document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("modal-crear-hecho");
    const openBtn = document.getElementById("menu-crear-hecho");
    const closeBtn = document.getElementById("salir-crear-hecho");
    const confirmBtn = document.getElementById("crear-hecho")
    const usarCoordenadasCheck = document.getElementById("btn-usar-coordenadas");
    const misHechosBtn = document.getElementById("salir-mis-hechos")
    const multimediaCountObject = {multimediaCount : 0};

    if(allElementsFound([modal, openBtn, closeBtn, confirmBtn, usarCoordenadasCheck, misHechosBtn], "crear hecho")) {
        listenModalToggle(modal, openBtn, closeBtn, () => limpiarFormulario(multimediaCountObject))
        listenMostrarUbicacionInputs(usarCoordenadasCheck)
        listenAgregarMultimedia(multimediaCountObject)

        confirmBtn.addEventListener("click", () => publicarHecho(closeBtn, usarCoordenadasCheck.checked, window.isAdmin))
    }
});

function limpiarFormulario(multimediaCountObject) {
    document.getElementById('form-crear-hecho').reset();
    document.getElementById('multimedia-container').innerHTML = '';
    multimediaCountObject.multimediaCount = 0;
}

// Función para publicar el hecho (actualizada para usar array de URLs)
async function publicarHecho(closeBtn, usarCoordenadasCheck, isAdmin = false) {
    try {
        mostrarCargando("crear-hecho")

        // --- Datos del formulario ---
        const titulo = document.getElementById('crear-hecho-titulo').value.trim();
        const descripcion = document.getElementById('crear-hecho-descripcion').value.trim();
        const categoria = document.getElementById('crear-hecho-categoria').value.trim();
        let ubicacion = { latitud: null, longitud: null };

        // Validar campos obligatorios básicos
        if (!titulo || !descripcion || !categoria) {
            throw new Error('Por favor complete todos los campos obligatorios (*)');
        }

        // --- Ubicación ---
        if (usarCoordenadasCheck) {
            const latitud = parseFloat(document.getElementById('crear-hecho-latitud').value);
            const longitud = parseFloat(document.getElementById('crear-hecho-longitud').value);
            if (isNaN(latitud) || isNaN(longitud)) {
                throw new Error('Debe ingresar una latitúd y longitúd válidas.');
            }
            ubicacion = { latitud, longitud };
        } else {
            const pais = document.getElementById('crear-hecho-pais').value.trim();
            const provincia = document.getElementById('crear-hecho-provincia').value.trim();
            const ciudad = document.getElementById('crear-hecho-ciudad').value.trim();
            const calle = document.getElementById('crear-hecho-calle').value.trim();
            const altura = document.getElementById('crear-hecho-altura').value.trim();

            if (!pais || !provincia || !ciudad || !calle || !altura) {
                throw new Error('Por favor complete todos los campos de dirección.');
            }

            const direccionCompleta = `${calle} ${altura}, ${ciudad}, ${provincia}, ${pais}`;
            const response = await fetch(
                `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(direccionCompleta)}`,
                { headers: { "User-Agent": "MetaMapa/1.0" } }
            );
            const data = await response.json();

            if (!data || data.length === 0) {
                throw new Error('No se pudo obtener la ubicación geográfica. Verifique la dirección ingresada.');
            }

            ubicacion = { latitud: parseFloat(data[0].lat), longitud: parseFloat(data[0].lon) };
        }

        // --- Otros campos ---
        const fechaInput = document.getElementById('crear-hecho-fecha').value;
        const contenidoTexto = document.getElementById('crear-hecho-contenido-texto').value.trim();
        const anonimato = document.getElementById('crear-hecho-anonimato').checked;

        if (!fechaInput || !contenidoTexto || ubicacion.latitud == null || ubicacion.longitud == null) {
            throw new Error('Por favor complete todos los campos obligatorios (*)');
        }

        const fechaAcontecimiento = fechaInput + ':00';
        const urlsMultimedia = recopilarMultimedias();

        // --- Objeto Hecho ---
        const hecho = {
            titulo,
            descripcion,
            categoria: { nombre: categoria },
            ubicacion,
            fechaAcontecimiento,
            origen: isAdmin ? 'CARGA_MANUAL' : 'CONTRIBUYENTE',
            contenidoTexto,
            contenidoMultimedia: urlsMultimedia,
            anonimato
        };

        if (!anonimato && window.autorData) {
            hecho.contribuyenteId = window.autorData.id;
        }

        const endpoint = isAdmin
            ? 'http://localhost:8086/apiAdministrativa/hechos'
            : 'http://localhost:8082/fuentesDinamicas/hechos';

        // --- Envío al backend ---
        const backendResponse = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(hecho)
        });

        if (!backendResponse.ok) {
            const text = await backendResponse.text();
            throw new Error('Error al publicar el hecho: ' + (text || backendResponse.status));
        }

        alert('Hecho publicado exitosamente');
        closeBtn.click();
        location.reload();

    } catch (error) {
        console.error(error);
        alert(`Error al publicar el hecho:\n${error.message}`);
    } finally {
        ocultarCargando("crear-hecho")
    }
}

// Función para recopilar solo las URLs de multimedias
function recopilarMultimedias() {
    const urls = [];
    const container = document.getElementById('multimedia-container');
    const multimediaItems = container.querySelectorAll('.multimedia-item');

    multimediaItems.forEach(item => {
        const id = item.id.split('-')[1];
        const urlInput = document.getElementById(`url-${id}`);
        if (urlInput && urlInput.value.trim()) { // Solo añadir si no está vacío
            urls.push(urlInput.value.trim());
        }
    });

    return urls; // Devuelve un array de strings (URLs)
}