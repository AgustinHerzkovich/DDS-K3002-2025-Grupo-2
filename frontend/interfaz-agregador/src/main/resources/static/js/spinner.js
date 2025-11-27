function mostrarCargando(confirmBtnId) {
    document.getElementById(`confirmar-${confirmBtnId}`).classList.add("hidden")
    document.getElementById(`confirmar-spinner-${confirmBtnId}`).classList.remove("hidden")
}

function ocultarCargando(confirmBtnId) {
    document.getElementById(`confirmar-${confirmBtnId}`).classList.remove("hidden")
    document.getElementById(`confirmar-spinner-${confirmBtnId}`).classList.add("hidden")
}