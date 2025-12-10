async function getPayloadColeccion(inputsObligatorios) {
    const criteriosDePertenencia = [];
    const fuentes = [];

    for (const criterioItem of inputsObligatorios.criteriosItems) {
        const criterioId = criterioItem.dataset.id;
        const tipoCriterio = document.getElementById(`criterio-tipo-${criterioId}`);

        if (tipoCriterio.value === "DISTANCIA") {
            const usarCoordenadas = document.getElementById(`criterio-usar-coordenadas-${criterioId}`);
            const dist = document.getElementById(`criterio-distancia-maxima-${criterioId}`);
            let ubicacion = {};

            try {
                if (usarCoordenadas && usarCoordenadas.checked) {
                    // Usar coordenadas manuales
                    const lat = document.getElementById(`criterio-latitud-${criterioId}`);
                    const lon = document.getElementById(`criterio-longitud-${criterioId}`);

                    if (lat.value && lon.value) {
                        const latitud = lat.value;
                        const longitud = lon.value;

                        // Validar que las coordenadas existan
                        const response = await fetch(
                            `https://nominatim.openstreetmap.org/reverse?format=json&addressdetails=1&lat=${latitud}&lon=${longitud}`,
                            { headers: { "User-Agent": "MetaMapa/1.0" } }
                        );

                        const data = await response.json();

                        if (!data || !data.address) {
                            throw new Error(`Las coordenadas ingresadas no son válidas para el criterio ${criterioId}.`);
                        }

                        ubicacion = { latitud: parseFloat(latitud), longitud: parseFloat(longitud) };
                    }
                } else {
                    // Geocodificar dirección
                    const pais = document.getElementById(`criterio-pais-${criterioId}`);
                    const provincia = document.getElementById(`criterio-provincia-${criterioId}`);
                    const ciudad = document.getElementById(`criterio-ciudad-${criterioId}`);

                    if (pais.value && provincia.value && ciudad.value) {
                        const direccionCompleta = `${ciudad.value.trim()}, ${provincia.value.trim()}, ${pais.value.trim()}`;

                        const response = await fetch(
                            `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(direccionCompleta)}`,
                            { headers: { "User-Agent": "MetaMapa/1.0" } }
                        );

                        const data = await response.json();

                        if (!Array.isArray(data) || data.length === 0) {
                            throw new Error(`No se pudo geocodificar la ubicación: "${direccionCompleta}".\nVerifique los datos ingresados.`);
                        }

                        ubicacion = { latitud: parseFloat(data[0].lat), longitud: parseFloat(data[0].lon) };
                    }
                }

                if (ubicacion.latitud && ubicacion.longitud && dist.value) {
                    criteriosDePertenencia.push({
                        tipo: 'distancia',
                        ubicacionBase: ubicacion,
                        distanciaMaxima: parseFloat(dist.value)
                    });
                }
            } catch (error) {
                console.error(error);
                alert(error.message);
                throw error;
            }
        } else if (tipoCriterio.value === "FECHA") {
            const fechaIni = document.getElementById(`criterio-fecha-inicial-${criterioId}`);
            const fechaFin = document.getElementById(`criterio-fecha-final-${criterioId}`);

            if (fechaIni.value && fechaFin.value) {
                criteriosDePertenencia.push({
                    tipo: 'fecha',
                    fechaInicial: fechaIni.value + ":00.000",
                    fechaFinal: fechaFin.value + ":00.000"
                });
            }
        }
    }

    inputsObligatorios.tiposFuentes.forEach((tipo, index) => {
        const selectNombre = inputsObligatorios.nombresFuentes[index];
        let nombre;

        nombre = selectNombre.value;

        if (nombre && (tipo.value === "estatica" || tipo.value === "proxy" || tipo.value === "dinamica")) {
            if(tipo.value) {
                fuentes.push({
                    tipo: tipo.value,
                    id: nombre
                });
            }
        }
    })

    return {
        titulo: inputsObligatorios.titulo.value,
        descripcion: inputsObligatorios.descripcion.value,
        criteriosDePertenencia,
        fuentes,
        tipoAlgoritmoConsenso: inputsObligatorios.algoritmo.value
    };
}

async function getPayloadModalHecho(inputsObligatorios) {
    try {
        let ubicacion = {}

        const fechaAcontecimiento = inputsObligatorios.fechaYHora.value + ':00';

        const contenidoMultimedia = inputsObligatorios.inputsMultimedia.map(input => input.value.trim())

        if (inputsObligatorios.usarCoordenadas.checked) {
            const latitud = inputsObligatorios.lat.value
            const longitud = inputsObligatorios.lon.value

            const response = await fetch(
                `https://nominatim.openstreetmap.org/reverse?format=json&addressdetails=1&lat=${latitud}&lon=${longitud}`,
                { headers: { "User-Agent": "MetaMapa/1.0" } }
            )

            const data = await response.json()

            if (!data || !data.address) {
                throw new Error("No se pudo obtener la ubicación geográfica.\nVerifique la dirección ingresada.");
            }

            ubicacion = { latitud, longitud};
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

            if (!Array.isArray(data) || data.length === 0 || data.error) {
                throw new Error('No se pudo obtener la ubicación geográfica.\nVerifique la dirección ingresada.');
            }

            ubicacion = { latitud: parseFloat(data[0].lat), longitud: parseFloat(data[0].lon) };
        }

        return {
            titulo: inputsObligatorios.titulo.value.trim(),
            descripcion: inputsObligatorios.descripcion.value.trim(),
            categoria: { nombre: inputsObligatorios.categoria.value.trim() },
            ubicacion,
            fechaAcontecimiento,
            origen: isAdmin ? 'CARGA_MANUAL' : 'CONTRIBUYENTE',
            contenidoTexto: inputsObligatorios.contenido.value.trim(),
            contenidoMultimedia
        }
    } catch (error) {
        console.error(error);
        alert(error.message);
        throw error;
    }
}

async function getPayloadCrearFuente(inputsObligatorios) {
    // Esta función ya no se usa, el payload se maneja directamente en publicarFuente
    // Se mantiene por compatibilidad pero no debería llamarse
    console.warn('getPayloadCrearFuente está obsoleta, usar publicarFuente directamente');
    return null;
}
