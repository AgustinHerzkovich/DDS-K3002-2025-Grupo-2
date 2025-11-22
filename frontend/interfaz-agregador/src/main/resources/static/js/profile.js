// Función para toggle del menú de perfil
function toggleProfileMenu() {
    const menu = document.getElementById('profileMenu');
    if (menu) {
        menu.classList.toggle('hidden');
    }
}

// Cerrar el menú si se hace clic fuera de él
document.addEventListener('click', function(event) {
    const menu = document.getElementById('profileMenu');
    const button = event.target.closest('#profileButton');

    if (menu && !menu.contains(event.target) && !button) {
        menu.classList.add('hidden');
    }
});

function toggleModalEditarPerfil() {
    const modal = document.getElementById('modalEditarPerfil');
    // Importante: Asume que tienes un div con id="modalEditarPerfil" en tu HTML
    if (!modal) {
        console.error("El modal 'modalEditarPerfil' no fue encontrado en el DOM.");
        return;
    }

    modal.classList.toggle('hidden');

    // Lógica de precarga de datos
    if (!modal.classList.contains('hidden')) {
        const data = window.autorData || {};

        // Los inputs del modal de edición deben tener estos IDs:
        document.getElementById('perfilNombre').value = data.nombre || '';
        document.getElementById('perfilApellido').value = data.apellido || '';
        document.getElementById('perfilEmail').value = data.email || '';

        const fechaNacimiento = data.fechaNacimiento;
        document.getElementById('perfilFechaNacimiento').value = fechaNacimiento || '';
    }
}

async function guardarPerfil() {
    const nombre = document.getElementById('perfilNombre').value.trim();
    const apellido = document.getElementById('perfilApellido').value.trim();
    const fechaNacimiento = document.getElementById('perfilFechaNacimiento').value;

    const payload = {
        nombre: nombre,
        apellido: apellido,
        fechaNacimiento: fechaNacimiento || null
    };

    if (!nombre || !apellido) {
        alert('El nombre y el apellido son obligatorios.');
        return;
    }

    try {
        const resp = await fetch('/editarIdentidad', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (resp.ok) {
            alert('Perfil actualizado correctamente. Se requiere un nuevo inicio de sesión.');

            window.location.href = "/";

            // Las siguientes líneas no son necesarias ya que redirigimos:
            // toggleModalEditarPerfil();
            // window.location.reload();
        } else {
            const text = await resp.text();
            try {
                const errorJson = JSON.parse(text);
                alert('Error al actualizar el perfil: ' + (errorJson.error || resp.status));
            } catch (parseError) {
                alert('Error al actualizar el perfil: ' + (text || resp.status));
            }
        }

    } catch (e) {
        console.error('Error de comunicación:', e);
        alert('Error de comunicación al intentar guardar el perfil. Verifique la conexión del servidor.');
    }
}