function limpiarFormularioModalHecho(multimediaCountObject) {
    document.getElementById('form-modal-hecho').reset();
    document.getElementById('crear-hecho-multimedia-container').innerHTML = '';
    multimediaCountObject.multimediaCount = 0;
}

function recopilarMultimediasEditarHecho() {
    const urls = [];
    const container = document.getElementById('crear-hecho-multimedia-container');
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

function limpiarModalCrearColeccion(modal, object, tipoCriterio, container) {
    modal.querySelectorAll('input, select, textarea').forEach(campo => {
        campo.value = "";
    });
    tipoCriterio.dispatchEvent(new Event("change"));
    container.innerHTML = "";
    object.contadorFuentes = 0;
}

function moveBetweenModals(exitModalBtnId, enterModalBtnId) {
    document.getElementById(exitModalBtnId).click();
    document.getElementById(enterModalBtnId).click();
}

function allElementsFound(elementsList, desiredAction) {
    if(elementsList.some(e => e === null)) {
        console.log("No se pudo encontrar algún elemento para " + desiredAction)
    }

    return !elementsList.some(e => e === null);
}

function marcarCampos(camposInput, camposNoCompletados) {
    camposInput.forEach(e => {
        const estaVacio = (e.value === "" || e.value == null);

        if (estaVacio) {
            camposNoCompletados.push(e)
            e.classList.add("form-not-completed");
        } else {
            e.classList.remove("form-not-completed");
        }
    });
}