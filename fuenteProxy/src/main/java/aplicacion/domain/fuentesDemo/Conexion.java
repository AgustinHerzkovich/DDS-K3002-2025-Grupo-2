package aplicacion.domain.fuentesDemo;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;
import java.util.Map;

//CONEXION
@Embeddable
public interface Conexion {
    Map<String,Object> siguienteHecho(String url, LocalDateTime fechaUltimaConsulta);
}