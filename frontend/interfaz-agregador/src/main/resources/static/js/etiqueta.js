document.addEventListener("DOMContentLoaded", function() {
    const agregarBtn = document.getElementById("btn-agregar-etiqueta");
    const eliminarEtiquetaBtns = document.getElementsByClassName("eliminar-etiqueta");

    if(allElementsFound([agregarBtn], "agregar etiqueta")) {
        agregarBtn.addEventListener("click", agregarEtiqueta);

        for (let i = 0; i < eliminarEtiquetaBtns.length; i++) {
            const etiquetaNombre = eliminarEtiquetaBtns[i].dataset.nombre;
            console.log(`Nombre etiqueta ${i+1}: ${etiquetaNombre}`)

            const borrarBtn = document.getElementById(`eliminar-etiqueta-${etiquetaNombre}`)
            borrarBtn.addEventListener("click", () => eliminarEtiqueta(etiquetaNombre));
        }
    }
});

function agregarEtiqueta() {
    const input = document.getElementById("nuevaEtiqueta");
    const nombre = input.value.trim();
    if (!nombre) {
        alert("Ingrese un nombre para la etiqueta");
        return;
    }

    fetch(`http://localhost:8086/apiAdministrativa/hechos/${hechoId}/tags`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(nombre)
    })
        .then(response => {
            if (!response.ok) throw new Error("Error al agregar etiqueta. Puede que este repetida");
            return response.json();
        })
        .then(data => {
            if (data && data.nombre) {
                // Crea el nuevo <li> con su nombre y atributo data-nombre para que se lo pueda eliminar despues
                const ul = document.getElementById("lista-etiquetas");
                const li = document.createElement("li");
                li.id = `eliminar-etiqueta-${data.nombre}`
                li.style.marginBottom = "4px";
                li.dataset.nombre = data.nombre;
                li.className = "tag-item";

                const span = document.createElement("span");
                span.textContent = data.nombre;

                const btnEliminar = document.createElement("button");
                btnEliminar.textContent = "X";
                btnEliminar.className = "btn-eliminar";
                btnEliminar.dataset.nombre = data.nombre;

                li.appendChild(span);
                li.appendChild(btnEliminar);
                ul.appendChild(li);

                btnEliminar.addEventListener("click", () => eliminarEtiqueta(data.nombre));

                input.value = ""; // limpiar input
            } else {
                alert("Error: respuesta inesperada del servidor");
            }
        })
        .catch(err => {
            console.error(err);
            alert("Error al agregar la etiqueta");
        });
}

function eliminarEtiqueta(nombreEtiqueta) {
    if (!confirm(`¿Seguro que quieres eliminar la etiqueta ${nombreEtiqueta}?`))
        return;

    const nombreCodificado = encodeURIComponent(nombreEtiqueta);

    fetch(`http://localhost:8086/apiAdministrativa/hechos/${hechoId}/tags/${nombreCodificado}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                // Buscar el <li> por data-nombre y eliminarlo
                const li = document.querySelector(`li[data-nombre="${nombreEtiqueta.replace(/"/g, '\\"')}"]`);
                if (li) li.remove();
            } else {
                alert("Error al eliminar la etiqueta");
            }
        })
        .catch(err => {
            console.error(err);
            alert("Error al eliminar la etiqueta");
        });
}