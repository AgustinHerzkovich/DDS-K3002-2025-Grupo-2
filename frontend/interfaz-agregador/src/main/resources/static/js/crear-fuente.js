document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("modal-fuente");
    const openBtn = document.getElementById("menu-crear-fuente");
    const closeBtn = document.getElementById("salir-crear-fuente");
    const confirmBtn = document.getElementById("crear-fuente")
    const crearFuenteTitle = document.getElementById("modal-crear-fuente-titulo")

    // Referencias a elementos del modal
    const tipoFuenteSelect = document.getElementById("modal-fuente-tipo");
    const staticaContainer = document.getElementById("modal-fuente-estatica-container");
    const proxyContainer = document.getElementById("modal-fuente-proxy-container");
    const cargarUrlCheckbox = document.getElementById("modal-fuente-cargar-url");
    const archivoContainer = document.getElementById("modal-fuente-archivo-container");
    const urlContainer = document.getElementById("modal-fuente-url-container");
    const dropZone = document.getElementById("modal-fuente-drop-zone");
    const fileInput = document.getElementById("modal-fuente-archivos");
    const fileList = document.getElementById("modal-fuente-archivos-lista");

    if(allElementsFound([modal, openBtn, closeBtn, confirmBtn], "crear fuente")) {
        // Debug: verificar que todos los elementos del file upload están disponibles
        console.log('Elementos encontrados:');
        console.log('dropZone:', dropZone);
        console.log('fileInput:', fileInput);
        console.log('fileList:', fileList);
        listenOpenModal(modal, openBtn, () => {
            document.getElementById("dropdown-menu").classList.add("hidden")
            crearFuenteTitle.classList.remove("hidden")
            confirmBtn.parentElement.classList.remove("hidden")
            closeBtn.parentElement.classList.remove("hidden")
        })
        listenCloseModal(modal, closeBtn, () => {
            limpiarModalFuente(modal)
            crearFuenteTitle.classList.add("hidden")
            confirmBtn.parentElement.classList.add("hidden")
            closeBtn.parentElement.classList.add("hidden")
        })

        // Listener para cambio de tipo de fuente
        tipoFuenteSelect.addEventListener('change', function() {
            const tipoSeleccionado = this.value;

            // Ocultar todos los contenedores
            staticaContainer.classList.add('hidden');
            proxyContainer.classList.add('hidden');

            if (tipoSeleccionado === 'estatica') {
                staticaContainer.classList.remove('hidden');
            } else if (tipoSeleccionado === 'proxy') {
                proxyContainer.classList.remove('hidden');
            }
        });

        // Listener para el checkbox de cargar por URL
        cargarUrlCheckbox.addEventListener('change', function() {
            if (this.checked) {
                archivoContainer.classList.add('hidden');
                urlContainer.classList.remove('hidden');
            } else {
                archivoContainer.classList.remove('hidden');
                urlContainer.classList.add('hidden');
            }
        });

        // Funcionalidad de drag & drop para archivos
        dropZone.addEventListener('click', (e) => {
            // No prevenir el default si el click es directamente en el input
            if (e.target === fileInput) {
                return;
            }
            console.log('Dropzone clicked, triggering file input...');
            e.preventDefault();
            e.stopPropagation();

            // Usar setTimeout para asegurar que el click se ejecute después del evento actual
            setTimeout(() => {
                fileInput.click();
            }, 0);
        });

        dropZone.addEventListener('dragover', (e) => {
            e.preventDefault();
            e.stopPropagation();
            dropZone.classList.add('dragover');
        });

        dropZone.addEventListener('dragleave', (e) => {
            e.preventDefault();
            e.stopPropagation();
            dropZone.classList.remove('dragover');
        });

        dropZone.addEventListener('drop', (e) => {
            e.preventDefault();
            e.stopPropagation();
            dropZone.classList.remove('dragover');
            const files = e.dataTransfer.files;
            if (files.length > 0) {
                handleFiles(files);
            }
        });

        fileInput.addEventListener('change', (e) => {
            console.log('File input changed, files selected:', e.target.files.length);
            const files = e.target.files;
            if (files.length > 0) {
                handleFiles(files);
            }
        });

        confirmBtn.addEventListener('click', async function() {
            const inputsObligatorios = validarFormularioModalFuente()

            if(!document.querySelector('#form-modal-fuente .form-not-completed')) {
                await publicarFuente(inputsObligatorios)
            }
        });
    }

    function handleFiles(files) {
        if (!files || files.length === 0) {
            return;
        }

        fileList.innerHTML = '';

        // Filtrar archivos válidos
        const validFiles = Array.from(files).filter(file => {
            const validExtensions = ['.csv', '.json', '.xml'];
            const extension = file.name.toLowerCase().substring(file.name.lastIndexOf('.'));
            return validExtensions.includes(extension);
        });

        if (validFiles.length === 0) {
            alert('Por favor seleccione archivos válidos (.csv, .json, .xml)');
            return;
        }

        // Actualizar el input file con los archivos válidos
        const dt = new DataTransfer();
        validFiles.forEach(file => dt.items.add(file));
        fileInput.files = dt.files;

        // Mostrar lista de archivos
        validFiles.forEach((file, index) => {
            const fileItem = document.createElement('div');
            fileItem.className = 'file-item';
            fileItem.innerHTML = `
                <span class="file-name">${file.name}</span>
                <button type="button" class="remove-file" data-index="${index}">
                    X
                </button>
            `;
            fileList.appendChild(fileItem);
        });

        // Remover estilos de error si hay archivos válidos
        const dropZone = document.getElementById('modal-fuente-drop-zone');
        dropZone.classList.remove('form-not-completed');

        // Listener para remover archivos (solo agregar si no existe)
        if (!fileList.hasAttribute('data-listener-added')) {
            fileList.addEventListener('click', (e) => {
                if (e.target.classList.contains('remove-file') || e.target.parentElement.classList.contains('remove-file')) {
                    const button = e.target.classList.contains('remove-file') ? e.target : e.target.parentElement;
                    const index = parseInt(button.getAttribute('data-index'));
                    removeFile(index);
                }
            });
            fileList.setAttribute('data-listener-added', 'true');
        }
    }

    function removeFile(indexToRemove) {
        const currentFiles = Array.from(fileInput.files);

        if (indexToRemove < 0 || indexToRemove >= currentFiles.length) {
            return;
        }

        const dt = new DataTransfer();
        currentFiles.forEach((file, index) => {
            if (index !== indexToRemove) {
                dt.items.add(file);
            }
        });

        fileInput.files = dt.files;

        // Regenerar la lista de archivos
        updateFileList();
    }

    function updateFileList() {
        fileList.innerHTML = '';

        if (fileInput.files.length === 0) {
            return;
        }

        Array.from(fileInput.files).forEach((file, index) => {
            const fileItem = document.createElement('div');
            fileItem.className = 'file-item';
            fileItem.innerHTML = `
                <span class="file-name">${file.name}</span>
                <button type="button" class="remove-file" data-index="${index}">
                    <i class="fas fa-times"></i>
                </button>
            `;
            fileList.appendChild(fileItem);
        });
    }
});

async function publicarFuente(inputsObligatorios) {
    try {
        mostrarCargando("crear-fuente")

        const tipoFuente = inputsObligatorios.tipoFuente.value;
        const cargaUrl = inputsObligatorios.cargarUrl.checked;

        let endpoint = '';
        let requestData;
        let headers = { 'Authorization': 'Bearer ' + jwtToken };

        if (tipoFuente === 'estatica') {
            if (cargaUrl) {
                // Cargar por URL - el backend espera solo el string de la URL
                endpoint = 'http://localhost:8086/apiAdministrativa/archivos/por-url';
                headers['Content-Type'] = 'application/json';
                const urlValue = inputsObligatorios.url.value.trim();
                requestData = JSON.stringify(urlValue);
                console.log('Enviando URL:', urlValue);
                console.log('Request body:', requestData);
            } else {
                // Cargar archivos - el parámetro debe llamarse 'files' no 'archivos'
                endpoint = 'http://localhost:8086/apiAdministrativa/archivos';
                const formData = new FormData();

                // Agregar todos los archivos seleccionados con el nombre 'files'
                Array.from(inputsObligatorios.archivos.files).forEach(file => {
                    formData.append('files', file);
                });

                requestData = formData;
                // No establecer Content-Type para FormData, el browser lo hará automáticamente
            }
        } else if (tipoFuente === 'proxy') {
            // TODO: Implementar cuando esté disponible
            throw new Error('La funcionalidad de fuente proxy aún no está implementada');
        }

        console.log('Enviando request a:', endpoint);
        console.log('Headers:', headers);

        const response = await fetch(endpoint, {
            method: 'POST',
            headers: headers,
            body: requestData
        });

        console.log('Response status:', response.status);

        if (!response.ok) {
            const errorText = await response.text();
            console.error('Error response:', errorText);
            throw new Error(errorText ? `Error al crear la fuente (${response.status}):\n${errorText}` : `Error al crear la fuente (${response.status})`)
        }

        const responseText = await response.text();
        console.log('Success response:', responseText);

        alert('Fuente creada exitosamente');
        document.getElementById("salir-crear-fuente").click();
        window.location.reload();
    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        ocultarCargando("crear-fuente")
    }
}