package aplicacion.domain.lectores;

import aplicacion.domain.hechos.Hecho;

import java.io.InputStream;
import java.util.List;

public interface LectorArchivo {
    List<Hecho> leerHechos(InputStream inputStream);
    Boolean soportaExtension(String extension);
}
