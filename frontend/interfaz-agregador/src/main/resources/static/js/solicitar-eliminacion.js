document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("modal-solicitud-eliminacion");
    const solicitarBtn = document.getElementById("solicitar-eliminacion");
    const cancelarBtn = document.getElementById("salir-solicitar-eliminacion");
    const enviarBtn = document.getElementById("enviar-solicitud-eliminacion");

    if(allElementsFound([modal, solicitarBtn, cancelarBtn, enviarBtn], "solicitar eliminación de un hecho")) {
        listenModalToggle(modal, solicitarBtn, cancelarBtn, () => {
            document.getElementById('motivo-solicitud-eliminacion').value = ''
        });

        enviarBtn.addEventListener("click", () => enviarSolicitudEliminacion(cancelarBtn));
    }
});

function enviarSolicitudEliminacion(cancelarBtn) {
    const motivo = document.getElementById('motivo-solicitud-eliminacion').value;

    if (!motivo.trim()) {
        alert('Por favor ingrese un motivo');
        return;
    }
    if (!solicitanteId) {
        alert('Debe estar logueado para enviar una solicitud de eliminación');
        return;
    }

    fetch('http://localhost:8085/apiPublica/solicitudes', {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify({
            solicitanteId: solicitanteId,
            hechoId: hechoId,
            motivo: motivo
        })
    })
        .then(response => {
            if (response.ok) {
                alert('Solicitud enviada exitosamente');
                cancelarBtn.click()
            } else {
                return response.text().then(text => {
                    alert('Error al enviar la solicitud: ' + text);
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al enviar la solicitud');
        });
}