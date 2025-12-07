import {configurarDescargaCSV} from './botonDescargarCSV.js'

document.addEventListener("DOMContentLoaded", () => {
    const listaColecciones = document.getElementById("lista-colecciones");
    const inputBusqueda = document.getElementById("input-busqueda-colecciones");
    const btnAnterior = document.getElementById("btn-anterior");
    const btnSiguiente = document.getElementById("btn-siguiente");
    const spanPagina = document.getElementById("pagina-actual");
    const nombreColeccion = document.getElementById("nombre-coleccion");
    const descripcionColeccion = document.getElementById("desc-coleccion");
    let idColeccion = null;
    let paginaActual = 0;
    const limite = 13;
    let busquedaActual = "";
    let chartProvincias = null;
    let limiteColecciones = 10;

    const sliderLimite = document.getElementById("slider-limite-provincias");
    const valorSlider = document.getElementById("valor-slider");
    const btnActualizar = document.getElementById("btn-actualizar-provincias");
    const slider = document.getElementById("slider-limite-provincias");

    const actualizarSlider = () => {
            const porcentaje = (slider.value - slider.min) / (slider.max - slider.min) * 100;
            slider.style.background = `linear-gradient(to right, #1565C0 0%, #1565C0 ${porcentaje}%, #ccc ${porcentaje}%, #ccc 100%)`;
    }

    sliderLimite.addEventListener("input", () => {
        limiteColecciones = sliderLimite.value;
        valorSlider.textContent = limiteColecciones;
        actualizarSlider()
    });
    actualizarSlider()
    btnActualizar.addEventListener("click", () => {
        cargarDatosColeccion();
    });

    function cargarDatosColeccion() {
        fetch(`http://localhost:8085/apiPublica/provinciasConMasHechosDeColeccion?idColeccion=${encodeURIComponent(idColeccion)}&limit=${limiteColecciones}`)
            .then(resp => resp.json())
            .then(data => {
                if (data.length === 0) {
                    document.getElementById("tarjeta-provincias").style.display = "none";
                    return;
                }

                const labels = data.map(d => d.nombre_provincia);
                const cantidades = data.map(d => d.cantidad_hechos);

                const ctx = document.getElementById("chart-provincias");

                if (chartProvincias) {
                    chartProvincias.destroy();
                }

                chartProvincias = new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels,
                        datasets: [{
                            label: 'Cantidad de hechos',
                            data: cantidades,
                            backgroundColor: '#1565C0'
                        }]
                    },
                    options: {
                        responsive: true,
                        animation: false,
                        plugins: {
                            legend: { display: false }
                        },
                        scales: {
                            y: { beginAtZero: true }
                        }
                    }
                });

                document.getElementById("tarjeta-provincias").style.display = "block";
            })
            .catch(err => console.error("Error cargando datos de provincias:", err));
    }



    function cargarColecciones() {
        const params = new URLSearchParams({
            limit: limite,
            page: paginaActual,
            search: busquedaActual
        });

        fetch(`http://localhost:8085/apiPublica/estadisticas/colecciones?${params.toString()}`)
            .then(resp => resp.json())
            .then(data => {
                listaColecciones.innerHTML = "";

                if (data.content && data.content.length > 0) {
                    // Validar que la página actual no exceda el total de páginas
                    if (paginaActual >= data.totalPages && data.totalPages > 0) {
                        paginaActual = 0;
                        cargarColecciones(); // Volver a cargar con la página corregida
                        return;
                    }

                    data.content.forEach(col => {
                        const li = document.createElement("li");
                        li.textContent = col.tituloColeccion;
                        li.classList.add("coleccion-item");
                        li.addEventListener("click", () => {
                            nombreColeccion.textContent = col.tituloColeccion;
                            idColeccion = col.idColeccion;
                            descripcionColeccion.textContent = col.descripcionColeccion
                            cargarDatosColeccion(col.nombreColeccion);
                        });
                        listaColecciones.appendChild(li);
                    });

                    btnAnterior.disabled = data.first;
                    btnSiguiente.disabled = data.last;
                    spanPagina.textContent = (paginaActual + 1) + " / " + data.totalPages;
                } else {
                    listaColecciones.innerHTML = "<li>No hay colecciones</li>";
                    btnAnterior.disabled = true;
                    btnSiguiente.disabled = true;
                    spanPagina.textContent = "0 / 0";
                }
            })
            .catch(err => {
                console.error("Error cargando colecciones:", err);
                listaCategorias.innerHTML = "<li>Error al cargar colecciones</li>";
            });
    }

    btnAnterior.addEventListener("click", () => {
        if (paginaActual > 0) {
            paginaActual--;
            cargarColecciones();
        }
    });

    btnSiguiente.addEventListener("click", () => {
        paginaActual++;
        cargarColecciones();
    });

    inputBusqueda.addEventListener("input", () => {
        busquedaActual = inputBusqueda.value.trim();
        paginaActual = 0;
        cargarColecciones();
    });

    configurarDescargaCSV("btn-descargar-csv", () => `http://localhost:8085/apiPublica/provinciasConMasHechosDeColeccion?idColeccion=${encodeURIComponent(idColeccion)}&limit=${limiteColecciones}`, () => `estadisticas-${limiteColecciones}_provincias_con_mas_hechos-coleccion_${nombreColeccion.textContent}.csv`)


    cargarColecciones();
});