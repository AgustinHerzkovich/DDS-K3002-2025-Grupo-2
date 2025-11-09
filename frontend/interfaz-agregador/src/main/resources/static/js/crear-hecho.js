// Variable para llevar el conteo de multimedias
let multimediaCount = 0;

// Función para toggle del modal
function toggleModalHecho() {
    const modal = document.getElementById('modalCrearHecho');

    if (modal.style.display === 'none' || modal.style.display === '') {
        // Abrir modal
        modal.style.display = 'flex'; // Cambiado a flex para centrar
        // Evitar scroll en el body
        document.body.style.overflow = 'hidden';
    } else {
        // Cerrar modal
        modal.style.display = 'none';
        // Restaurar scroll en el body
        document.body.style.overflow = '';

        // Limpiar formulario al cerrar
        limpiarFormulario();
    }
}


// Función para agregar un nuevo campo de URL de multimedia
function agregarMultimedia() {
    multimediaCount++;
    const container = document.getElementById('multimediaContainer');

    const multimediaDiv = document.createElement('div');
    // Usamos flex para alinear input y botón
    multimediaDiv.className = 'multimedia-item flex items-center gap-2';
    multimediaDiv.id = `multimedia-${multimediaCount}`;

    multimediaDiv.innerHTML = `
                <input type="text" style="color:black"
                       class="form-input flex-grow border-gray-300 rounded-md shadow-sm text-sm"
                       id="url-${multimediaCount}"
                       placeholder="https://ejemplo.com/imagen.jpg" required>
                <button type="button" class="text-red-500 hover:text-red-700 p-1 rounded-full bg-red-100 hover:bg-red-200" onclick="eliminarMultimedia(${multimediaCount})">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                    </svg>
                </button>
            `;

    container.appendChild(multimediaDiv);
}

// Función para eliminar un multimedia
function eliminarMultimedia(id) {
    const elemento = document.getElementById(`multimedia-${id}`);
    if (elemento) {
        elemento.remove();
    }
}

// Función para recopilar solo las URLs de multimedias
function recopilarMultimedias() {
    const urls = [];
    const container = document.getElementById('multimediaContainer');
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
async function publicarHecho(isAdmin=false) {
    // Obtener valores del formulario
    console.log(isAdmin);
    const titulo = document.getElementById('titulo').value;
    const descripcion = document.getElementById('descripcion').value;
    const categoria = document.getElementById('categoria').value;
    const usarCoordenadas = document.getElementById('usarCoordenadas').checked;
    let ubicacion = {};

    if (usarCoordenadas) {
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
    const contenidoTexto = document.getElementById('contenidoTexto').value;
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
                toggleModalHecho();
                // Opcional: Recargar la página o actualizar la lista de hechos dinámicamente
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

// Mostrar u ocultar inputs según el modo de ubicación
function toggleUbicacionInputs() {
  const usarCoordenadas = document.getElementById("usarCoordenadas").checked;
  const direccionContainer = document.getElementById("direccionContainer");
  const coordenadasContainer = document.getElementById("coordenadasContainer");

  if (usarCoordenadas) {
    direccionContainer.style.display = "none";
    coordenadasContainer.style.display = "flex";

    // Hacer requeridas las coordenadas
    document.getElementById("latitud").required = true;
    document.getElementById("longitud").required = true;

    // Quitar required de dirección
    document.getElementById("pais").required = false;
    document.getElementById("provincia").required = false;
    document.getElementById("ciudad").required = false;
    document.getElementById("calle").required = false;
    document.getElementById("altura").required = false;
  } else {
    direccionContainer.style.display = "flex";
    coordenadasContainer.style.display = "none";

    // Hacer requeridos los campos de dirección
    document.getElementById("pais").required = true;
    document.getElementById("provincia").required = true;
    document.getElementById("ciudad").required = true;
    document.getElementById("calle").required = true;
    document.getElementById("altura").required = true;

    // Quitar required de coordenadas
    document.getElementById("latitud").required = false;
    document.getElementById("longitud").required = false;
  }
}

// Función para limpiar el formulario
function limpiarFormulario() {
    document.getElementById('formCrearHecho').reset();
    document.getElementById('multimediaContainer').innerHTML = '';
    multimediaCount = 0;
}