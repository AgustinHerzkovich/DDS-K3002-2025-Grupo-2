const idHecho = document.currentScript.dataset.idHecho;
console.log("ID del hecho:", idHecho);

function eliminarEtiqueta(nombreEtiqueta) {
    if (!confirm(`¿Seguro que quieres eliminar la etiqueta "${nombreEtiqueta}"?`))
        return;

    const nombreCodificado = encodeURIComponent(nombreEtiqueta);

    fetch(`http://localhost:8086/apiAdministrativa/hechos/${idHecho}/tags/${nombreCodificado}`, {
        method: 'DELETE'
    })
    .then(response => {
        if (response.ok) {
            // Buscar el <li> por data-nombre y eliminarlo
            const li = document.querySelector(`li[data-nombre="${CSS.escape(nombreEtiqueta)}"]`);
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

function agregarEtiqueta() {
    const input = document.getElementById("nuevaEtiqueta");
    const nombre = input.value.trim();
    if (!nombre) {
        alert("Ingrese un nombre para la etiqueta");
        return;
    }

    fetch(`http://localhost:8086/apiAdministrativa/hechos/${idHecho}/tags`, {
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
            li.style.marginBottom = "4px";
            li.dataset.nombre = data.nombre;
            li.className = "tag-item";

            const span = document.createElement("span");
            span.textContent = data.nombre;

            const btnEliminar = document.createElement("button");
            btnEliminar.textContent = "X";
            btnEliminar.style.marginLeft = "8px";
            btnEliminar.className = "btn-eliminar";
            btnEliminar.dataset.nombre = data.nombre;
            btnEliminar.onclick = () => eliminarEtiqueta(data.nombre);

            li.appendChild(span);
            li.appendChild(btnEliminar);
            ul.appendChild(li);

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