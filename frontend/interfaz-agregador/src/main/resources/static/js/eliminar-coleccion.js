document.addEventListener('DOMContentLoaded', function () {
    const coleccionCards = document.getElementsByClassName("coleccion-card");

    if(allElementsFound([coleccionCards], "eliminar colección")) {
        for (let i = 0; i < coleccionCards.length; i++) {
            const coleccionId = coleccionCards[i].dataset.id;
            console.log(`ID Coleccion ${i+1}: ${coleccionId}`)

            const borrarBtn = document.getElementById(`eliminar-coleccion-${coleccionId}`)
            borrarBtn.addEventListener("click", () => eliminarColeccion(coleccionId));
        }
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
        headers: { 'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + jwtToken }
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