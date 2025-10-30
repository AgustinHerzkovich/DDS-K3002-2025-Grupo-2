package aplicacion.domain.fuentesProxy.fuentesDemo;
import aplicacion.domain.hechos.Categoria;
import aplicacion.domain.hechos.Origen;
import aplicacion.domain.hechos.Multimedia;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@DiscriminatorValue(value = "prueba")
public class ConexionPrueba extends Conexion {
    private Integer contador = 0;

    @Override
    public Map<String, Object> siguienteHecho(String url, LocalDateTime ultimaConsulta) {
        contador++;
        Categoria categoria1 =new Categoria("Cate 1");
        Categoria categoria2 =new Categoria("Cate 2");
        if (contador == 1) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("titulo", "Primer Hecho");
            datos.put("descripcion", "Esto es el primer hecho de prueba");
            datos.put("categoria",categoria1 ); // ejemplo si tu enum tiene esto
            datos.put("latitud", -34.6037);
            datos.put("longitud", -58.3816);
            datos.put("fecha_acontecimiento", LocalDateTime.now().minusDays(1));
            datos.put("origen", Origen.EXTERNO);
            datos.put("contenido_texto", "Contenido del primer hecho");
            datos.put("contenido_multimedia", new ArrayList<Multimedia>());
            return datos;
        } else if (contador == 2) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("titulo", "Segundo Hecho");
            datos.put("descripcion", "Esto es el segundo hecho de prueba");
            datos.put("categoria", categoria2 );
            datos.put("latitud", -34.60);
            datos.put("longitud", -58.38);
            datos.put("fecha_acontecimiento", LocalDateTime.now().minusHours(3));
            datos.put("origen", Origen.EXTERNO);
            datos.put("contenido_texto", "Contenido del segundo hecho");
            datos.put("contenido_multimedia", new ArrayList<Multimedia>());
            return datos;
        } else {
            return null; // corta el while en FuenteDemo
        }
    }
}


