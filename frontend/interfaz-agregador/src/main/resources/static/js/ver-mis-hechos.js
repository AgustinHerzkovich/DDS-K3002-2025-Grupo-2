document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("modal-mis-hechos");
    const openBtn = document.getElementById("menu-mis-hechos");
    const closeBtn = document.getElementById("salir-mis-hechos");
    const autorId = window.autorData.id;
    const listaMisHechos = document.getElementById("lista-mis-hechos")
    const endpoint = isAdmin ? `http://localhost:8086/apiAdministrativa/contribuyentes/${autorId}/hechos` : `http://localhost:8085/apiPublica/contribuyentes/${autorId}/hechos`;

    if (allElementsFound([modal, openBtn, closeBtn, autorId, listaMisHechos], "ver mis hechos")) {
        fetch(endpoint)
            .then(response => response.json())
            .then(hechos => {
                if (hechos.length !== 0) {
                    let html = "";
                    hechos.forEach(hecho => {
                        html += `
                            <div class="list-group-item">
                                <div class="d-flex justify-content-between align-items-center">
                                    <h6>${hecho.titulo}</h6>
                                    <a href="/hechos/${hecho.id}" id="btn-editar-hecho-${hecho.id}" data-hecho='${JSON.stringify(hecho)}' class="btn-ver flex gap-2 align-items-center">
                                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="icon w-4 h-4">
                                            <path stroke-linecap="round" stroke-linejoin="round" d="M13.5 6H5.25A2.25 2.25 0 0 0 3 8.25v10.5A2.25 2.25 0 0 0 5.25 21h10.5A2.25 2.25 0 0 0 18 18.75V10.5m-10.5 6L21 3m0 0h-5.25M21 3v5.25" />
                                        </svg>
                                        Ver Hecho
                                    </a>
                                </div>
                                <p>${hecho.descripcion}</p>
                                <small>Fecha: ${new Date(hecho.fechaAcontecimiento).toLocaleDateString()}</small>
                            </div>`
                    })

                    listaMisHechos.innerHTML = html;
                    //listenEditarHechoButtons(hechos)
                } else {
                    listaMisHechos.innerHTML = `
                        <div class="gap-[2rem] flex flex-col items-center py-[2rem]">
                            <div class="flex flex-col items-center gap-[0.5rem]">
                                <h4 class="font-bold text-xl">Todavía no tiene hechos creados</h4>
                                <h5 class="text-l">Pruebe publicar uno</h5>
                            </div>
                            <button id="open-modal-hecho" class="btn-tertiary flex gap-2">
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
                                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15"/>
                                </svg>
                                <span>Crear Hecho</span>
                            </button>
                        </div>`;
                }
                listenOpenModal(modal, openBtn, () => document.getElementById("dropdown-menu").classList.add("hidden"))
                listenCloseModal(modal, closeBtn)
            })
            .catch(error => {
                console.error('Error al obtener los hechos:', error);
                //alert('Error al cargar los hechos');
            })
    }
});
