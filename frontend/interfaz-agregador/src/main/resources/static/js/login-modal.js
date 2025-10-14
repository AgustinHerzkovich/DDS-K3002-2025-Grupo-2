// Script para manejar el modal de login

function openLoginModal() {
    const modal = document.getElementById('loginModal');
    if (modal) {
        modal.classList.remove('hidden');
        document.body.style.overflow = 'hidden'; // Prevenir scroll
    }
}

function saveCurrentUrlAndLogin(provider) {
    // Guardar la URL actual (sin el hash ni query params del modal)
    const currentUrl = window.location.pathname;

    // Enviar al servidor para guardar en la sesión
    fetch('/save-redirect-url', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'url=' + encodeURIComponent(currentUrl)
    }).then(() => {
        // Redirigir según el proveedor
        if (provider === 'google') {
            window.location.href = '/oauth2/authorization/keycloak?kc_idp_hint=google';
        } else {
            window.location.href = '/oauth2/authorization/keycloak';
        }
    }).catch(error => {
        console.error('Error guardando URL:', error);
        // Redirigir de todas formas
        if (provider === 'google') {
            window.location.href = '/oauth2/authorization/keycloak?kc_idp_hint=google';
        } else {
            window.location.href = '/oauth2/authorization/keycloak';
        }
    });
}

function closeLoginModal() {
    const modal = document.getElementById('loginModal');
    if (modal) {
        modal.classList.add('hidden');
        document.body.style.overflow = 'auto'; // Restaurar scroll
    }
}

document.addEventListener('DOMContentLoaded', function() {
    // Interceptar clics en enlaces de login para abrir modal
    document.querySelectorAll('a[href="/login"]').forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            openLoginModal();
        });
    });

    // Interceptar el submit del formulario de login
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            saveCurrentUrlAndLogin('keycloak');
        });
    }
});

// Cerrar modal con tecla ESC
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeLoginModal();
    }
});
