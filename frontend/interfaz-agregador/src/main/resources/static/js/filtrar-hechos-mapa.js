document.addEventListener('DOMContentLoaded', function () {
    const hechosToggleBtn = document.getElementById('btn-toggle-hechos');
    const hechosContent = document.getElementById('hechos-content');
    const hechosSeparator = document.getElementById('hechos-separator');
    const hechosChevron = document.getElementById('chevron-icon');

    listenPanelToggle(hechosToggleBtn, hechosContent, hechosSeparator, hechosChevron)

    const filtrosToggleBtn = document.getElementById('btn-toggle-filtros');
    const filtrosContent = document.getElementById('filtros-content');
    const filtrosSeparator = document.getElementById('filtros-separator');
    const filtrosChevron = document.getElementById('chevron-filtros-icon');

    listenPanelToggle(filtrosToggleBtn, filtrosContent, filtrosSeparator, filtrosChevron)
    listenAplicarFilter()
    listenLimpiarFilter(filtrosContent)
});