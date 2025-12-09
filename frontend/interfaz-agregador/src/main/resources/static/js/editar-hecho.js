document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("modal-hecho");
    const openBtn = document.getElementById("hecho-editar");
    const closeBtn = document.getElementById("salir-editar-hecho")
    const modalTitle = document.getElementById("modal-editar-hecho-titulo")
    const confirmBtn = document.getElementById("editar-hecho")
    const anonimatoContainer = document.getElementById("modal-hecho-anonimato-container")
    const agregarBtn = document.getElementById("modal-editar-hecho-agregar-multimedia")
    let direccionHecho = {}
    let primeraVezEditando = true

    if(allElementsFound([modal, openBtn, closeBtn, confirmBtn, anonimatoContainer, agregarBtn], "editar hecho")) {
        listenOpenModal(modal, openBtn, async function() {
            if(primeraVezEditando) {
                try {
                    direccionHecho = await obtenerDireccionEditarHecho()
                } catch (error) {
                    console.error(error);
                    return;
                }
                primeraVezEditando = false
            }

            autocompletarModalHecho(direccionHecho)

            modalTitle.classList.remove("hidden")
            agregarBtn.classList.remove("hidden")
            anonimatoContainer.classList.add("hidden")
            confirmBtn.parentElement.classList.remove("hidden")
            closeBtn.parentElement.classList.remove("hidden")
        })
        listenCloseModal(modal, closeBtn, () => {
            limpiarModalHecho(modal)

            modalTitle.classList.add("hidden")
            agregarBtn.classList.add("hidden")
            anonimatoContainer.classList.remove("hidden")
            confirmBtn.parentElement.classList.add("hidden")
            closeBtn.parentElement.classList.add("hidden")
        })
        listenAgregarMultimediaModalHecho(agregarBtn)

        confirmBtn.addEventListener("click", async function() {
            const inputsObligatorios = validarFormularioModalHecho()

            if(!document.querySelector('#form-modal-hecho .form-not-completed')) {
                await guardarEdicion(inputsObligatorios)
            }
        })
    }
})

async function guardarEdicion(inputsObligatorios) {
    try {
        mostrarCargando("editar-hecho")

        let payload;
        try {
            payload = await getPayloadModalHecho(inputsObligatorios)
        } catch (error) {
            console.error(error)
            return
        }
        console.log(`Enviando Payload: ${JSON.stringify(payload, null, 2)}`);

        const endpoint = isAdmin ? `http://api-administrativa:8086/apiAdministrativa/hechos/${hechoId}` : `http://api-publica:8085/apiPublica/hechos/${hechoId}`

        const response = await fetch(endpoint, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + jwtToken
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const errorText = await response.text()
            throw new Error(errorText ? `Error al editar el hecho:\n${errorText}` : "Error al editar el hecho")
        }

        alert("Hecho actualizado con éxito");
        window.location.reload()
    } catch (error) {
        console.error('Error:', error);
        alert('Error al actualizar el hecho');
    } finally {
        ocultarCargando("editar-hecho")
    }
}