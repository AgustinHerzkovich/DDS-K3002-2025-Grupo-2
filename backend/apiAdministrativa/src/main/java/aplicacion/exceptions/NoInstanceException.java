package aplicacion.exceptions;

public class NoInstanceException extends RuntimeException {
    public NoInstanceException(String serviceId) {
        super("No hay instancias de " + serviceId + " registradas");
    }
}
