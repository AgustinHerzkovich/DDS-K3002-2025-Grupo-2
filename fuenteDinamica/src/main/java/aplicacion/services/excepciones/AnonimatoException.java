package aplicacion.services.excepciones;

public class AnonimatoException extends Exception {
    public AnonimatoException() {
        super("No se puede editar un hecho que fue subido de forma anónima.");
    }
}
