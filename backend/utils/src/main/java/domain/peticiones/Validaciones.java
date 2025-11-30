package domain.peticiones;

import domain.excepciones.IdInvalidoException;

import java.util.UUID;

public class Validaciones {
    private Validaciones() {
    }

    public static void validarId(String id) throws IdInvalidoException {
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IdInvalidoException("El ID proporcionado no es válido: " + id);
        }
    }
}
