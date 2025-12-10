document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("modal-hecho");
    const openBtn = document.getElementById("menu-crear-hecho");
    const closeBtn = document.getElementById("salir-crear-hecho");
    const confirmBtn = document.getElementById("crear-hecho")
    const modalTitle = document.getElementById("modal-crear-hecho-titulo")
    const agregarBtn = document.getElementById("modal-crear-hecho-agregar-multimedia")

    if(allElementsFound([modal, openBtn, closeBtn, confirmBtn], "crear hecho")) {
        listenOpenModal(modal, openBtn, () => {
            document.getElementById("dropdown-menu").classList.add("hidden")
            modalTitle.classList.remove("hidden")
            agregarBtn.classList.remove("hidden")
            confirmBtn.parentElement.classList.remove("hidden")
            closeBtn.parentElement.classList.remove("hidden")
        })
        listenCloseModal(modal, closeBtn, () => {
            limpiarModalHecho(modal)

            modalTitle.classList.add("hidden")
            agregarBtn.classList.add("hidden")
            confirmBtn.parentElement.classList.add("hidden")
            closeBtn.parentElement.classList.add("hidden")
        })
        listenCamposUbicacionCrearHecho()
        listenAgregarMultimediaModalHecho(agregarBtn)

        confirmBtn.addEventListener('click', async function() {
            const inputsObligatorios = validarFormularioModalHecho()

            if(!document.querySelector('#form-modal-hecho .form-not-completed')) {
                await publicarHecho(inputsObligatorios)
            }
        });
    }
});

async function publicarHecho(inputsObligatorios) {
    try {
        mostrarCargando("crear-hecho")
        let payload;

        try {
            payload = await getPayloadModalHecho(inputsObligatorios)
        } catch (error) {
            console.error(error)
            return
        }

        payload.anonimato = inputsObligatorios.anonimato ? inputsObligatorios.anonimato.checked : true
        if (!payload.anonimato && window.autorData) {
            payload.autor = window.autorData.id
        }

        const endpoint = isAdmin
            ? apiAdministrativaUrl + '/hechos'
            : apiPublicaUrl + 'hechos';

        const headers = {
            'Content-Type': 'application/json',
        }

        if (isAdmin || !payload.anonimato) {
            headers['Authorization'] = 'Bearer ' + jwtToken;
        }

        const response = await fetch(endpoint, {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText ? `Error al publicar el hecho:\n${errorText}` : "Error al publicar el hecho")
        }

        alert('Hecho publicado exitosamente');
        window.location.reload();
    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        ocultarCargando("crear-hecho")
    }
}