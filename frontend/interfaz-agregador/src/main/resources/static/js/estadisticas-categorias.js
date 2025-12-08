import { configurarDescargaCSV } from "./botonDescargarCSV.js";

document.addEventListener("DOMContentLoaded", () => {
    const listaCategorias = document.getElementById("lista-categorias");
    const inputBusqueda = document.getElementById("input-busqueda-categorias");
    const btnAnterior = document.getElementById("btn-anterior");
    const btnSiguiente = document.getElementById("btn-siguiente");
    const spanPagina = document.getElementById("pagina-actual");
    const nombreCategoria = document.getElementById("nombre-categoria");
    let paginaActual = 0;
    const limite = 13;
    let busquedaActual = "";
    let chartProvincias = null;
    let chartHoras = null;
    let limiteProvincias = 10;

    const sliderLimite = document.getElementById("slider-limite-provincias");
    const valorSlider = document.getElementById("valor-slider");
    const btnActualizar = document.getElementById("btn-actualizar-provincias");
    const slider = document.getElementById("slider-limite-provincias");

    const actualizarSlider = () => {
            const porcentaje = (slider.value - slider.min) / (slider.max - slider.min) * 100;
            slider.style.background = `linear-gradient(to right, #1565C0 0%, #1565C0 ${porcentaje}%, #ccc ${porcentaje}%, #ccc 100%)`;
    }

    sliderLimite.addEventListener("input", () => {
        limiteProvincias = sliderLimite.value;
        valorSlider.textContent = limiteProvincias;
        actualizarSlider()
    });
        actualizarSlider()

    btnActualizar.addEventListener("click", () => {
        const categoriaSeleccionada = document.getElementById("nombre-categoria").textContent;
        cargarDatosCategoria(categoriaSeleccionada);
    });

    function cargarDatosCategoria(nombreCategoria) {
        document.getElementById("nombre-categoria").textContent = nombreCategoria;

        fetch(`http://localhost:8085/apiPublica/provinciasConMasHechosDeCategoria?nombreCategoria=${encodeURIComponent(nombreCategoria)}&limit=${limiteProvincias}`)
            .then(resp => resp.json())
            .then(data => {
                if (data.length === 0) {
                    document.getElementById("tarjeta-provincias").style.display = "none";
                    return;
                }

                const labels = data.map(d => d.nombreProvincia);
                const cantidades = data.map(d => d.cantidadHechos);

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



        fetch(`http://localhost:8085/apiPublica/horaConMasHechosDeCategoria?nombreCategoria=${encodeURIComponent(nombreCategoria)}&limit=100`)
            .then(resp => resp.json())
            .then(data => {
                const contenedorHora = document.getElementById("tarjeta-hora-texto");
                const indicadorHora = document.getElementById("hora-mas-hechos");
                const contenedorGrafico = document.getElementById("tarjeta-hora-grafico");
                contenedorGrafico.style.display = "block";
                const canvas = document.getElementById("chart-hora");
                if (data.length === 0) {
                    contenedorHora.style.display = "none";
                    return;
                }

                if (chartHoras) {
                    chartHoras.destroy();
                }

                const horas = Array.from({length: 24}, (_, i) => i); // 0 a 23
                const valores = horas.map(h => {
                    const item = data.find(d => d.horaConMasHechosDeCategoria === h);
                    return item ? item.cantidadDeHechos : 0;
                });

                const hora = data[0].horaConMasHechosDeCategoria;
                const cantidad = data[0].cantidadDeHechos;

                indicadorHora.textContent = `Hora con más hechos: ${hora.toString().padStart(2,'0')}:00 (${cantidad} hechos)`;
                contenedorHora.style.display = "block";

                chartHoras = new Chart(canvas, {
                    type: 'line',
                    data: {
                        labels: horas.map(h => h.toString().padStart(2, '0') + ':00'),
                        datasets: [{
                            label: 'Cantidad de hechos',
                            data: valores,
                            backgroundColor: '#1565C0',
                            borderColor: '#0D47A1',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        animation: false,
                        scales: {
                            y: {
                                beginAtZero: true,
                                title: {
                                    display: true,
                                    text: 'Cantidad de hechos'
                                }
                            },
                            x: {
                                title: {
                                    display: true,
                                    text: 'Hora del día'
                                }
                            }
                        },
                        plugins: {
                            legend: {
                                display: false
                            },
                            tooltip: {
                                callbacks: {
                                    label: function(context) {
                                        return `${context.parsed.y} hechos`;
                                    }
                                }
                            }
                        }
                    }
                });

            })
            .catch(err => console.error("Error cargando datos de hora:", err));
    }



    function cargarCategorias() {
        const params = new URLSearchParams({
            limit: limite,
            page: paginaActual,
            search: busquedaActual
        });

        fetch(`http://localhost:8085/apiPublica/estadisticas/categorias?${params.toString()}`)
            .then(resp => resp.json())
            .then(data => {
                listaCategorias.innerHTML = "";

                if (data.content && data.content.length > 0) {
                    // Validar que la página actual no exceda el total de páginas
                    if (paginaActual >= data.totalPages && data.totalPages > 0) {
                        paginaActual = 0;
                        cargarCategorias(); // Volver a cargar con la página corregida
                        return;
                    }

                    data.content.forEach(cat => {
                        const li = document.createElement("li");
                        li.textContent = cat.categoria;
                        li.classList.add("categoria-item");
                        li.addEventListener("click", () => {
                            nombreCategoria.textContent = cat.categoria;
                            cargarDatosCategoria(cat.categoria);
                        });
                        listaCategorias.appendChild(li);
                    });

                    btnAnterior.disabled = data.first;
                    btnSiguiente.disabled = data.last;
                    spanPagina.textContent = (paginaActual + 1) + " / " + data.totalPages;
                } else {
                    listaCategorias.innerHTML = "<li>No hay categorías</li>";
                    btnAnterior.disabled = true;
                    btnSiguiente.disabled = true;
                    spanPagina.textContent = "0 / 0";
                }
            })
            .catch(err => {
                console.error("Error cargando categorías:", err);
                listaCategorias.innerHTML = "<li>Error al cargar categorías</li>";
            });
    }

    btnAnterior.addEventListener("click", () => {
        if (paginaActual > 0) {
            paginaActual--;
            cargarCategorias();
        }
    });

    btnSiguiente.addEventListener("click", () => {
        paginaActual++;
        cargarCategorias();
    });

    inputBusqueda.addEventListener("input", () => {
        busquedaActual = inputBusqueda.value.trim();
        paginaActual = 0;
        cargarCategorias();
    });
    configurarDescargaCSV("btn-descargar-csv", () => `http://localhost:8085/apiPublica/provinciasConMasHechosDeCategoria?nombreCategoria=${(nombreCategoria.textContent)}&limit=${limiteProvincias}`, () => `estadisticas-${limiteProvincias}_provincias_con_mas_hechos-categoria_${nombreCategoria.textContent}.csv`)
    configurarDescargaCSV("btn-descargar-csv-horas", () => `http://localhost:8085/apiPublica/horaConMasHechosDeCategoria?nombreCategoria=${(nombreCategoria.textContent)}&limit=100`, () => `estadisticas-hechos_por_hora-categoria_${nombreCategoria.textContent}.csv`)

    cargarCategorias();
});