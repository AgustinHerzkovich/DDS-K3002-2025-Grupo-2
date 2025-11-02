package aplicacion.excepciones;

public class FuenteNoEncontradaException extends RuntimeException {
    public FuenteNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
