package aplicacion.excepciones;

public class IdInvalidoException extends Exception {
    public IdInvalidoException() {
        super("Ingresaste un id invalido");
    }
}