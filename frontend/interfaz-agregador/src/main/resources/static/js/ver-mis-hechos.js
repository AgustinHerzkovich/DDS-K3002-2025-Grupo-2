document.addEventListener("DOMContentLoaded", function() {
    const modal = document.getElementById("mis-hechos-modal");
    const openBtn = document.getElementById("menu-mis-hechos");
    const closeBtn = document.getElementById("salir-mis-hechos");
    const autorId = window.autorData.id;
    const endpoint = isAdmin ?
        `http://localhost:8086/apiAdministrativa/contribuyentes/${autorId}/hechos` :
        `http://localhost:8082/fuentesDinamicas/contribuyentes/${autorId}/hechos`;
    const response = await fetch(endpoint);
    const hechos = await response.json();

    const modal = `<th:block th:replace="~{fragments/modal :: modalMisHechos(hechos)}" />`;

    if(allElementsFound([modal, openBtn], "ver mis hechos")) {
        listenModalToggle(modal, openBtn, closeBtn)
    }
});

async function toggleModalMisHechos() {
    try {
        const autorId = window.autorData.id;
        const endpoint = isAdmin ?
            `http://localhost:8086/apiAdministrativa/contribuyentes/${autorId}/hechos` :
            `http://localhost:8082/fuentesDinamicas/contribuyentes/${autorId}/hechos`;
        const response = await fetch(endpoint);
        const hechos = await response.json();

        const modal = `<th:block th:replace="~{fragments/modal :: modalMisHechos(hechos)}" />`;

    } catch (error) {
        console.error('Error al obtener los hechos:', error);
        alert('Error al cargar los hechos');
    }
}