export function configurarDescargaCSV(botonId, url, nombreArchivo = "archivo.csv") {

    const boton = document.getElementById(botonId);
    if (!boton) {
        console.warn(`No existe el botón con ID: ${botonId}`);
        return;
    }

    boton.addEventListener("click", () => {

        fetch(url, {
            method: "GET",
            headers: {
                "Accept": "text/csv"
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("No se pudo descargar el CSV.");
                }
                return response.blob();
            })
            .then(blob => {
                const urlBlob = window.URL.createObjectURL(blob);

                const a = document.createElement("a");
                a.href = urlBlob;
                a.download = nombreArchivo;

                document.body.appendChild(a);
                a.click();
                a.remove();

                window.URL.revokeObjectURL(urlBlob);
            })
            .catch(err => {
                console.error("Error descargando CSV:", err);
                alert("Hubo un error al descargar el archivo.");
            });

    });
}