package aplicacion.services.excepciones;

public class HechoNoEncontradoException extends Exception {
    public HechoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
