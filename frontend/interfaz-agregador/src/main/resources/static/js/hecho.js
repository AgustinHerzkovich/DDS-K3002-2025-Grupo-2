function validarFormularioModalHecho() {
    const titulo = document.getElementById('modal-hecho-titulo')
    const categoria = document.getElementById('modal-hecho-categoria')
    const descripcion = document.getElementById('modal-hecho-descripcion')
    const usarCoordenadas = document.getElementById('modal-hecho-usar-coordenadas')
    const pais = document.getElementById('modal-hecho-pais')
    const provincia = document.getElementById('modal-hecho-provincia')
    const ciudad = document.getElementById('modal-hecho-ciudad')
    const calle = document.getElementById('modal-hecho-calle')
    const altura = document.getElementById('modal-hecho-altura')
    const fechaYHora = document.getElementById('modal-hecho-fecha')
    const lat = document.getElementById('modal-hecho-latitud');
    const lon = document.getElementById('modal-hecho-longitud');
    const contenido = document.getElementById('modal-hecho-contenido-texto')
    const anonimato = document.getElementById("modal-hecho-anonimato")
    const inputsMultimedia = Array.from(document.querySelectorAll('#modal-hecho-multimedia-container .multimedia-input'));
    const inputsObligatoriosEstaticos = {
        titulo,
        descripcion,
        categoria,
        pais,
        provincia,
        ciudad,
        calle,
        altura,
        fechaYHora,
        lat,
        lon,
        contenido
    }

    validarInputsObligatorios([...Object.values(inputsObligatoriosEstaticos), ...inputsMultimedia])

    return {
        ...inputsObligatoriosEstaticos,
        inputsMultimedia,
        usarCoordenadas,
        anonimato
    }
}

function limpiarModalHecho(modal) {
    modal.querySelectorAll('input, select, textarea').forEach(campo => {
        campo.classList.remove("form-not-completed");
        campo.classList.add("form-input");
    });

    const coordenadasContainer = document.getElementById("modal-hecho-coordenadas-container")
    if(!coordenadasContainer.classList.contains("hidden")) {
        coordenadasContainer.classList.add("hidden")
        document.getElementById("modal-hecho-direccion-container").classList.remove("hidden")
    }

    const sinMultimediaMensaje = document.getElementById("sin-multimedia-hecho")
    if(sinMultimediaMensaje.classList.contains("hidden")) {
        sinMultimediaMensaje.classList.remove("hidden")
    }

    document.getElementById('form-modal-hecho').reset();
    document.getElementById('modal-hecho-multimedia-container').innerHTML = '';
}

function agregarMultimediaModalHecho(numeroMultimedia) {
    const mensajeSinMultimedia = document.getElementById("sin-multimedia-hecho")
    if(!mensajeSinMultimedia.classList.contains("hidden")) {
        mensajeSinMultimedia.classList.add("hidden")
    }

    const multimediaDiv = document.createElement('div');
    multimediaDiv.className = 'flex items-center gap-2 mb-[1rem]';
    multimediaDiv.id = `multimedia-${numeroMultimedia}`;

    multimediaDiv.innerHTML = `
                <input type="text" class="form-input multimedia-input" maxlength="500"
                       id="url-${numeroMultimedia}"
                       placeholder="https://ejemplo.com/imagen.jpg" required>
                <button id="eliminar-multimedia-${numeroMultimedia}" type="button" class="text-red-500 hover:text-red-700 p-1 rounded-full bg-red-100 hover:bg-red-200">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M6 18 18 6M6 6l12 12" />
                    </svg>
                </button>
        `;

    document.getElementById('modal-hecho-multimedia-container').appendChild(multimediaDiv);

    const eliminarMultimediaBtn = document.getElementById(`eliminar-multimedia-${numeroMultimedia}`)
    const element = document.getElementById(`multimedia-${numeroMultimedia}`);
    eliminarMultimediaBtn.addEventListener("click", () => element.remove());
}

async function autocompletarModalHecho(direccionObject) {
    document.getElementById("modal-hecho-anonimato").value = hecho.anonimato
    document.getElementById("modal-hecho-descripcion").value = hecho.descripcion
    document.getElementById('modal-hecho-categoria').value = hecho.categoria.nombre
    document.getElementById('modal-hecho-contenido-texto').value = hecho.contenidoTexto
    document.getElementById('modal-hecho-fecha').value = hecho.fechaAcontecimiento
    document.getElementById('modal-hecho-titulo').value = hecho.titulo
    document.getElementById('modal-hecho-latitud').value = hecho.ubicacion.latitud
    document.getElementById('modal-hecho-longitud').value = hecho.ubicacion.longitud

    document.getElementById('modal-hecho-ciudad').value = direccionObject.ciudad;
    document.getElementById('modal-hecho-calle').value = direccionObject.calle;
    document.getElementById('modal-hecho-provincia').value = direccionObject.provincia;
    document.getElementById('modal-hecho-altura').value = direccionObject.altura;
    document.getElementById('modal-hecho-pais').value = direccionObject.pais;

    hecho.contenidoMultimedia.forEach((url, index) => {
        agregarMultimediaModalHecho(index)

        document.getElementById(`url-${index}`).value = url
    })
}

async function obtenerDireccionEditarHecho() {
    try {
        mostrarCargando("hecho-editar")

        const response = await fetch(
            `https://nominatim.openstreetmap.org/reverse?format=json&addressdetails=1&lat=${hecho.ubicacion.latitud}&lon=${hecho.ubicacion.longitud}`,
            { headers: { "User-Agent": "MetaMapa/1.0" } }
        )

        const data = await response.json()
        console.log("DATA NOMINATIM →", data);
        if (!data || !data.address) {
            throw new Error('No se pudo obtener la ubicación geográfica.');
        }

        return {
            pais: data.address.country ?? "",
            ciudad: data.address.city ?? data.address.town ?? data.address.village ?? data.address.suburb ?? "",
            provincia: data.address.state ?? "",
            calle: data.address.road ?? data.address.pedestrian ?? data.address.highway ?? "",
            altura: data.address.house_number ?? ""
        }

    } catch (error) {
        console.error(error);
        alert(error.message);
    } finally {
        ocultarCargando("hecho-editar")
    }
}