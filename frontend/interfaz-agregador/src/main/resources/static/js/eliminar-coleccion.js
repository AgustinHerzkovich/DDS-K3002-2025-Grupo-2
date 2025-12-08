document.addEventListener('DOMContentLoaded', function () {
    const coleccionesCards = Array.from(document.getElementsByClassName("coleccion-card"));

    if(allElementsFound([coleccionesCards], "eliminar colección")) {
        coleccionesCards.forEach((coleccionCard, index) => {
            const coleccionId = coleccionCard.dataset.id;
            console.log(`ID Colección ${index + 1}: ${coleccionId}`)

            const borrarBtn = document.getElementById(`eliminar-coleccion-${coleccionId}`)
            borrarBtn.addEventListener("click", async function(){await eliminarColeccion(coleccionId)});
        })
    }
});

async function eliminarColeccion(id) {
    const spinnerEliminarColeccion = document.getElementById(`spinner-eliminar-coleccion-${id}`)
    const eliminarBtn = document.getElementById(`eliminar-coleccion-${id}`)

    if (!confirm('¿Está seguro que desea eliminar esta colección?\nEsta acción no se puede deshacer.')) return;

    try {
        eliminarBtn.classList.add("hidden")
        spinnerEliminarColeccion.classList.remove("hidden")

        const response = await fetch(`http://localhost:8086/apiAdministrativa/colecciones/${id}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + jwtToken }
        })

        if (!response.ok) {
            const errorText = await response.text()
            throw new Error(errorText ? `Error al eliminar la colección:\n${errorText}` : "Error al eliminar la colección")
        }

        alert('Colección eliminada correctamente.');
        window.location.reload();
    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        eliminarBtn.classList.remove("hidden")
        spinnerEliminarColeccion.classList.add("hidden")
    }
}