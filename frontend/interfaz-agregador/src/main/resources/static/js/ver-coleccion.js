document.addEventListener('DOMContentLoaded', function () {
    const verBtns = Array.from(document.querySelectorAll(".coleccion-card .btn-ver-coleccion"));
    const modal = document.getElementById("modal-ver-coleccion");
    const closeBtn = document.getElementById("salir-ver-coleccion")

    if(allElementsFound([verBtns, modal], "ver colección")) {
        colecciones.forEach((coleccion, index) => {
            listenOpenModal(modal, verBtns[index], () => {
                document.getElementById(`sin-criterios-ver-coleccion`).classList.remove("hidden")
                document.getElementById(`sin-fuentes-ver-coleccion`).classList.remove("hidden")

                autocompletarModalVerColeccion(coleccion)
            })
        })
        listenCloseModal(modal, closeBtn, () => limpiarModalColeccion(modal, 'ver-coleccion'))
    }
});

function autocompletarModalVerColeccion(coleccion) {
    document.getElementById('titulo-ver-coleccion').textContent  = coleccion.titulo
    document.getElementById('descripcion-ver-coleccion').textContent  = coleccion.descripcion
    document.getElementById('algoritmo-ver-coleccion').textContent  = coleccion.tipoAlgoritmoConsenso;

    autocompletarCriteriosColeccion(coleccion);

    autocompletarFuentesColeccion(coleccion);
}