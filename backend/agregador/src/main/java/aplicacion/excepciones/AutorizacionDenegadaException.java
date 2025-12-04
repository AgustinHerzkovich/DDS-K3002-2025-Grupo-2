package aplicacion.excepciones;

public class AutorizacionDenegadaException extends RuntimeException {
    public AutorizacionDenegadaException(String mensaje) {
        super(mensaje);
    }
}

