document.addEventListener("DOMContentLoaded", function () {
    const modal = document.getElementById("modal-mis-hechos");
    const openBtn = document.getElementById("menu-mis-hechos");
    const closeBtn = document.getElementById("salir-mis-hechos");
    const autorId = window.autorData.id;
    const listaMisHechos = document.getElementById("lista-mis-hechos");
    const paginationControls = document.getElementById("pagination-mis-hechos");
    const btnPrev = document.getElementById("pagination-mis-hechos-prev");
    const btnNext = document.getElementById("pagination-mis-hechos-next");
    const pageInfo = document.getElementById("pagination-mis-hechos-info");

    const baseEndpoint = `http://api-publica:8085/apiPublica/contribuyentes/${autorId}/hechos`;

    let currentPage = 0;
    const pageSize = 10;
    let totalPages = 1;

    if (allElementsFound([modal, openBtn, closeBtn, autorId, listaMisHechos, paginationControls, btnPrev, btnNext, pageInfo], "ver mis hechos")) {

        function cargarHechos(page) {
            const endpoint = `${baseEndpoint}?page=${page}&size=${pageSize}`;

            // Mostrar spinner mientras se cargan los hechos
            listaMisHechos.innerHTML = `
                <style>
                    @keyframes spin-hechos {
                        from { transform: rotate(0deg); }
                        to { transform: rotate(360deg); }
                    }
                </style>
                <div class="flex justify-center items-center py-[3rem]">
                    <div style="width: 55px; height: 55px; border: 6px solid #d1d5db; border-top: 6px solid #0047AB; border-radius: 50%; animation: spin-hechos 0.8s linear infinite;"></div>
                </div>`;
            paginationControls.classList.add("hidden");

            fetch(endpoint, {headers: {'Authorization': 'Bearer ' + jwtToken}})
                .then(response => response.json())
                .then(data => {
                    const content = data.content || data;
                    totalPages = data.totalPages || 1;
                    currentPage = data.number !== undefined ? data.number : page;

                    if (content.length !== 0) {
                        console.log("Hechos cargados:", content);
                        let html = "";
                        content.forEach(hecho => {
                            const actionButton = hecho.visible
                                ? `<a href="/hechos/${hecho.id}" id="btn-editar-hecho-${hecho.id}" data-hecho='${JSON.stringify(hecho)}' class="btn-ver flex gap-2 align-items-center">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="icon w-4 h-4">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="M13.5 6H5.25A2.25 2.25 0 0 0 3 8.25v10.5A2.25 2.25 0 0 0 5.25 21h10.5A2.25 2.25 0 0 0 18 18.75V10.5m-10.5 6L21 3m0 0h-5.25M21 3v5.25" />
                                    </svg>
                                    Ver Hecho
                                </a>`
                                : `<span class="flex gap-2 align-items-center text-gray-500 italic">
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="icon w-4 h-4">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="M18.364 18.364A9 9 0 0 0 5.636 5.636m12.728 12.728A9 9 0 0 1 5.636 5.636m12.728 12.728L5.636 5.636" />
                                    </svg>
                                    Eliminado
                                </span>`;

                            html += `
                                <div class="list-group-item">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <h4 class="texto-clamp font-medium text-gray-900 mb-0.5">${hecho.titulo}</h4>
                                        ${actionButton}
                                    </div>
                                    <p class="texto-clamp">${hecho.descripcion}</p>
                                    <small>Fecha: ${new Date(hecho.fechaAcontecimiento).toLocaleDateString()}</small>
                                </div>`
                        });

                        listaMisHechos.innerHTML = html;

                        // Mostrar controles de paginación si hay más de una página
                        if (totalPages >= 1) {
                            paginationControls.classList.remove("hidden");
                            actualizarControlesPaginacion();
                        } else {
                            paginationControls.classList.add("hidden");
                        }
                    } else if (currentPage === 0) {
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
                        paginationControls.classList.add("hidden");
                        document.getElementById("open-modal-hecho").addEventListener("click", () => {
                            closeBtn.click()
                            document.getElementById("menu-crear-hecho").click()
                        })
                    }
                })
                .catch(error => {
                    console.error('Error al obtener los hechos:', error);
                    listaMisHechos.innerHTML = `
                        <div class="gap-[2rem] flex flex-col items-center py-[2rem]">
                            <div class="flex flex-col items-center gap-[0.5rem]">
                                <h4 class="font-bold text-xl text-red-600">Error al cargar los hechos</h4>
                                <h5 class="text-l">Por favor, intente nuevamente</h5>
                            </div>
                        </div>`;
                    paginationControls.classList.add("hidden");
                });
        }

        function actualizarControlesPaginacion() {
            pageInfo.textContent = `Página ${currentPage + 1} de ${totalPages}`;
            btnPrev.disabled = currentPage === 0;
            btnNext.disabled = currentPage >= totalPages - 1;
        }

        btnPrev.addEventListener("click", () => {
            if (currentPage > 0) {
                cargarHechos(currentPage - 1);
            }
        });

        btnNext.addEventListener("click", () => {
            if (currentPage < totalPages - 1) {
                cargarHechos(currentPage + 1);
            }
        });

        // Cargar primera página al abrir el modal
        listenOpenModal(modal, openBtn, () => {
            document.getElementById("dropdown-menu").classList.add("hidden");
            cargarHechos(0);
        });

        listenCloseModal(modal, closeBtn);
    }
});
