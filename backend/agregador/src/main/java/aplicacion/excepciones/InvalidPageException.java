package aplicacion.excepciones;

public class InvalidPageException extends RuntimeException {
    public InvalidPageException(Integer page, Integer limit) {
      super(buildMessage(page, limit));
    }

    private static String buildMessage(Integer page, Integer limit) {
      if (page == null || limit == null) {
        return "Los parámetros 'page' y 'limit' son obligatorios.";
      } else if (page < 0 || limit < 0) {
        return "Los parámetros 'page' y 'limit' deben ser mayores o iguales a 0.";
      } else {
        return "Parámetros de paginación inválidos.";
      }
    }

    public static void validate(Integer page, Integer limit){
        if(page == null || limit == null || page < 0 || limit < 0){
          throw  new InvalidPageException(page, limit);
        }
    }
}
