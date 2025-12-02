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
    let limiteProvincias = 10;

    const sliderLimite = document.getElementById("slider-limite-provincias");
    const valorSlider = document.getElementById("valor-slider");
    const btnActualizar = document.getElementById("btn-actualizar-provincias");

    sliderLimite.addEventListener("input", () => {
        limiteProvincias = sliderLimite.value;
        valorSlider.textContent = limiteProvincias;
    });

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
                            backgroundColor: '#42A5F5'
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



        fetch(`http://localhost:8085/apiPublica/horaConMasHechosDeCategoria?nombreCategoria=${encodeURIComponent(nombreCategoria)}`)
            .then(resp => resp.json())
            .then(data => {
                const contenedorHora = document.getElementById("tarjeta-hora");
                const indicadorHora = document.getElementById("hora-mas-hechos");

                if (data.length === 0) {
                    contenedorHora.style.display = "none";
                    return;
                }

                const hora = data[0].horaConMasHechosDeCategoria;
                const cantidad = data[0].cantidadDeHechos;

                indicadorHora.textContent = `Hora con más hechos: ${hora.toString().padStart(2,'0')}:00 (${cantidad} hechos)`;
                contenedorHora.style.display = "block";
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

    configurarDescargaCSV("btn-descargar-csv", `http://localhost:8085/apiPublica/provinciasConMasHechosDeCategoria?nombreCategoria=${encodeURIComponent(nombreCategoria)}&limit=${limiteProvincias}`, "estadisticas.csv")
    
    cargarCategorias();
});