package aplicacion.excepciones;

public class ColeccionNoEncontradaException extends RuntimeException {
    public ColeccionNoEncontradaException(String message) {
        super(message);
    }
}
