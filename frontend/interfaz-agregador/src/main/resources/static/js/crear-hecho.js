document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("modal-hecho");
    const openBtn = document.getElementById("menu-crear-hecho");
    const closeBtn = document.getElementById("salir-crear-hecho");
    const confirmBtn = document.getElementById("crear-hecho")
    const usarCoordenadasCheck = document.getElementById("crear-hecho-usar-coordenadas");
    const multimediaCountObject = {multimediaCount : 0};

    if(allElementsFound([modal, openBtn, closeBtn, confirmBtn, usarCoordenadasCheck], "crear hecho")) {
        listenModalToggle(modal, openBtn, closeBtn, () => limpiarFormularioModalHecho(multimediaCountObject))
        listenUbicacionInputsCrearHecho(usarCoordenadasCheck)
        listenAgregarMultimediaCrearHecho(multimediaCountObject)

        confirmBtn.addEventListener("click", () => publicarHecho(closeBtn, usarCoordenadasCheck.checked))
    }
});

async function publicarHecho(closeBtn, usarCoordenadasCheck) {
    try {
        mostrarCargando("crear-hecho")

        // --- Datos del formulario ---
        const titulo = document.getElementById('crear-hecho-titulo').value.trim();
        const descripcion = document.getElementById('crear-hecho-descripcion').value.trim();
        const categoria = document.getElementById('crear-hecho-categoria').value.trim();
        const fechaInput = document.getElementById('crear-hecho-fecha').value;
        const contenidoTexto = document.getElementById('crear-hecho-contenido-texto').value.trim();
        const anonimato = document.getElementById('crear-hecho-anonimato').checked;
        let ubicacion = { latitud: null, longitud: null };

        if (!fechaInput || !contenidoTexto) {
            throw new Error('Por favor complete todos los campos obligatorios (*)');
        }
        // Validar campos obligatorios básicos
        if (!titulo || !descripcion || !categoria) {
            throw new Error('Por favor complete todos los campos obligatorios (*)');
        }

        const fechaAcontecimiento = fechaInput + ':00';
        const urlsMultimedia = recopilarMultimediasEditarHecho();

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
            hecho.autor = window.autorData.id;
        }

        // --- Selección del endpoint ---
        const endpoint = isAdmin
            ? 'http://localhost:8086/apiAdministrativa/hechos'
            : 'http://localhost:8082/fuentesDinamicas/hechos';

        console.log("Token:", jwtToken); // TODO: Hacer que se obtenga el token

        // --- Envío al backend ---
        const backendResponse = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + jwtToken },
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