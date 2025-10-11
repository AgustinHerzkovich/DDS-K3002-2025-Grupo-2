package aplicacion.domain.hechos;

import aplicacion.domain.usuarios.Contribuyente;

public class RevisionHecho { // TODO: Registrar revisiones de hecho o quitar
    private Contribuyente administrador;
    private Hecho hecho;

    public RevisionHecho(Contribuyente administrador, Hecho hecho) {
        this.administrador = administrador;
        this.hecho = hecho;
    }
}
    /*
    Basicamente, sirve para guardar y registrar revisiones pasadas sobre un hecho. No se aclara si es una por hecho o pueden ser varias
    En un futuro pueden persistirse o las dejamos de usar.
    */