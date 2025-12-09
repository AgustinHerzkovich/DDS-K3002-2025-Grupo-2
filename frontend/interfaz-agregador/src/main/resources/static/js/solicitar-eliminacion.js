document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("modal-solicitud-eliminacion");
    const openBtn = document.getElementById("solicitar-eliminacion");
    const closeBtn = document.getElementById("salir-solicitar-eliminacion");
    const confirmBtn = document.getElementById("enviar-solicitud-eliminacion");

    if(allElementsFound([modal, openBtn, closeBtn, confirmBtn], "solicitar eliminación de un hecho")) {
        listenOpenModal(modal, openBtn)
        listenCloseModal(modal, closeBtn, () => {
            document.querySelectorAll("#form-solicitud-eliminacion textarea").forEach(input => {
                input.value = ''
                input.classList.add("form-input");
                input.classList.remove("form-not-completed");
            })
        })

        confirmBtn.addEventListener("click", async function() {
            const inputMotivo = document.getElementById('motivo-solicitud-eliminacion');

            validarInputsObligatorios([inputMotivo]);

            if(!inputMotivo.classList.contains("form-not-completed")) {
                console.log("HOLA")
                await enviarSolicitudEliminacion(inputMotivo)
            }
        });
    }
});

async function enviarSolicitudEliminacion(inputMotivo) {
    try {
        mostrarCargando("enviar-solicitud-eliminacion");

        const payload = {
            solicitanteId: autorData.id,
            hechoId: hechoId,
            motivo: inputMotivo.value
        }
        console.log("Enviando Payload:", JSON.stringify(payload, null, 2));

        const response = await fetch('http://api-publica:8085/apiPublica/solicitudes', {
            method: 'POST',
            headers: getHeaders(),
            body: JSON.stringify(payload)
        })

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText ? `Error al solicitar la eliminación del hecho:\n${errorText}` : "Error al solicitar la eliminación del hecho")
        }

        alert('Solicitud de eliminación enviada exitosamente');
        window.location.reload();
    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        ocultarCargando("enviar-solicitud-eliminacion");
    }
}