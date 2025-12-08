function mostrarCargando(btnId) {
    document.getElementById(`nombre-${btnId}`).classList.add("hidden")
    document.getElementById(`spinner-${btnId}`).classList.remove("hidden")
}

function ocultarCargando(btnId) {
    document.getElementById(`nombre-${btnId}`).classList.remove("hidden")
    document.getElementById(`spinner-${btnId}`).classList.add("hidden")
}