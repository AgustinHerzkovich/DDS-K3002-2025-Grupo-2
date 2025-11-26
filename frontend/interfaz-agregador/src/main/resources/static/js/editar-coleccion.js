document.addEventListener('DOMContentLoaded', function () {
    const cambiarAlgoritmoBtn = document.getElementById('btnCambiarAlgoritmo');
    const agregarBtn = document.getElementById('btnAgregarFuente');
    const select = document.getElementById('coleccion-algoritmo-select');


    if(allElementsFound([cambiarAlgoritmoBtn, agregarBtn, select], "editar colección")) {
        listenEliminarFuenteDeColeccion()

        cambiarAlgoritmoBtn.addEventListener('click', () => {
            const coleccionId = cambiarAlgoritmoBtn.dataset.coleccionId;

            if(coleccionId) {
                cambiarAlgoritmo(coleccionId);
            }
        });

        agregarBtn.addEventListener('click', () => {
            const coleccionId = agregarBtn.dataset.coleccionId;

            if (coleccionId) {
                agregarFuente(coleccionId);
            }
        });
    }
});

function cambiarAlgoritmo(coleccionId) {
    if (!coleccionId) {
        console.error('ID de colección inválido');
        return;
    }

    const select = document.getElementById('coleccion-algoritmo-select');
    const nuevoAlgoritmo = select.value;

    fetch(`http://localhost:8086/apiAdministrativa/colecciones/${coleccionId}/algoritmo`, {
        method: 'PATCH',
        headers: getHeaders(),
        body: JSON.stringify({ algoritmoConsenso: nuevoAlgoritmo })
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorText => {throw new Error(errorText || 'Error al cambiar el algoritmo')})
            }

            alert('Algoritmo actualizado exitosamente');
            window.location.reload();
        })
        .catch(error => {
            console.error('Error en cambiarAlgoritmo:', error);
            alert(`Error al cambiar algoritmo: ${error.message}`);
        })
}

function eliminarFuente(coleccionId, fuenteId) {
    if (!coleccionId || !fuenteId) {
        console.error('IDs inválidos');
        return;
    }

    if (!confirm(`¿Estás seguro de eliminar la fuente "${fuenteId}"?`)) return;

    fetch(`http://localhost:8086/apiAdministrativa/colecciones/${coleccionId}/fuentes/${fuenteId}`, {
            method: 'DELETE',
            headers: getHeaders()
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorText => {throw new Error(errorText || 'Error al eliminar la fuente')})
            }

            alert('Fuente eliminada exitosamente');
            window.location.reload();
        })
        .catch(error => {
            console.error('Error en eliminarFuente:', error);
            alert(`Error al eliminar fuente: ${error.message}`);
        })
}

function agregarFuente(coleccionId) {
    if (!coleccionId) {
        console.error('ID de colección inválido');
        return;
    }

    const tipo = document.getElementById('newFuenteTipo')?.value;
    const id = document.getElementById('newFuenteId')?.value?.trim();
    const ip = document.getElementById('newFuenteIp')?.value?.trim();
    const puerto = document.getElementById('newFuentePuerto')?.value?.trim();

    if (!tipo || !id || !ip || !puerto) {
        alert('Todos los campos son obligatorios');
        return;
    }

    const puertoNum = parseInt(puerto, 10);
    if (isNaN(puertoNum) || puertoNum <= 0) {
        alert('El puerto debe ser un número válido mayor a 0');
        return;
    }

    const nuevaFuente = { tipo, id, ip, puerto: puertoNum };

    fetch(`http://localhost:8086/apiAdministrativa/colecciones/${coleccionId}/fuentes`, {
            method: 'POST',
            headers: getHeaders(),
            body: JSON.stringify(nuevaFuente)
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorText => {throw new Error(errorText || 'Error al agregar la fuente')})
            }

            alert('Fuente agregada exitosamente');

            // Limpiar campos
            document.getElementById('newFuenteId').value = '';
            document.getElementById('newFuenteIp').value = '';
            document.getElementById('newFuentePuerto').value = '';

            window.location.reload();
        })
        .catch(error => {
            console.error('Error en agregarFuente:', error);
            alert(`Error al agregar fuente: ${error.message}`);
        })
}