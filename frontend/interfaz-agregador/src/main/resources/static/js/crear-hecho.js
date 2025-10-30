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
function publicarHecho() {
    // Obtener valores del formulario
    const titulo = document.getElementById('titulo').value;
    const descripcion = document.getElementById('descripcion').value;
    const categoria = document.getElementById('categoria').value;
    const latitud = parseFloat(document.getElementById('latitud').value);
    const longitud = parseFloat(document.getElementById('longitud').value);
    const fechaInput = document.getElementById('fecha').value;
    const contenidoTexto = document.getElementById('contenidoTexto').value;
    const anonimato = document.getElementById('anonimato').checked;

    // Validar campos básicos
    if (!titulo || !descripcion || !categoria || isNaN(latitud) || isNaN(longitud) || !fechaInput || !contenidoTexto) {
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
        ubicacion: {
            latitud: latitud,
            longitud: longitud
        },
        fechaAcontecimiento: fechaAcontecimiento,
        origen: 'CONTRIBUYENTE',
        contenidoTexto: contenidoTexto,
        contenidoMultimedia: urlsMultimedia,
        anonimato: anonimato
    };


    if (!anonimato && window.autorData) {

        hecho.autor = { id: window.autorData.id };
    }

    console.log('Hecho a publicar:', hecho);
    console.log('JSON del hecho:', JSON.stringify(hecho, null, 2));

    // Enviar al backend
    fetch('http://localhost:8085/apiPublica/hechos', {
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

// Función para limpiar el formulario
function limpiarFormulario() {
    document.getElementById('formCrearHecho').reset();
    document.getElementById('multimediaContainer').innerHTML = '';
    multimediaCount = 0;
}