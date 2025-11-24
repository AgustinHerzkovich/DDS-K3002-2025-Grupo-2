document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById("modal-editar-perfil")
    const openModalBtn = document.getElementById("menu-editar-perfil")
    const cancelarBtn = document.getElementById("salir-editar-perfil")
    const guardarBtn = document.getElementById("editar-perfil")

    obtenerDatosPerfil()

    listenModalToggle(modal, openModalBtn, cancelarBtn, obtenerDatosPerfil)
    guardarBtn.addEventListener("click", guardarPerfil)
});

function obtenerDatosPerfil() {
    const data = window.autorData || {}
    document.getElementById('perfil-nombre').value = data.nombre || '';
    document.getElementById('perfil-apellido').value = data.apellido || '';
    document.getElementById('perfil-email').value = data.email || '';

    const fechaNacimiento = data.fechaNacimiento;
    document.getElementById('perfil-fecha-nacimiento').value = fechaNacimiento || '';
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
}