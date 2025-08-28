package aplicacion.services.excepciones;

// Se tira si un contribuyente no existe, o no tiene identidad configurada
// En el futuro me gustaria que la identidad se maneje del lado del cliente y se envie en el post, pero hasta entonces, si no esta guardada tiro este error.
public class ContribuyenteNoConfiguradoException extends RuntimeException {
  public ContribuyenteNoConfiguradoException(String message) {
    super(message);
  }
}
