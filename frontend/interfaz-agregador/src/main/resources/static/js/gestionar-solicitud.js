document.addEventListener('DOMContentLoaded', function () {
    solicitudes.forEach(solicitud => {
        if(solicitud.estado.estado.toLowerCase() === 'pendiente') {
            document.getElementById(`aprobar-solicitud-${solicitud.id}`).addEventListener("click", async function() {
                await aprobarSolicitud(solicitud.id);
            })

            document.getElementById(`rechazar-solicitud-${solicitud.id}`).addEventListener("click", async function() {
                await rechazarSolicitud(solicitud.id);
            })
        }

        const verMasBtn = document.getElementById(`ver-mas-${solicitud.id}`);
        const verMenosBtn = document.getElementById(`ver-menos-${solicitud.id}`);
        const motivoSolicitud = document.getElementById(`motivo-${solicitud.id}`)
        verMasBtn.addEventListener("click",  function() {
            motivoSolicitud.classList.remove("texto-corto")
            motivoSolicitud.classList.add("texto-expandido")
            verMenosBtn.classList.remove("hidden")
            verMasBtn.classList.add("hidden")
        })

        verMenosBtn.addEventListener("click",  function() {
            motivoSolicitud.classList.remove("texto-expandido")
            motivoSolicitud.classList.add("texto-corto")
            verMenosBtn.classList.add("hidden")
            verMasBtn.classList.remove("hidden")
        })
    })
})


async function aprobarSolicitud(solicitudId) {
    if (!confirm('¿Estás seguro de que deseas aprobar esta solicitud?')) {
        return;
    }

    try {
        mostrarCargando(`aprobar-solicitud-${solicitudId}`);

        const response = await fetch(apiAdministrativaUrl + `/solicitudes/${solicitudId}/estado`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + jwtToken },
            body: JSON.stringify({ nuevoEstado: 'ACEPTADA', adminId: userId }),
        })

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText ? `Error al aprobar la solicitud:\n${errorText}` : "Error al aprobar la solicitud")
        }

        alert("Se ha aceptado la solicitud correctamente");
        window.location.reload();
    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        ocultarCargando(`aprobar-solicitud-${solicitudId}`);
    }
}

async function rechazarSolicitud(solicitudId) {
    if (!confirm('¿Estás seguro de que deseas rechazar esta solicitud?')) {
        return;
    }

    try {
        mostrarCargando(`rechazar-solicitud-${solicitudId}`);

        const response = await fetch(apiAdministrativaUrl + `/solicitudes/${solicitudId}/estado`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + jwtToken },
            body: JSON.stringify({ nuevoEstado: 'RECHAZADA', adminId: userId }),
        })

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText ? `Error al rechazar la solicitud:\n${errorText}` : "Error al rechazar la solicitud")
        }

        alert("Se ha rechazado la solicitud correctamente");
        window.location.reload();
    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        ocultarCargando(`rechazar-solicitud-${solicitudId}`);
    }
}