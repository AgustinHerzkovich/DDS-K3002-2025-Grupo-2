document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById("modal-editar-perfil")
    const openBtn = document.getElementById("menu-editar-perfil")
    const closeBtn = document.getElementById("salir-editar-perfil")
    const guardarBtn = document.getElementById("editar-perfil")

    if(allElementsFound([modal, openBtn, closeBtn, guardarBtn], "editar perfil")) {
        listenOpenModal(modal, openBtn, () => {
            document.getElementById("dropdown-menu").classList.add("hidden")
            obtenerDatosPerfil()
        })
        listenCloseModal(modal, closeBtn)

        guardarBtn.addEventListener("click", guardarPerfil)
    }

});

function obtenerDatosPerfil() {
    document.getElementById('perfil-nombre').value = window.autorData.nombre
    document.getElementById('perfil-apellido').value = window.autorData.apellido
    document.getElementById('perfil-email').value = window.autorData.email
    document.getElementById('perfil-fecha-nacimiento').value = window.autorData.fechaNacimiento
}

function guardarPerfil() {
    const nombre = document.getElementById('perfil-nombre').value.trim();
    const apellido = document.getElementById('perfil-apellido').value.trim();
    const fechaNacimiento = document.getElementById('perfil-fecha-nacimiento').value;

    const payload = {
        nombre: nombre,
        apellido: apellido,
        fechaNacimiento: fechaNacimiento || null
    };

    if (!nombre || !apellido) {
        alert('El nombre y el apellido son obligatorios.');
        return;
    }

    mostrarCargando("editar-perfil");

    fetch('/editarIdentidad', {
        method: 'POST',
        headers:  getHeaders(),
        body: JSON.stringify(payload)
    })
        .then(resp => {
            if (resp.ok) {
                alert('Perfil actualizado correctamente. Se requiere un nuevo inicio de sesión.');
                window.location.href = "/";
            } else {
                resp.text()
                    .then(text => {
                        const errorJson = JSON.parse(text);
                        alert('Error al actualizar el perfil: ' + (errorJson.error || resp.status))
                    });
            }
        })
        .catch(err => {
            console.error('Error de comunicación:', err);
            alert('Error de comunicación al intentar guardar el perfil. Verifique la conexión del servidor.');
        })
        .finally(() => {
            ocultarCargando("editar-perfil");
        })
}