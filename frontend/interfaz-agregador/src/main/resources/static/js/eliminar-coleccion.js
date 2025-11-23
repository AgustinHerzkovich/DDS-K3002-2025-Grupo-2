if (!window.eliminarColeccionListenerAgregado) {
    document.addEventListener("click", e => {
        const btn = e.target.closest(".eliminar-coleccion");
        if (btn) {
            const id = btn.dataset.id;
            eliminarColeccion(id);
        }
    });
    window.eliminarColeccionListenerAgregado = true;
}

function eliminarColeccion(id) {
    if (!id && id !== 0) {
        alert('ID de colección inválido.');
        return;
    }
    if (!confirm('¿Está seguro que desea eliminar esta colección? Esta acción no se puede deshacer.')) return;

    fetch('/apiAdministrativa/colecciones/' + id, {
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