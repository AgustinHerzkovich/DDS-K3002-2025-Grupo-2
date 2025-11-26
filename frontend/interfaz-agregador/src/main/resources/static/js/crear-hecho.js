document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("modal-crear-hecho");
    const openBtn = document.getElementById("menu-crear-hecho");
    const closeBtn = document.getElementById("salir-crear-hecho");
    const confirmBtn = document.getElementById("crear-hecho")
    const usarCoordenadasCheck = document.getElementById("btn-usar-coordenadas").checked;
    const misHechosBtn = document.getElementById("salir-mis-hechos")
    const multimediaCountObject = {multimediaCount : 0};

    if(allElementsFound([modal, openBtn, closeBtn, confirmBtn, usarCoordenadasCheck, misHechosBtn], "crear hecho")) {
        listenModalToggle(modal, openBtn, closeBtn, () => limpiarFormulario(multimediaCountObject))
        listenEditarHecho(openBtn, )
        listenMostrarUbicacionInputs(usarCoordenadasCheck)
        listenAgregarMultimedia(multimediaCountObject)
    }
});

async function guardarEdicion(hechoId) {
    // Obtener todos los valores del formulario igual que en publicarHecho
    const titulo = document.getElementById('titulo').value;
    const descripcion = document.getElementById('descripcion').value;
    const categoria = document.getElementById('categoria').value;
    const contenidoTexto = document.getElementById('contenido-texto').value;
    const fechaInput = document.getElementById('fecha').value;
    const anonimato = document.getElementById('anonimato').checked;
    const urlsMultimedia = recopilarMultimedias();

    let ubicacion = {};
    if (document.getElementById('btn-usar-coordenadas').checked) {
        ubicacion = {
            latitud: parseFloat(document.getElementById('latitud').value),
            longitud: parseFloat(document.getElementById('longitud').value)
        };
    }

    const hechoEditado = {
        titulo,
        descripcion,
        categoria: { nombre: categoria },
        ubicacion,
        fechaAcontecimiento: fechaInput + ':00',
        contenidoTexto,
        contenidoMultimedia: urlsMultimedia,
        anonimato
    };


    try {
        const response = await fetch(`http://localhost:8082/fuentesDinamicas/hechos/${hechoId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(hechoEditado)
        });

        if (response.ok) {
            alert('Hecho actualizado exitosamente');
            toggleModalCrearHecho();
            location.reload();
        } else {
            throw new Error('Error al actualizar el hecho');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al actualizar el hecho');
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

// Función para publicar el hecho (actualizada para usar array de URLs)
async function publicarHecho(closeBtn, usarCoordenadasCheck, isAdmin=false) {
    // Obtener valores del formulario
    console.log(isAdmin);
    const titulo = document.getElementById('titulo').value;
    const descripcion = document.getElementById('descripcion').value;
    const categoria = document.getElementById('categoria').value;
    let ubicacion = {};

    if (usarCoordenadasCheck) {
        // Caso manual
        const latitud = parseFloat(document.getElementById('latitud').value);
        const longitud = parseFloat(document.getElementById('longitud').value);

        if (isNaN(latitud) || isNaN(longitud)) {
            alert('Debe ingresar latitud y longitud válidas.');
            return;
        }

        ubicacion = { latitud, longitud };
    } else {
        // Caso dirección -> geocoding
        const pais = document.getElementById('pais').value.trim();
        const provincia = document.getElementById('provincia').value.trim();
        const ciudad = document.getElementById('ciudad').value.trim();
        const calle = document.getElementById('calle').value.trim();
        const altura = document.getElementById('altura').value.trim();

        if (!pais || !provincia || !ciudad || !calle || !altura) {
            alert('Por favor complete todos los campos de dirección.');
            return;
        }

        const direccionCompleta = `${calle} ${altura}, ${ciudad}, ${provincia}, ${pais}`;
        console.log('Buscando coordenadas para:', direccionCompleta);

        try {
            const response = await fetch(
                `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(direccionCompleta)}`
            );
            const data = await response.json();

            if (!data || data.length === 0) {
                alert('No se pudo obtener la ubicación geográfica. Verifique la dirección ingresada.');
                return;
            }

            const latitud = parseFloat(data[0].lat);
            const longitud = parseFloat(data[0].lon);
            ubicacion = { latitud, longitud };
            console.log('Ubicación geocodificada:', ubicacion);

        } catch (error) {
            console.error('Error al obtener coordenadas:', error);
            alert('Hubo un error al obtener la ubicación. Intente nuevamente.');
            return;
        }
    }

    const fechaInput = document.getElementById('fecha').value;
    const contenidoTexto = document.getElementById('contenido-texto').value;
    const anonimato = document.getElementById('anonimato').checked;

    if (!titulo || !descripcion || !categoria || !ubicacion.latitud || !ubicacion.longitud || !fechaInput || !contenidoTexto) {
        alert('Por favor complete todos los campos obligatorios (*)');
        return;
    }

    // Convertir la fecha de datetime-local a formato ISO
    const fechaAcontecimiento = fechaInput + ':00'; // Agregar segundos

    const urlsMultimedia = recopilarMultimedias();

    // Crear objeto de hecho
    const hecho = {
        titulo: titulo,
        descripcion: descripcion,
        categoria: {
            nombre: categoria
        },
        ubicacion,
        fechaAcontecimiento: fechaAcontecimiento,
        origen: isAdmin ? 'CARGA_MANUAL': 'CONTRIBUYENTE',
        contenidoTexto: contenidoTexto,
        contenidoMultimedia: urlsMultimedia,
        anonimato: anonimato
    };


    // console.log("anonimato:", anonimato);
    // console.log("window.autorData:", window.autorData);
    if (!anonimato && window.autorData) {
        console.log("id es:", window.autorData.id);
        hecho.contribuyenteId = window.autorData.id;
    }

    console.log('Hecho a publicar:', hecho);
    console.log('JSON del hecho:', JSON.stringify(hecho, null, 2));
    const endpoint = isAdmin ? 'http://localhost:8086/apiAdministrativa/hechos' : 'http://localhost:8082/fuentesDinamicas/hechos';

    // Enviar al backend
    fetch(endpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(hecho)
    })
        .then(response => {
            if (response.ok) {
                alert('Hecho publicado exitosamente');
                closeBtn.click();
                location.reload();
            } else {
                // Intenta leer el mensaje de error del backend si existe
                return response.text().then(text => {
                    throw new Error('Error al publicar el hecho: ' + (text || response.status));
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al publicar el hecho.\nDetalle: ' + error.message);
        });
}

// Función para limpiar el formulario
function limpiarFormulario(multimediaCountObject) {
    document.getElementById('form-crear-hecho').reset();
    document.getElementById('multimedia-container').innerHTML = '';
    multimediaCountObject.multimediaCount = 0;
}