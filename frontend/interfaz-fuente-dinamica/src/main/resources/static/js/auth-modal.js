// Script para manejar el modal de login, registro y recuperación de contraseña

// ===========================================
// ========== FUNCIONES PRINCIPALES DE MODAL =========
// ===========================================

function openLoginModal() {
    const modal = document.getElementById('loginModal');
    // Si tienes otras modales (registro, reset), asegúrate de cerrarlas aquí:
    // closeRegisterModal();
    // closeForgotPasswordModal();

    if (modal) {
        modal.classList.remove('hidden');
        document.body.style.overflow = 'hidden'; // Prevenir scroll
    }
}

function closeLoginModal() {
    const modal = document.getElementById('loginModal');
    if (modal) {
        modal.classList.add('hidden');
        document.body.style.overflow = 'auto'; // Restaurar scroll
    }
}


// ===========================================
// ========== FUNCIÓN DE LOGIN CON REDIRECCIÓN (TU CÓDIGO CENTRAL) =========
// ===========================================

function saveCurrentUrlAndLogin(provider) {
    // 1. Cerrar el modal antes de redirigir al proveedor
    closeLoginModal();

    // 2. Guardar la URL actual (sin el hash ni query params del modal)
    const currentUrl = window.location.pathname;

    // 3. Enviar al servidor para guardar en la sesión (Keycloak/Spring Security lo usará para devolverte aquí)
    fetch('/save-redirect-url', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        // Aquí debes enviar el token CSRF si estás usando Spring Security
        body: 'url=' + encodeURIComponent(currentUrl) /* + '&_csrf=' + document.querySelector('meta[name="_csrf"]').content */
    }).then(() => {
        // 4. Redirigir según el proveedor
        if (provider === 'google') {
            // Este es el link a Keycloak con el hint para Google
            window.location.href = '/oauth2/authorization/keycloak?kc_idp_hint=google';
        } else {
            // Login estándar o redirección a la página principal de login de Keycloak
            window.location.href = '/oauth2/authorization/keycloak';
        }
    }).catch(error => {
        console.error('Error guardando URL o en redirección:', error);
        // Fallback: Redirigir de todas formas
        if (provider === 'google') {
            window.location.href = '/oauth2/authorization/keycloak?kc_idp_hint=google';
        } else {
            window.location.href = '/oauth2/authorization/keycloak';
        }
    });
}


// ===========================================
// ========== EVENTOS DE LA PÁGINA =========
// ===========================================

document.addEventListener('DOMContentLoaded', function() {

    // 1. Abrir Modal desde el botón del Header (usa 'openLoginModal' directamente en el HTML con onclick)

    // 2. Interceptar el SUBMIT del formulario de login estándar
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            // Llama a la función de redirección con el proveedor general 'keycloak' (sin hint)
            saveCurrentUrlAndLogin('keycloak');
        });
    }

    // 3. Vincular el botón de Google
    // Aunque ya lo pusimos en el HTML con onclick, es buena práctica vincularlo aquí:
    const googleLoginButton = document.getElementById('googleLoginButton');
    if (googleLoginButton) {
        googleLoginButton.addEventListener('click', function() {
            saveCurrentUrlAndLogin('google');
        });
    }

    // 4. Cierre de modal al hacer click en la 'X' (Si no usas onclick en HTML)
    const closeBtn = document.querySelector('#loginModal .close-button');
    if (closeBtn) {
        closeBtn.addEventListener('click', closeLoginModal);
    }

    // 5. Cierre de modal al hacer click fuera
    const loginModal = document.getElementById('loginModal');
    if (loginModal) {
        loginModal.addEventListener('click', function(event) {
            if (event.target === loginModal) {
                closeLoginModal();
            }
        });
    }

    // (Añadir aquí la lógica de navegación entre modales si implementas Registro y Recuperación)

});


// 6. Cerrar modal con tecla ESC
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeLoginModal();
        // Si tienes más modales, añádelas aquí:
        // closeRegisterModal();
        // closeForgotPasswordModal();
    }
});