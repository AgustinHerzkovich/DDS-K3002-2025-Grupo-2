package aplicacion.excepciones;

public class TooHighLimitException extends RuntimeException {
    public TooHighLimitException(Integer limit) {
        super("El limite" + limit + "es demasiado alto. Prueba uno mas chico");
    }
}
