document.addEventListener("DOMContentLoaded", function() {
    const openBtns = document.getElementsByClassName("btn-editar-hecho")

    for (let i = 0; i < openBtns.length; i++) {
        const hecho = openBtns[i].dataset.hecho;
        console.log(`ID Hecho ${i+1}: ${hecho.id}`)

        const editarBtn = document.getElementById(`btn-editar-hecho-${hecho.id}`)
        editarBtn.addEventListener("click", () => editarHecho);
    }
    listenEditarHecho(openBtn, )
}

onclick="editarHecho(${JSON.stringify(hecho).replace(/"/g, '&quot;')})"

async function guardarEdicion(hechoId) {
    // Obtener todos los valores del formulario igual que en publicarHecho
    const titulo = document.getElementById('titulo').value;
    const descripcion = document.getElementById('descripcion').value;
    const categoria = document.getElementById('categoria').value;
    const contenidoTexto = document.getElementById('contenido-texto').value;
    const fechaInput = document.getElementById('fecha').value;
    const anonimato = document.getElementById('anonimato').checked;
    const urlsMultimedia = recopilarMultimedias();

    let ubicacion = {};
    if (document.getElementById('btn-usar-coordenadas').checked) {
        ubicacion = {
            latitud: parseFloat(document.getElementById('latitud').value),
            longitud: parseFloat(document.getElementById('longitud').value)
        };
    }

    const hechoEditado = {
        titulo,
        descripcion,
        categoria: { nombre: categoria },
        ubicacion,
        fechaAcontecimiento: fechaInput + ':00',
        contenidoTexto,
        contenidoMultimedia: urlsMultimedia,
        anonimato
    };


    try {
        const response = await fetch(`http://localhost:8082/fuentesDinamicas/hechos/${hechoId}`, {
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
