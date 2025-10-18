package aplicacion.excepciones;

public class MailYaExisteException extends RuntimeException {
    public MailYaExisteException(String message) {
        super(message);
    }
}
