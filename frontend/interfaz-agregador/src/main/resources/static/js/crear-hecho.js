// Variable para llevar el conteo de multimedias
let multimediaCount = 0;

// Función para toggle del modal
function toggleModal() {
    const modal = document.getElementById('modalCrearHecho');

    if (modal.style.display === 'none' || modal.style.display === '') {
        // Abrir modal
        modal.style.display = 'block';
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

// Función para agregar un nuevo multimedia
function agregarMultimedia() {
    multimediaCount++;
    const container = document.getElementById('multimediaContainer');

    const multimediaDiv = document.createElement('div');
    multimediaDiv.className = 'multimedia-item border p-3 mb-3 rounded';
    multimediaDiv.id = `multimedia-${multimediaCount}`;

    multimediaDiv.innerHTML = `
        <div class="d-flex justify-content-between align-items-center mb-2">
            <h6 class="mb-0">Multimedia ${multimediaCount}</h6>
            <button type="button" class="btn btn-sm btn-danger" onclick="eliminarMultimedia(${multimediaCount})">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">
                    <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z"/>
                    <path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4zM2.5 3h11V2h-11z"/>
                </svg>
            </button>
        </div>
        
        <div class="row g-2">
            <div class="col-md-6">
                <label class="form-label">URL</label>
                <input type="text" class="form-control" id="url-${multimediaCount}" placeholder="URL del archivo" required>
            </div>
            
            <div class="col-md-3">
                <label class="form-label">Formato</label>
                <input type="text" class="form-control" id="formato-${multimediaCount}" placeholder="Ej: mp4, jpg" required>
            </div>
            
            <div class="col-md-3">
                <label class="form-label">Tamaño (MB)</label>
                <input type="number" class="form-control" id="tamaño-${multimediaCount}" placeholder="Tamaño" step="0.01" required>
            </div>
            
            <div class="col-md-12">
                <label class="form-label">Tipo</label>
                <select class="form-select" id="tipo-${multimediaCount}" onchange="cambiarTipoMultimedia(${multimediaCount})">
                    <option value="">Seleccione un tipo</option>
                    <option value="imagen">Imagen</option>
                    <option value="audio">Audio</option>
                    <option value="video">Video</option>
                </select>
            </div>
            
            <!-- Campos específicos que aparecerán según el tipo -->
            <div class="col-md-12" id="campos-especificos-${multimediaCount}">
                <!-- Se llenarán dinámicamente -->
            </div>
        </div>
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

// Función para cambiar los campos según el tipo de multimedia
function cambiarTipoMultimedia(id) {
    const tipo = document.getElementById(`tipo-${id}`).value;
    const camposEspecificos = document.getElementById(`campos-especificos-${id}`);

    // Limpiar campos específicos
    camposEspecificos.innerHTML = '';

    if (tipo === 'imagen') {
        camposEspecificos.innerHTML = `
            <div class="row g-2 mt-2">
                <div class="col-md-12">
                    <label class="form-label">Resolución</label>
                    <input type="text" class="form-control" id="resolucion-${id}" placeholder="Ej: 1920x1080" required>
                </div>
            </div>
        `;
    } else if (tipo === 'audio') {
        camposEspecificos.innerHTML = `
            <div class="row g-2 mt-2">
                <div class="col-md-12">
                    <label class="form-label">Duración (segundos)</label>
                    <input type="number" class="form-control" id="duracion-${id}" placeholder="Duración en segundos" required>
                </div>
            </div>
        `;
    } else if (tipo === 'video') {
        camposEspecificos.innerHTML = `
            <div class="row g-2 mt-2">
                <div class="col-md-6">
                    <label class="form-label">Resolución</label>
                    <input type="text" class="form-control" id="resolucion-${id}" placeholder="Ej: 1920x1080" required>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Duración (segundos)</label>
                    <input type="number" class="form-control" id="duracion-${id}" placeholder="Duración en segundos" required>
                </div>
            </div>
        `;
    }
}

// Función para recopilar todos los multimedias
function recopilarMultimedias() {
    const multimedias = [];
    const container = document.getElementById('multimediaContainer');
    const multimediaItems = container.querySelectorAll('.multimedia-item');

    multimediaItems.forEach(item => {
        const id = item.id.split('-')[1];
        const tipo = document.getElementById(`tipo-${id}`).value;

        if (!tipo) {
            return; // Saltar si no tiene tipo seleccionado
        }

        const multimedia = {
            url: document.getElementById(`url-${id}`).value,
            formato: document.getElementById(`formato-${id}`).value,
            tamaño: parseFloat(document.getElementById(`tamaño-${id}`).value)
        };

        // Agregar campos específicos según el tipo
        if (tipo === 'imagen') {
            multimedia.resolucion = document.getElementById(`resolucion-${id}`).value;
            multimedia.tipo = 'imagen';
        } else if (tipo === 'audio') {
            multimedia.duracion = parseInt(document.getElementById(`duracion-${id}`).value);
            multimedia.tipo = 'audio';
        } else if (tipo === 'video') {
            multimedia.resolucion = document.getElementById(`resolucion-${id}`).value;
            multimedia.duracion = parseInt(document.getElementById(`duracion-${id}`).value);
            multimedia.tipo = 'video';
        }

        multimedias.push(multimedia);
    });

    return multimedias;
}

// Función para publicar el hecho
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
    if (!titulo || !descripcion || !categoria || !latitud || !longitud || !fechaInput || !contenidoTexto) {
        alert('Por favor complete todos los campos obligatorios');
        return;
    }

    // Convertir la fecha de datetime-local a formato ISO (ya viene en formato correcto)
    const fechaAcontecimiento = fechaInput + ':00'; // Agregar segundos si no los tiene

    // Recopilar multimedias
    const multimedias = recopilarMultimedias();

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
        contenidoMultimedia: multimedias,
        anonimato: anonimato
    };

    // Agregar autor solo si no es anónimo
    if (!anonimato && window.autorData) {
        hecho.autor = window.autorData;
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
            toggleModal();
            // Recargar la página para ver el nuevo hecho
            location.reload();
        } else {
            return response.text().then(text => {
                alert('Error al publicar el hecho: ' + text);
            });
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error al publicar el hecho');
    });
}

// Función para limpiar el formulario
function limpiarFormulario() {
    document.getElementById('formCrearHecho').reset();
    document.getElementById('multimediaContainer').innerHTML = '';
    multimediaCount = 0;
}

