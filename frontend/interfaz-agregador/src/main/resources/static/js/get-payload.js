function getPayloadColeccion(inputsObligatorios) {
    let criterioDePertenencia = {}
    const fuentes = [];

    inputsObligatorios.tiposFuentes.forEach((tipo, index) => {
        const nombre = tipo.value === "dinamica" ? "e359660d-9459-4312-9db6-59e4f9c935d4" : inputsObligatorios.nombresFuentes[index].value;

        if ((nombre && (tipo.value === "estatica" || tipo.value === "proxy")) || tipo.value === "dinamica") {
            if(tipo.value) {
                fuentes.push({
                    tipo: tipo.value,
                    id: nombre
                });
            }
        }
    })

    if(inputsObligatorios.tipoCriterio.value === "DISTANCIA") {
        const lat = document.getElementById('criterio-latitud');
        const lon = document.getElementById('criterio-longitud');
        const dist = document.getElementById('criterio-distancia-minima');

        if(lat.value && lon.value && dist.value) {
            criterioDePertenencia = {
                tipo: 'distancia',
                ubicacionBase: { latitud: parseFloat(lat.value), longitud: parseFloat(lon.value) },
                distanciaMinima: parseFloat(dist.value)
            };
        }
    } else {
        if(inputsObligatorios.tipoCriterio.value === "FECHA") {
            const fechaIni = document.getElementById('criterio-fecha-inicial');
            const fechaFin = document.getElementById('criterio-fecha-final');

            if (fechaIni.value && fechaFin.value) {
                criterioDePertenencia = {
                    tipo: 'fecha',
                    fechaInicial: fechaIni.value + ":00.000",
                    fechaFinal: fechaFin.value + ":00.000"
                };
            }
        }
    }

    return {
        titulo: inputsObligatorios.titulo.value,
        descripcion: inputsObligatorios.descripcion.value,
        criteriosDePertenencia: [criterioDePertenencia],
        fuentes: fuentes,
        tipoAlgoritmoConsenso: inputsObligatorios.algoritmo.value
    };
}

async function getPayloadCrearHecho(inputsObligatorios) {
    try {
        let ubicacion = {}
        const fechaAcontecimiento = inputsObligatorios.fechaYHora.value + ':00';

        const contenidoMultimedia = inputsObligatorios.inputsMultimedia.map(input => input.value.trim())

        if (inputsObligatorios.usarCoordenadas.checked) {
            ubicacion = { latitud: inputsObligatorios.lat.value, longitud: inputsObligatorios.lon.value };
        } else {
            const pais = inputsObligatorios.pais.value.trim();
            const provincia = inputsObligatorios.provincia.value.trim();
            const ciudad = inputsObligatorios.ciudad.value.trim();
            const calle = inputsObligatorios.calle.value.trim();
            const altura = inputsObligatorios.altura.value.trim();

            const direccionCompleta = `${calle} ${altura}, ${ciudad}, ${provincia}, ${pais}`;

            const response = await fetch(
                `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(direccionCompleta)}`,
                { headers: { "User-Agent": "MetaMapa/1.0" } }
            )

            const data = await response.json()

            if (!Array.isArray(data) || data.length === 0) {
                throw new Error('No se pudo obtener la ubicación geográfica.\nVerifique la dirección ingresada.');
            }

            ubicacion = { latitud: parseFloat(data[0].lat), longitud: parseFloat(data[0].lon) };
        }

        const hecho = {
            titulo: inputsObligatorios.titulo.value.trim(),
            descripcion: inputsObligatorios.descripcion.value.trim(),
            categoria: { nombre: inputsObligatorios.categoria.value.trim() },
            ubicacion,
            fechaAcontecimiento,
            origen: isAdmin ? 'CARGA_MANUAL' : 'CONTRIBUYENTE',
            contenidoTexto: inputsObligatorios.contenido.value.trim(),
            anonimato: inputsObligatorios.anonimato.checked,
            contenidoMultimedia
        };

        if (!inputsObligatorios.anonimato.checked && window.autorData) {
            hecho.autor = {
                id: window.autorData.id,
                esAdministrador: isAdmin,
                identidad: {
                    nombre: window.autorData.nombre,
                    apellido: window.autorData.apellido,
                    fechaNacimiento: window.autorData.fechaNacimiento
                },
                mail: window.autorData.email
            }
        }

        return hecho
    } catch (error) {
        console.error(error);
        alert(error.message);
    }
}