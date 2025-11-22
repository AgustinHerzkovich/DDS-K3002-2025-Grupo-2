function toggleHamburgerMenu() {
    const menu = document.getElementById('hamburgerMenu');
    menu.classList.toggle('hidden');
}

// Cerrar el menú al hacer click fuera
document.addEventListener('click', function(event) {
    const menu = document.getElementById('hamburgerMenu');
    const btnOpen = document.getElementById("hamburgerButton")

    if (!menu.contains(event.target) && !btnOpen.contains(event.target)) {
        menu.classList.add('hidden');
    }
});