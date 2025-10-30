package aplicacion.excepciones;

import aplicacion.domain.hechos.Hecho;

import java.util.Optional;

public class CategoriaYaPresenteException extends RuntimeException {
    public CategoriaYaPresenteException(Hecho hecho, String etiquetaName) {
        super("El hecho" + hecho + "ya contiene el tag" + etiquetaName);
    }
}
