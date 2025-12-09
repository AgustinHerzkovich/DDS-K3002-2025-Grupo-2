import {configurarDescargaCSV} from "./botonDescargarCSV.js"
document.addEventListener("DOMContentLoaded", () => {

    let limit1 = 10;
    let limit2 = 10;
    let limit3 = 10;
    let limit4 = 10;
    function configurarSlider(sliderId, valorSpanId, callback, maximo, exponente, divisor) {
        const slider = document.getElementById(sliderId);
        const valorSpan = document.getElementById(valorSpanId);

        function actualizar() {
            const valorCuadratico = Math.ceil(Math.pow(slider.value, exponente)/divisor);
            valorSpan.textContent = valorCuadratico;

            const porcentaje = (slider.value - slider.min) / (slider.max - slider.min) * 100;
            slider.style.background = `linear-gradient(to right, #1565C0 0%, #1565C0 ${porcentaje}%, #ccc ${porcentaje}%, #ccc 100%)`;

            if(slider.value == maximo)
                valorSpan.textContent = "todas las"
            if (callback) callback((slider.value == maximo? 2000000000 : valorCuadratico)); // Peak de la programacion
        }

        slider.addEventListener("input", actualizar);
        actualizar();
    }

    // Hechos por provincia
    configurarSlider("slider-hechos-provincias", "valor-slider-hechos-provincias", (valor) => {
        limit1 = valor
    }, 100, 2, 10);

    // Hechos por categoría
    configurarSlider("slider-hechos-categorias", "valor-slider-hechos-categorias", (valor) => {
        limit2=valor
    }, 100, 2, 10);

    // Hechos por provincia y categoría
    configurarSlider("slider-hechos-provincia-categoria", "valor-slider-hechos-provincia-categoria", (valor) => {
        limit3=valor
    }, 100, 2, 10);

    // Hora con más hechos por categoría
    configurarSlider("slider-hora-mas-hechos", "valor-slider-hora-mas-hechos", (valor) => {
        limit4=valor
    }, 25, 1, 1); // 25 para no mostrar "todas" ni usar 2000000000 horas

    configurarDescargaCSV("btn-hechos-provincias", () => `http://api-publica:8085/apiPublica/provinciasConMasHechosDeColeccion?limit=${limit1}`, () => `estadisticas-${limit1 >= 2000000000 ? "todas_las_provincias" : limit1 + "_provincias_con_mas_hechos"}-todas_las_colecciones.csv`);
    configurarDescargaCSV("btn-hechos-categorias", () => `http://api-publica:8085/apiPublica/categoriasConMasHechos?limit=${limit2}`, () => `estadisticas-${limit2 >= 2000000000 ? "todas_las_categorias" : limit2 + "_categorias_con_mas_hechos"}.csv`);
    configurarDescargaCSV("btn-hechos-provincia-categoria", () => `http://api-publica:8085/apiPublica/provinciasConMasHechosDeCategoria?limit=${limit3}`, () => `estadisticas-${limit3 >= 2000000000 ? "todas_las_provincias" : limit3 + "_provincias_con_mas_hechos"}-todas_las_categorias.csv`);
    configurarDescargaCSV("btn-hora-mas-hechos", () => `http://api-publica:8085/apiPublica/horaConMasHechosDeCategoria?limit=${limit4}`, () => `estadisticas-${limit4 >= 24 ? "las_24_horas" : limit4 + "_horas_con_mas_hechos"}-todas_las_categorias.csv`);
    configurarDescargaCSV("btn-solicitudes-spam", "http://api-publica:8085/apiPublica/solicitudesDeEliminacionSpam", "estadisticas-solicitudes_segun_estado.csv");


});