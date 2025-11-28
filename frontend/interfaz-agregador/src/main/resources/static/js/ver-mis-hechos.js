document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("modal-mis-hechos");
    const openBtn = document.getElementById("menu-mis-hechos");
    const closeBtn = document.getElementById("salir-mis-hechos");
    const autorId = window.autorData.id;
    const listaMisHechos = document.getElementById("lista-mis-hechos")
    const endpoint = isAdmin ? `http://localhost:8085/apiPublica/contribuyentes/${autorId}/hechos` : `http://localhost:8082/fuentesDinamicas/contribuyentes/${autorId}/hechos`;

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
                                    <button id="btn-editar-hecho-${hecho.id}" data-hecho='${JSON.stringify(hecho)}' class="btn-primary btn-sm">
                                        Editar
                                    </button>
                                </div>
                                <p>${hecho.descripcion}</p>
                                <small>Fecha: ${new Date(hecho.fechaAcontecimiento).toLocaleDateString()}</small>
                            </div>`
                    })

                    listaMisHechos.innerHTML = html;
                    listenEditarHechoButtons(hechos)
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

                    listenIrACrearHecho(closeBtn)
                }
                listenModalToggle(modal, openBtn, closeBtn)
            })
            .catch(error => {
                console.error('Error al obtener los hechos:', error);
                alert('Error al cargar los hechos');
            })
    }
});