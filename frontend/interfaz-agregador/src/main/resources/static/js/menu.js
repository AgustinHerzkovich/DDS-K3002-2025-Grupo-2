document.addEventListener('DOMContentLoaded', function () {
    const menu = document.getElementById('dropdown-menu');
    const menuBtn = document.getElementById("menu-button");

    listenModalToggle(menu, menuBtn);
});