function validarFormularioModalHecho() {
    const titulo = document.getElementById('modal-hecho-titulo')
    const categoria = document.getElementById('modal-hecho-categoria')
    const descripcion = document.getElementById('modal-hecho-descripcion')
    const usarCoordenadas = document.getElementById('modal-hecho-usar-coordenadas')
    const pais = document.getElementById('modal-hecho-pais')
    const provincia = document.getElementById('modal-hecho-provincia')
    const ciudad = document.getElementById('modal-hecho-ciudad')
    const calle = document.getElementById('modal-hecho-calle')
    const altura = document.getElementById('modal-hecho-altura')
    const fechaYHora = document.getElementById('modal-hecho-fecha')
    const lat = document.getElementById('modal-hecho-latitud');
    const lon = document.getElementById('modal-hecho-longitud');
    const contenido = document.getElementById('modal-hecho-contenido-texto')
    const anonimato = document.getElementById("modal-hecho-anonimato")
    const inputsMultimedia = Array.from(document.querySelectorAll('#modal-hecho-multimedia-container .multimedia-input'));
    const inputsObligatoriosEstaticos = {
        titulo,
        descripcion,
        categoria,
        pais,
        provincia,
        ciudad,
        calle,
        altura,
        fechaYHora,
        lat,
        lon,
        contenido
    }

    validarInputsObligatorios([...Object.values(inputsObligatoriosEstaticos), ...inputsMultimedia])

    return {
        ...inputsObligatoriosEstaticos,
        inputsMultimedia,
        usarCoordenadas,
        anonimato
    }
}

function limpiarModalHecho(modal, multimediaCountObject) {
    modal.querySelectorAll('input, select, textarea').forEach(campo => {
        campo.classList.remove("form-not-completed");
        campo.classList.add("form-input");
    });
    document.getElementById('form-modal-hecho').reset();
    document.getElementById('modal-hecho-multimedia-container').innerHTML = '';
    multimediaCountObject.multimediaCount = 0;
}