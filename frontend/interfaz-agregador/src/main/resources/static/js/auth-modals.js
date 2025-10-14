// Script para manejar los modales de autenticación (registro y recuperación de contraseña)

// ========== MODAL DE RECUPERAR CONTRASEÑA (PASO 1) ==========
function openForgotPasswordModal() {
    const modal = document.getElementById('forgotPasswordModal');
    if (modal) {
        modal.classList.remove('hidden');
        document.body.style.overflow = 'hidden';
    }
}

function closeForgotPasswordModal() {
    const modal = document.getElementById('forgotPasswordModal');
    if (modal) {
        modal.classList.add('hidden');
        document.body.style.overflow = 'auto';
    }
}

// ========== MODAL DE RESETEAR CONTRASEÑA (PASO 2) ==========
function openResetPasswordModal() {
    const modal = document.getElementById('resetPasswordModal');
    if (modal) {
        modal.classList.remove('hidden');
        document.body.style.overflow = 'hidden';
    }
}

function closeResetPasswordModal() {
    const modal = document.getElementById('resetPasswordModal');
    if (modal) {
        modal.classList.add('hidden');
        document.body.style.overflow = 'auto';
    }
}

// ========== MODAL DE REGISTRO ==========
function openRegisterModal() {
    const modal = document.getElementById('registerModal');
    if (modal) {
        modal.classList.remove('hidden');
        document.body.style.overflow = 'hidden';
    }
}

function closeRegisterModal() {
    const modal = document.getElementById('registerModal');
    if (modal) {
        modal.classList.add('hidden');
        document.body.style.overflow = 'auto';
    }
}

// ========== VALIDACIONES Y EVENTOS ==========
document.addEventListener('DOMContentLoaded', function() {

    // === FORMULARIO DE RECUPERAR CONTRASEÑA (PASO 1) ===
    const forgotPasswordForm = document.getElementById('forgotPasswordForm');
    if (forgotPasswordForm) {
        forgotPasswordForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const email = document.getElementById('forgot-email').value;

            // Aquí puedes agregar la lógica para enviar el email al backend
            console.log('Solicitando recuperación de contraseña para:', email);

            // Mostrar mensaje de éxito (temporal)
            alert('Se ha enviado un enlace de recuperación a tu correo electrónico');

            // Cerrar modal de recuperación y abrir el de nueva contraseña
            closeForgotPasswordModal();
            openResetPasswordModal();
        });
    }

    // === FORMULARIO DE RESETEAR CONTRASEÑA (PASO 2) ===
    const resetPasswordForm = document.getElementById('resetPasswordForm');
    if (resetPasswordForm) {
        resetPasswordForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const newPassword = document.getElementById('new-password').value;
            const confirmPassword = document.getElementById('confirm-password').value;
            const errorDiv = document.getElementById('password-error');

            // Validar que las contraseñas coincidan
            if (newPassword !== confirmPassword) {
                errorDiv.classList.remove('hidden');
                return;
            }

            errorDiv.classList.add('hidden');

            // Aquí puedes agregar la lógica para enviar la nueva contraseña al backend
            console.log('Cambiando contraseña a:', newPassword);

            // Mostrar mensaje de éxito
            alert('¡Contraseña cambiada exitosamente!');

            // Cerrar modal y volver al login
            closeResetPasswordModal();
            openLoginModal();
        });

        // Validación en tiempo real
        const confirmPasswordInput = document.getElementById('confirm-password');
        if (confirmPasswordInput) {
            confirmPasswordInput.addEventListener('input', function() {
                const newPassword = document.getElementById('new-password').value;
                const confirmPassword = this.value;
                const errorDiv = document.getElementById('password-error');

                if (confirmPassword && newPassword !== confirmPassword) {
                    errorDiv.classList.remove('hidden');
                } else {
                    errorDiv.classList.add('hidden');
                }
            });
        }
    }

    // === FORMULARIO DE REGISTRO ===
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const email = document.getElementById('register-email').value;
            const password = document.getElementById('register-password').value;
            const confirmPassword = document.getElementById('register-confirm-password').value;
            const errorDiv = document.getElementById('register-password-error');

            // Validar que las contraseñas coincidan
            if (password !== confirmPassword) {
                errorDiv.classList.remove('hidden');
                return;
            }

            errorDiv.classList.add('hidden');

            // Recopilar datos del formulario
            const formData = {
                email: email,
                password: password,
                nombre: document.getElementById('register-nombre').value,
                apellido: document.getElementById('register-apellido').value,
                fechaNacimiento: document.getElementById('register-fecha-nacimiento').value
            };

            // Aquí puedes agregar la lógica para enviar los datos al backend
            console.log('Registrando usuario:', formData);

            // Mostrar mensaje de éxito
            alert('¡Cuenta creada exitosamente! Por favor, inicia sesión.');

            // Cerrar modal de registro y abrir el de login
            closeRegisterModal();
            openLoginModal();
        });

        // Validación en tiempo real para registro
        const registerConfirmPasswordInput = document.getElementById('register-confirm-password');
        if (registerConfirmPasswordInput) {
            registerConfirmPasswordInput.addEventListener('input', function() {
                const password = document.getElementById('register-password').value;
                const confirmPassword = this.value;
                const errorDiv = document.getElementById('register-password-error');

                if (confirmPassword && password !== confirmPassword) {
                    errorDiv.classList.remove('hidden');
                } else {
                    errorDiv.classList.add('hidden');
                }
            });
        }
    }
});

// Cerrar modales con tecla ESC
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') {
        closeForgotPasswordModal();
        closeResetPasswordModal();
        closeRegisterModal();
    }
});

