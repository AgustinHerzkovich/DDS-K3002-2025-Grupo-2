function validarFormularioModalFuente() {
    const tipoFuente = document.getElementById('modal-fuente-tipo');
    const cargarUrl = document.getElementById('modal-fuente-cargar-url');
    const archivos = document.getElementById('modal-fuente-archivos');
    const url = document.getElementById('modal-fuente-url');
    const dropZone = document.getElementById('modal-fuente-drop-zone');

    // Limpiar estados de error previos de elementos especiales
    dropZone.classList.remove('form-not-completed');

    // Validar campos básicos obligatorios
    validarInputsObligatorios([tipoFuente]);

    // Validar campos específicos según el tipo de fuente
    if (tipoFuente.value === 'estatica') {
        if (cargarUrl.checked) {
            // Validar URL
            validarInputsObligatorios([url]);
            // Validación adicional de formato URL
            if (url.value.trim() && !isValidUrl(url.value.trim())) {
                url.classList.add('form-not-completed');
                url.classList.remove('form-input');
            }
        } else {
            // Validar archivos - marcar el drop zone como inválido si no hay archivos
            if (!archivos.files || archivos.files.length === 0) {
                dropZone.classList.add('form-not-completed');
            } else {
                dropZone.classList.remove('form-not-completed');
            }
        }
    }
    // No hay validaciones específicas para proxy ya que muestra un TODO

    return {
        tipoFuente,
        cargarUrl,
        archivos,
        url
    };
}

function limpiarModalFuente(modal) {
    // Limpiar estilos de validación
    modal.querySelectorAll('input, select, textarea').forEach(campo => {
        campo.classList.remove("form-not-completed");
        if (!campo.classList.contains('form-check-input')) {
            campo.classList.add("form-input");
        }
    });

    // Limpiar drop zone
    const dropZone = document.getElementById('modal-fuente-drop-zone');
    dropZone.classList.remove('form-not-completed');
    dropZone.classList.remove('dragover');

    // Resetear formulario
    const form = document.getElementById('form-modal-fuente');
    form.reset();

    // Ocultar todas las secciones específicas
    document.getElementById('modal-fuente-proxy-container').classList.add('hidden');
    document.getElementById('modal-fuente-estatica-container').classList.add('hidden');
    document.getElementById('modal-fuente-url-container').classList.add('hidden');

    // Mostrar contenedor de archivo por defecto
    document.getElementById('modal-fuente-archivo-container').classList.remove('hidden');

    // Limpiar lista de archivos
    document.getElementById('modal-fuente-archivos-lista').innerHTML = '';

    // Resetear checkbox
    document.getElementById('modal-fuente-cargar-url').checked = false;

    // Limpiar archivos seleccionados
    const fileInput = document.getElementById('modal-fuente-archivos');
    fileInput.value = '';
}

function isValidUrl(string) {
    try {
        new URL(string);
        return true;
    } catch (_) {
        return false;
    }
}
