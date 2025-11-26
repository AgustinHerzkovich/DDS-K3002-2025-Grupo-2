document.addEventListener('DOMContentLoaded', function () {
    const menu = document.getElementById('dropdown-menu');
    const menuBtn = document.getElementById("menu-button");

    if(allElementsFound([menu, menuBtn], "abrir menu")) {
        listenModalToggle(menu, menuBtn);
    }
});