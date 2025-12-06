// Función modificada para recibir el ID directamente
function openDetalle(id) {
    currentId = id;
    console.log('LOG 1: Llamada a openDetalle con ID:', id);
    // Buscar objeto localmente en el array provisto por el servidor
    const hecho = hechosPendientes.find(h => String(h.id) === String(id));
    if (hecho) {
        console.log('LOG 2: Hecho encontrado:', hecho);
        document.getElementById('modalTitulo').textContent = hecho.titulo || '';
        document.getElementById('modalDescripcion').textContent = hecho.descripcion || '';
        document.getElementById('modalFecha').textContent = hecho.fechaAcontecimiento ? new Date(hecho.fechaAcontecimiento).toLocaleString() : '';
        document.getElementById('modalUbicacion').textContent = hecho.ubicacion ? (hecho.ubicacion.latitud + ', ' + hecho.ubicacion.longitud) : 'N/D';
        console.log('LOG 3: Mostrando modal de detalle.');
        document.getElementById('modalAutor').textContent = hecho.autor.identidad.nombre + ' ' + hecho.autor.identidad.apellido || 'Anónimo';
    } else {
        // LOG 2b: Si no encuentra el objeto (posible problema de tipo de dato del ID)
        console.error('LOG 2b: Error, Hecho no encontrado para ID:', id);
    }
    document.getElementById('sugerencia').value = '';
    document.getElementById('detalleModal').classList.remove('hidden');
}

function closeDetalle() {
    document.getElementById('detalleModal').classList.add('hidden');
    currentId = null;
}

async function gestionar(estado) {
    if (!currentId) return alert('ID inválido.');
    const sugerencia = document.getElementById('sugerencia').value || null;
    const payload = { estado: estado, sugerencia: sugerencia };

    try {
        const aceptarBtn = document.getElementById('aceptarBtn');
        const rechazarBtn = document.getElementById('rechazarBtn');
        aceptarBtn.disabled = true; rechazarBtn.disabled = true;

        const resp = await fetch('/gestionar-solicitud/' + currentId, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + jwtToken },
            body: JSON.stringify(payload)
        });

        if (resp.ok) {
            alert('Operación realizada correctamente.');
            closeDetalle();
            window.location.reload();
        } else {
            const text = await resp.text();
            console.error('Error del servidor:', resp.status, text);
            alert('Error al procesar la solicitud: ' + (text || resp.status));
        }
    } catch (e) {
        console.error(e);
        alert('Error de comunicación: ' + e.message);
    } finally {
        document.getElementById('aceptarBtn').disabled = false;
        document.getElementById('rechazarBtn').disabled = false;
    }
}

document.addEventListener('click', function (e) {
    if (e.target.classList.contains('modal') && !e.target.closest('.modal-content')) {
        closeDetalle();
    }
});


document.addEventListener('DOMContentLoaded', function() {
    console.log('LOG 5: DOMContentLoaded ejecutado. Adjuntando listeners.');
    const detailButtons = document.querySelectorAll('.btn-ver-hecho-pendiente');
    detailButtons.forEach(button => {
        button.addEventListener('click', function() {
            console.log('LOG 6: Botón Ver Detalle clickeado.');
            // Obtenemos el ID del atributo data-id (th:attr="data-id...") y lo pasamos a openDetalle
            const id = this.dataset.id;
            openDetalle(id);
        });
    });
    console.log('LOG 7: Lista de hechos del servidor:', hechosPendientes);
});