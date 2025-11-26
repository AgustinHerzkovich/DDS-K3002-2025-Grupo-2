document.addEventListener("DOMContentLoaded", function() {
    const openBtns = document.getElementsByClassName("btn-editar-hecho")

    for (let i = 0; i < openBtns.length; i++) {
        const hecho = openBtns[i].dataset.hecho;
        console.log(`ID Hecho ${i+1}: ${hecho.id}`)

        const editarBtn = document.getElementById(`btn-editar-hecho-${hecho.id}`)
        editarBtn.addEventListener("click", () => editarHecho);
    }
}

onclick="editarHecho(${JSON.stringify(hecho).replace(/"/g, '&quot;')})"