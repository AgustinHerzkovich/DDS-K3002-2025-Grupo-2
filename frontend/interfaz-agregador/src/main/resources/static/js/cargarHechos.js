function mostrarConfirmacion() {
    document.getElementById("confirm-overlay").style.display = "flex";
}

function ocultarConfirmacion() {
    document.getElementById("confirm-overlay").style.display = "none";
}

function mostrarSpinner() {
    document.getElementById("spinner-overlay").style.display = "flex";
}

function ocultarSpinner() {
    document.getElementById("spinner-overlay").style.display = "none";
}

function cargarHechos() {
    mostrarConfirmacion();

    const btnYes = document.getElementById("confirm-yes");
    const btnNo = document.getElementById("confirm-no");

    btnYes.onclick = null;
    btnNo.onclick = null;

    btnYes.onclick = function () {
        mostrarSpinner();

        fetch('http://api-administrativa:8086/apiAdministrativa/cargarHechos', {
            method: "POST",
            headers: { 'Authorization': 'Bearer ' + jwtToken },
        })
        .then(response => {
            ocultarSpinner();
            ocultarConfirmacion();
            if (response.ok) {
                alert("Hechos cargados");
            } else {
                alert("Error al cargar los hechos");
            }
        })
        .catch(error => {
            ocultarSpinner();
            console.error("Error:", error);
            alert("No se pudo conectar al servidor");
        });
    };

    btnNo.onclick = ocultarConfirmacion;
}