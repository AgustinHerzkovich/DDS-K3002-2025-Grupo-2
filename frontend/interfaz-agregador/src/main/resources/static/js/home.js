document.addEventListener("DOMContentLoaded", function() {
    const returnBtn = document.getElementById("btn-return");
    const scrollArrowBtn = document.getElementById("home-scroll-arrow")

    if (returnBtn) {
        returnBtn.remove();
    }

    if(allElementsFound([scrollArrowBtn], "scrollear con flecha")) {
        listenHomeScrollableArrow(scrollArrowBtn)
    }
});