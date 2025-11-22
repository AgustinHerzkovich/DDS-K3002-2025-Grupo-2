document.addEventListener("click", e => {
    if (e.target.closest(".btn-eliminar-coleccion")) {
        const id = e.target.closest(".btn-eliminar-coleccion").dataset.id;
        eliminarColeccion(id);
    }
});

function eliminarColeccion(id) {
    if (!id && id !== 0) {
        alert('ID de colección inválido.');
        return;
    }
    if (!confirm('¿Está seguro que desea eliminar esta colección? Esta acción no se puede deshacer.')) return;

    fetch('http://localhost:8086/apiAdministrativa/colecciones/' + id, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' }
    })
        .then(response => {
            if (response.ok) {
                alert('Colección eliminada correctamente.');
                window.location.reload();
            } else {
                return response.text().then(text => { throw new Error('Error ' + response.status + (text ? ' - ' + text : '')); });
            }
        })
        .catch(err => {
            console.error(err);
            alert('Error al eliminar la colección. Verifique la consola.\nDetalle: ' + err.message);
        });
}