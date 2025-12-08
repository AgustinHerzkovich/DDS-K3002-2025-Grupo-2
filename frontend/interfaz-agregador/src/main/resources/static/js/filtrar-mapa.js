document.addEventListener('DOMContentLoaded', function () {
    const filtrosToggleBtn = document.getElementById('btn-toggle-filtros');
    const filtrosContent = document.getElementById('filtros-content');
    const filtrosSeparator = document.getElementById('filtros-separator');
    const filtrosChevron = document.getElementById('chevron-filtros-icon');
    const aplicarBtn = document.getElementById('btn-aplicar-filtros');

    if(allElementsFound([filtrosToggleBtn, filtrosContent, filtrosSeparator, filtrosChevron], "filtrar hechos")) {
        listenPanelToggle(filtrosToggleBtn, filtrosContent, filtrosSeparator, filtrosChevron)
        listenLimpiarFiltrosMapa(filtrosContent)
        listenRadioSliderMapa()

        aplicarBtn.addEventListener('click', async function(e) {
            e.preventDefault();
            aplicarFiltros();
        });
    }
});

function aplicarFiltros() {
    const params = new URLSearchParams(window.location.search);

    // Tomar directamente de los inputs
    const acontecimientoDesde = document.getElementById('fechaAcontecimientoDesde').value;
    const acontecimientoHasta = document.getElementById('fechaAcontecimientoHasta').value;
    const reporteDesde = document.getElementById('fechaReporteDesde').value;
    const reporteHasta = document.getElementById('fechaReporteHasta').value;
    const categoria = document.getElementById('categoria').value;
    const latitud = document.getElementById('filtroLatitud')?.value;
    const longitud = document.getElementById('filtroLongitud')?.value;
    const radio = document.getElementById('filtroRadio')?.value;

    // Validaciones
    if (acontecimientoDesde && acontecimientoHasta) {
        if (new Date(acontecimientoDesde) >= new Date(acontecimientoHasta)) {
            alert("La fecha de acontecimiento 'desde' no puede ser mayor o igual que 'hasta'.");
            return;
        }
    }

    if (reporteDesde && reporteHasta) {
        if (new Date(reporteDesde) >= new Date(reporteHasta)) {
            alert("La fecha de reporte 'desde' no puede ser mayor o igual que 'hasta'.");
            return;
        }
    }

    // Actualizar parámetros con los valores correctos
    if (categoria) params.set('categoria', categoria);
    else params.delete('categoria');

    if (acontecimientoDesde) params.set('fechaAcontecimientoDesde', acontecimientoDesde);
    else params.delete('fechaAcontecimientoDesde');

    if (acontecimientoHasta) params.set('fechaAcontecimientoHasta', acontecimientoHasta);
    else params.delete('fechaAcontecimientoHasta');

    if (reporteDesde) params.set('fechaReporteDesde', reporteDesde);
    else params.delete('fechaReporteDesde');

    if (reporteHasta) params.set('fechaReporteHasta', reporteHasta);
    else params.delete('fechaReporteHasta');

    // Enviar latitud, longitud y radio solo si hay coordenadas
    if (latitud && longitud) {
        params.set('latitud', latitud);
        params.set('longitud', longitud);
        // Convertir radio de km a grados (1 grado ≈ 111 km)
        const radioKm = radio || 5;
        const radioGrados = radioKm / 111.0;
        params.set('radio', radioGrados.toString());
    } else {
        params.delete('latitud');
        params.delete('longitud');
        params.delete('radio');
    }

    window.location.href = '/mapa?' + params.toString();
}