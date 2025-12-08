document.addEventListener("DOMContentLoaded", function() {
    const returnBtn = document.getElementById("btn-return")

    if(allElementsFound([returnBtn], "volver a la página anterior")) {
        returnBtn.addEventListener("click", function () {
            window.history.back()
        });
    }
});