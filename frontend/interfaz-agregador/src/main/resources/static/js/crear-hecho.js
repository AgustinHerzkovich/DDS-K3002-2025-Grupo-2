document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("modal-hecho");
    const openBtn = document.getElementById("menu-crear-hecho");
    const closeBtn = document.getElementById("salir-crear-hecho");
    const confirmBtn = document.getElementById("crear-hecho")
    const crearHechoTitle = document.getElementById("modal-crear-hecho-titulo")
    const multimediaCountObject = {multimediaCount : 0};

    if(allElementsFound([modal, openBtn, closeBtn, confirmBtn], "crear hecho")) {
        listenOpenModal(modal, openBtn, () => {
            document.getElementById("dropdown-menu").classList.add("hidden")
            crearHechoTitle.classList.remove("hidden")
            confirmBtn.parentElement.classList.remove("hidden")
            closeBtn.parentElement.classList.remove("hidden")
        })
        listenCloseModal(modal, closeBtn, () => {
            limpiarModalHecho(modal, multimediaCountObject)
            crearHechoTitle.classList.add("hidden")
            confirmBtn.parentElement.classList.add("hidden")
            closeBtn.parentElement.classList.add("hidden")
        })
        listenCamposUbicacionCrearHecho()
        listenAgregarMultimediaModalHecho(multimediaCountObject)

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

        const payload = await getPayloadCrearHecho(inputsObligatorios)
        console.log("Enviando Payload:", JSON.stringify(payload, null, 2));

        const endpoint = isAdmin
            ? 'http://localhost:8086/apiAdministrativa/hechos'
            : 'http://localhost:8085/apiPublica/hechos';

        const response = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + jwtToken },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText ? `Error al publicar el hecho:\n${errorText}` : "Error al publicar el hecho")
        }

        alert('Hecho publicado exitosamente');
        document.getElementById("salir-crear-hecho").click();
        window.location.reload();
    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        ocultarCargando("crear-hecho")
    }
}