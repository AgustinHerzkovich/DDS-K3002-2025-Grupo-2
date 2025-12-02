let chart = null
document.addEventListener("DOMContentLoaded", () => {
    const canvas = document.getElementById("grafico-categorias-mas-hechos");
    const slider = document.getElementById("slider-limit");
    const sliderValue = document.getElementById("slider-value");
    const btnActualizar = document.getElementById("btn-actualizar");

    let chart = null;
    let limit = 10;

    function actualizarSlider() {
        sliderValue.textContent = slider.value;
        limit = slider.value;
    }

    slider.addEventListener("input", actualizarSlider);

    btnActualizar.addEventListener("click", () => {
        cargarGrafico();
    });

    function cargarGrafico() {
        fetch(`http://localhost:8085/apiPublica/categoriasConMasHechos?page=0&limit=${limit}`)
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
                            backgroundColor: '#42A5F5',
                            borderColor: '#1E88E5',
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

    actualizarSlider();
    cargarGrafico();
});