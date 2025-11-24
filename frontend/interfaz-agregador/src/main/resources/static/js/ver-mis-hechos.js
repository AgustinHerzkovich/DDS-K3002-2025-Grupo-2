document.addEventListener('click', function(e) {
    const modal = document.getElementById("misHechosModal")

    if (e.target.closest(".buttonCloseModal")) {
        modal.classList.add('hidden');
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

        const contenido = `
            <div id="misHechosModal" class="modal-page hidden">
                <div class="modal-content modal-page-format max-w-lg">
                    <div class="modal-header">
                        <h5 class="modal-title">Mis Hechos</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="list-group">
                            ${hechos.map(hecho => `
                                <div class="list-group-item">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <h6>${hecho.titulo}</h6>
                                        <button onclick="editarHecho(${JSON.stringify(hecho).replace(/"/g, '&quot;')})" 
                                                class="btn btn-primary btn-sm">
                                            Editar
                                        </button>
                                    </div>
                                    <p>${hecho.descripcion}</p>
                                    <small>Fecha: ${new Date(hecho.fechaAcontecimiento).toLocaleDateString()}</small>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                </div>
            </div>
        `;

        document.body.insertAdjacentHTML('beforeend', contenido);
        const modal = new bootstrap.Modal(document.getElementById('misHechosModal'));
        modal.show();

        document.getElementById('misHechosModal').addEventListener('hidden.bs.modal', function() {
            this.remove();
        });

    } catch (error) {
        console.error('Error al obtener los hechos:', error);
        alert('Error al cargar los hechos');
    }
}