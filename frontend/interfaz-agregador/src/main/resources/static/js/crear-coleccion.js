document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById("modal-crear-coleccion")
    const openBtn = document.getElementById("menu-crear-coleccion")
    const agregarCriterioBtn = document.getElementById("agregar-criterio-crear-coleccion")
    const agregarFuenteBtn = document.getElementById("agregar-fuente-crear-coleccion")
    const closeBtn = document.getElementById("salir-crear-coleccion");
    const confirmBtn = document.getElementById("crear-coleccion");

    if(allElementsFound([modal, openBtn, agregarCriterioBtn, agregarFuenteBtn, confirmBtn, closeBtn], "crear colección")) {
        listenOpenModal(modal, openBtn, () => document.getElementById("dropdown-menu").classList.add("hidden"))
        listenCloseModal(modal, closeBtn, () => limpiarModalColeccion(modal, 'crear-coleccion'))
        listenCriteriosColeccion(agregarCriterioBtn, 'crear-coleccion');
        listenFuentesColeccion(agregarFuenteBtn, 'crear-coleccion');

        confirmBtn.addEventListener("click", async function() {
            const inputsObligatorios = validarFormularioModalCrearColeccion()

            if(!document.querySelector('#form-modal-coleccion .form-not-completed') && document.getElementById("sin-criterios-crear-coleccion").classList.contains("hidden")) {
                await crearColeccion(inputsObligatorios)
            }
        })
    }
});

async function crearColeccion(inputsObligatorios) {
    try {
        mostrarCargando("crear-coleccion");

        const payload = getPayloadColeccion(inputsObligatorios)
        console.log("Enviando Payload:", JSON.stringify(payload, null, 2));

        const response = await fetch('http://localhost:8086/apiAdministrativa/colecciones', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization' : 'Bearer ' + jwtToken },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const errorText = await response.text()
            throw new Error(errorText ? `Error al crear la colección:\n${errorText}` : "Error al crear la colección")
        }

        alert("Colección creada con éxito");
        window.location.reload()
    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        ocultarCargando("crear-coleccion");
    }
}