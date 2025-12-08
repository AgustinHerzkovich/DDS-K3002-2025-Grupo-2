document.addEventListener('DOMContentLoaded', function () {
    const menu = document.getElementById('dropdown-menu');
    const openBtn = document.getElementById("menu-button");

    if(allElementsFound([menu, openBtn], "abrir menu")) {
        openBtn.addEventListener("click", function(e) {
            e.stopPropagation();
            menu.classList.toggle("hidden");
        })

        document.addEventListener("click", function(e) {
            if(!menu.contains(e.target)) {
                menu.classList.add("hidden");
            }
        })
    }
});