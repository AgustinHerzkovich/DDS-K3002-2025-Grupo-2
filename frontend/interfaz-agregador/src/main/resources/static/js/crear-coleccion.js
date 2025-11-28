document.addEventListener('DOMContentLoaded', function () {
    const modal = document.getElementById("modal-crear-coleccion")
    const openModalBtn = document.getElementById("menu-crear-coleccion")
    const confirmBtn = document.getElementById("crear-coleccion");
    const cancelarBtn = document.getElementById("salir-crear-coleccion");
    const agregarFuenteBtn = document.getElementById("crear-coleccion-agregar-fuente");
    const tipoCriterio = document.getElementById('criterio-tipo');
    const container = document.getElementById('fuentes-container');
    let contadorFuentesObject = {contadorFuentes : 0};

    if(allElementsFound([modal, openModalBtn, confirmBtn, cancelarBtn, agregarFuenteBtn, tipoCriterio, container], "crear colección")) {
        listenModalToggle(modal, openModalBtn, cancelarBtn, () => limpiarModalCrearColeccion(modal, contadorFuentesObject, tipoCriterio, container));
        listenAgregarFuenteCrearColeccion(agregarFuenteBtn, contadorFuentesObject, container);
        listenCamposCriterioCrearColeccion(tipoCriterio);
        listenCamposFuenteCrearColeccion();

        confirmBtn.addEventListener("click", crearColeccion)
    }
});

async function crearColeccion() {
    try {
        mostrarCargando("crear-coleccion");

        const payload = validarCamposCrearColeccion()
        if(!payload) {
            return;
        }

        console.log("Enviando Payload:", JSON.stringify(payload, null, 2));

        const response = await fetch('http://localhost:8086/apiAdministrativa/colecciones', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const errorText = await response.text()
            throw new Error(errorText ? `Error al crear la colección:\n${errorText}` : "Error al crear la colección")
        }

        alert("¡Colección creada con éxito!");
        document.getElementById("salir-crear-coleccion").click();
        window.location.reload()

    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        ocultarCargando("crear-coleccion");
    }
}

function validarCamposCrearColeccion() {
    const camposNoCompletados = []
    const titulo = document.getElementById('titulo-coleccion')
    const descripcion = document.getElementById('descripcion-coleccion')
    const criterioTipoSelect = document.getElementById('criterio-tipo');
    const tiposSelect = Array.from(document.querySelectorAll('#fuentes-container .fuente-item select'));
    const nombres = Array.from(document.querySelectorAll('#fuentes-container .fuente-item input'));
    const algoritmo = document.getElementById('algoritmo-consenso');
    let criterioDePertenencia = {}
    const fuentes = [];
    const camposInput = []

    camposInput.push(titulo, descripcion, algoritmo)

    tiposSelect.forEach((tipo, index) => {
        const nombre = tipo.value === "dinamica" ? "e359660d-9459-4312-9db6-59e4f9c935d4" : nombres[index].value;

        camposInput.push(tipo, nombres[index])

        if ((nombre && (tipo.value === "estatica" || tipo.value === "proxy")) || tipo.value === "dinamica") {
            if(tipo.value) {
                fuentes.push({
                    tipo: tipo.value,
                    id: nombre
                });
            }
        }
    })

    if(criterioTipoSelect.value === "DISTANCIA") {
        const lat = document.getElementById('criterio-latitud');
        const lon = document.getElementById('criterio-longitud');
        const dist = document.getElementById('criterio-distancia-minima');

        camposInput.push(lat, lon, dist)

        if(lat.value && lon.value && dist.value) {
            criterioDePertenencia = {
                tipo: 'distancia',
                ubicacionBase: { latitud: parseFloat(lat.value), longitud: parseFloat(lon.value) },
                distanciaMinima: parseFloat(dist.value)
            };
        }
    } else {
        if(criterioTipoSelect.value === "FECHA") {
            const fechaIni = document.getElementById('criterio-fecha-inicial');
            const fechaFin = document.getElementById('criterio-fecha-final');

            camposInput.push(fechaIni, fechaFin)

            if(fechaIni.value && fechaFin.value) {
                criterioDePertenencia = {
                    tipo: 'fecha',
                    fechaInicial: fechaIni.value + ":00.000",
                    fechaFinal: fechaFin.value + ":00.000"
                };
            }
        } else {
            camposInput.push(criterioTipoSelect)
            camposNoCompletados.push(criterioTipoSelect)
        }
    }

    marcarCampos(camposInput, camposNoCompletados)

    if(camposNoCompletados.length === 0) {
        return {
            titulo: titulo.value,
            descripcion: descripcion.value,
            criterioDePertenencia,
            fuentes,
            tipoAlgoritmoConsenso: algoritmo.value
        };
    }
}