package aplicacion.domain.fuentesProxy.fuentesDemo;

import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.hechos.Origen;
import aplicacion.domain.hechos.Ubicacion;
import aplicacion.domain.hechos.multimedias.Multimedia;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

// Es una clase constructora de hechos
@Embeddable
public class HechoBuilder {
    public Hecho construirHecho(Map<String, Object> datos) {
        if(datos.isEmpty()){return null;}
        String titulo = (String) datos.get("titulo");
        String descripcion = (String) datos.get("descripcion");
        Categoria categoria = (Categoria) datos.get("categoria"); // o convertilo si viene como otra cosa
        Double latitud = (Double) datos.get("latitud");
        Double longitud = (Double) datos.get("longitud");
        LocalDateTime fechaAcontecimiento = (LocalDateTime) datos.get("fecha_acontecimiento");
        Origen origen = (Origen) datos.get("origen");
        String contenidoTexto = (String) datos.get("contenido_texto");
        List<Multimedia> contenidoMultimedia = (List<Multimedia>) datos.get("contenido_multimedia");

        return new Hecho(
                titulo,
                descripcion,
                categoria,
                new Ubicacion(latitud, longitud),
                fechaAcontecimiento,
                origen,
                contenidoTexto,
                contenidoMultimedia
        );
    }
}
