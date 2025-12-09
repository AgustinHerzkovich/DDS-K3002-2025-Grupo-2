document.addEventListener('DOMContentLoaded', async function () {
    const editarBtns = Array.from(document.querySelectorAll(".coleccion-card .btn-editar-coleccion"));
    const modal = document.getElementById("modal-editar-coleccion");
    const agregarFuenteBtn = document.getElementById("agregar-fuente-editar-coleccion")
    const closeBtn = document.getElementById("salir-editar-coleccion");
    const confirmBtn = document.getElementById("editar-coleccion")

    if(allElementsFound([editarBtns, modal, agregarFuenteBtn, closeBtn, confirmBtn], "editar colecciones")) {
        colecciones.forEach((coleccion, index) => {
            listenOpenModal(modal, editarBtns[index], () => {
                window.coleccionActual = structuredClone(coleccion);
                window.coleccionOriginal = structuredClone(coleccion)

                document.getElementById('algoritmo-editar-coleccion').value = coleccion.tipoAlgoritmoConsenso;
                coleccion.fuentes.forEach(fuente => cargarFuenteColeccion(fuente))

                const containerFuenteCreada = crearContainerFuenteColeccion(0);
                document.getElementById("fuente-nueva-container-editar-coleccion").appendChild(containerFuenteCreada)
                document.getElementById('eliminar-fuente-0').remove();
                listenCambiosTipoFuenteColeccion(0)
            });
        });
        listenCloseModal(modal, closeBtn, () => limpiarModalEditarColeccion(agregarFuenteBtn));
        listenFuentesActualesColeccion()

        confirmBtn.addEventListener('click', async function() {
            await actualizarColeccion(window.coleccionOriginal);
        });
    }
});

async function actualizarColeccion(coleccion) {
    try {
        mostrarCargando("editar-coleccion");
        let seHicieronCambios = false;

        const algoritmoConsenso = document.getElementById("algoritmo-editar-coleccion").value
        if(algoritmoConsenso !== coleccion.tipoAlgoritmoConsenso) {
            const response = await fetch(`http://api-administrativa:8086/apiAdministrativa/colecciones/${coleccion.id}/algoritmo`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + jwtToken
                },
                body: JSON.stringify({ algoritmoConsenso })
            });

            if (!response.ok) {
                throw new Error('Error al actualizar el algoritmo de consenso');
            }

            seHicieronCambios = true
        }

        const fuentesAEliminar = coleccion.fuentes.filter(fuente => !window.coleccionActual.fuentes.some(fuenteActual => fuenteActual.id === fuente.id))
        for (const fuenteAEliminar of fuentesAEliminar) {
            const response = await fetch(`http://api-administrativa:8086/apiAdministrativa/colecciones/${coleccion.id}/fuentes/${fuenteAEliminar.id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': 'Bearer ' + jwtToken
                }
            });

            if (!response.ok) {
                throw new Error(`Error al quitar la fuente ${fuenteAEliminar.alias}`);
            }

            seHicieronCambios = true
        }

        const fuentesNuevas = window.coleccionActual.fuentes.filter(fuenteActual => !coleccion.fuentes.some(fuente => fuente.id === fuenteActual.id))
        for (const fuenteNueva of fuentesNuevas) {
            const response = await fetch(`http://api-administrativa:8086/apiAdministrativa/colecciones/${coleccion.id}/fuentes`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + jwtToken
                },
                body: JSON.stringify({ tipo: fuenteNueva.tipo, id: fuenteNueva.id })
            });

            if (!response.ok) {
                throw new Error(`Error al agregar la fuente ${fuenteNueva.alias}`);
            }

            seHicieronCambios = true
        }

        if(seHicieronCambios) {
            alert("Colección actualizada con éxito");
            window.location.reload();
        } else {
            alert("No se ha realizado ningún cambio");
        }
    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        ocultarCargando("editar-coleccion");
    }
}