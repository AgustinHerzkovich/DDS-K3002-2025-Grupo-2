document.addEventListener('DOMContentLoaded', function () {
    const verBtns = Array.from(document.querySelectorAll(".coleccion-card .btn-ver-coleccion"));
    const modal = document.getElementById("modal-coleccion");
    const modalHeader = document.getElementById("modal-header-ver-coleccion")
    const closeBtn = document.getElementById("salir-ver-coleccion")

    if(allElementsFound([verBtns, modal, modalHeader], "editar hechos")) {
        colecciones.forEach((coleccion, index) => {
            listenOpenModal(modal, verBtns[index], () => {
                verColeccion(coleccion)

                const mensajeSinFuentes = document.getElementById("sin-fuentes-coleccion")
                if(mensajeSinFuentes.classList.contains("hidden")) {
                    mensajeSinFuentes.classList.remove("hidden")
                }

                modal.querySelectorAll("#modal-coleccion input, #modal-coleccion select, #modal-coleccion textarea").forEach(el => {
                    el.disabled = true;
                });

                modal.querySelectorAll("#modal-coleccion .form-obligatory-icon, #modal-coleccion .max-char-span").forEach(el => {
                    el.classList.add("hidden")
                });

                modalHeader.classList.remove("hidden");
                closeBtn.parentElement.classList.remove("hidden");
            })
        })

        listenCloseModal(modal, closeBtn, () => {
            limpiarModalColeccion(modal)

            const mensajeSinFuentes = document.getElementById("sin-fuentes-coleccion")
            if(!mensajeSinFuentes.classList.contains("hidden")) {
                mensajeSinFuentes.classList.add("hidden")
            }

            modal.querySelectorAll("#modal-coleccion input, #modal-coleccion select, #modal-coleccion textarea, #modal-coleccion button").forEach(el => {
                el.disabled = false;
            });

            modal.querySelectorAll("#modal-coleccion .form-obligatory-icon, #modal-coleccion .max-char-span").forEach(el => {
                el.classList.remove("hidden")
            });

            modalHeader.classList.add("hidden");
            closeBtn.parentElement.classList.add("hidden");
        })
    }
});

function verColeccion(coleccion) {
    document.getElementById('titulo-coleccion').value = coleccion.titulo
    document.getElementById('descripcion-coleccion').value = coleccion.descripcion
    document.getElementById('algoritmo-coleccion').value = coleccion.tipoAlgoritmoConsenso;

    coleccion.criteriosDePertenencia.forEach(criterioDePertenencia => {
        document.getElementById('criterio-tipo-coleccion').value = criterioDePertenencia.tipo

        if(criterioDePertenencia.tipo === 'DISTANCIA') {
            document.getElementById('criterio-latitud').value = criterioDePertenencia.ubicacionBase.latitud;
            document.getElementById('criterio-longitud').value = criterioDePertenencia.ubicacionBase.longitud;
            document.getElementById('criterio-distancia-minima').value = criterioDePertenencia.distanciaMinima;
            document.getElementById("campos-distancia-coleccion").classList.remove("hidden");
        } else {
            if(criterioDePertenencia.tipo === 'FECHA') {
                document.getElementById('criterio-fecha-inicial').value = criterioDePertenencia.fechaInicial;
                document.getElementById('criterio-fecha-final').value = criterioDePertenencia.fechaFinal;
                document.getElementById("campos-fecha-coleccion").classList.remove("hidden");
            }
        }
    })

    coleccion.fuentes.forEach((fuente, index) => {
        agregarFuenteColeccion(index)

        document.getElementById(`eliminar-fuente-${index}`).classList.add("hidden")

        const tipoFuente = document.getElementById(`fuente-tipo-${index}`)
        tipoFuente.value = fuente.tipo

        if(fuente.tipo !== "dinamica") {
            const nombreFuente = document.getElementById(`fuente-nombre-${index}`)
            nombreFuente.value = fuente.id
            document.getElementById(`nombre-container-${index}`).classList.remove("hidden")
        }
    })
}