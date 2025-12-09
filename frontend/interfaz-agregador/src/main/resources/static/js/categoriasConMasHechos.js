 import { configurarDescargaCSV } from "./botonDescargarCSV.js";

let chart = null
document.addEventListener("DOMContentLoaded", () => {
    const canvas = document.getElementById("grafico-categorias-mas-hechos");
    const slider = document.getElementById("slider-limit");
    const sliderValue = document.getElementById("slider-value");
    const btnActualizar = document.getElementById("btn-actualizar");

    let chart = null;
    let limit = 10;

    const actualizarSlider = () => {
            const porcentaje = (slider.value - slider.min) / (slider.max - slider.min) * 100;
            slider.style.background = `linear-gradient(to right, #1565C0 0%, #1565C0 ${porcentaje}%, #ccc ${porcentaje}%, #ccc 100%)`;
    }

    slider.addEventListener("input", () => {{
        sliderValue.textContent = slider.value;
        limit = slider.value;
        actualizarSlider();
    }});
    actualizarSlider();
    btnActualizar.addEventListener("click", () => {
        cargarGrafico();
    });

    function cargarGrafico() {
        fetch(`http://api-publica:8085/apiPublica/categoriasConMasHechos?page=0&limit=${limit}`)
            .then(resp => resp.json())
            .then(data => {
                if (!data || data.length === 0) {
                    console.error("No hay datos");
                    return;
                }

                const labels = data.map(item => item.nombreCategoria);
                const valores = data.map(item => item.cantidadHechos);
                if (chart) chart.destroy();

                chart = new Chart(canvas, {
                    type: 'bar',
                    data: {
                        labels,
                        datasets: [{
                            label: "Cantidad de hechos",
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
                            x: {
                                ticks: {
                                    font: {
                                        size: 14,
                                        weight: '500'
                                    },
                                    color:'black',
                                    maxRotation: 45,
                                    minRotation: 0,
                                    callback: function(value, index) {
                                        const label = this.getLabelForValue(value);
                                        const maxLength = 25;

                                        if (label.length > maxLength) {
                                            return label.substring(0, maxLength) + '…';
                                        }

                                        return label;
                                    }
                                }
                            },
                            y: {
                                beginAtZero: true,
                                ticks: {
                                    font: { size: 13 }
                                }
                            }
                        },
                        plugins: {
                            tooltip: {
                                callbacks: {
                                    title: function(context) {
                                        return context[0].label;
                                    }
                                }
                            }
                        }
                    }
                });
            })
            .catch(err => {
                console.error("Error cargando categorías con más hechos:", err);
            });
    }

    configurarDescargaCSV("btn-descargar-csv", () => `http://api-publica:8085/apiPublica/categoriasConMasHechos?page=0&limit=${limit}`, () => `estadisticas-${limit}_categorias_con_mas_hechos.csv`)

    actualizarSlider();
    cargarGrafico();
});