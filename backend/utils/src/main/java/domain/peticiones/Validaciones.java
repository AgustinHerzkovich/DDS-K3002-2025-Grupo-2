package domain.peticiones;

import domain.excepciones.IdInvalidoException;

public class Validaciones {
    private Validaciones() {
    }

    public static void validarId(Long id) throws IdInvalidoException {
        if (id == null || id <= 0) {
            throw new IdInvalidoException("El ID debe ser un número positivo y no nulo.");
        }
    }
}
