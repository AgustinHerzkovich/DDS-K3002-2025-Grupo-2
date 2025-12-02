document.addEventListener("DOMContentLoaded", function() {

})

async function guardarEdicion(hechoId) {
    try {
        const response = await fetch(`http://localhost:8085/apiPublica/hechos/${hechoId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(hechoEditado)
        });

        if (response.ok) {
            alert('Hecho actualizado exitosamente');
            toggleModalCrearHecho();
            location.reload();
        } else {
            throw new Error('Error al actualizar el hecho');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al actualizar el hecho');
    }
}
