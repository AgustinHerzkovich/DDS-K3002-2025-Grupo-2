package aplicacion.domain.fuentesDemo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;
import java.util.Map;

//CONEXION
@Embeddable
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ConexionPrueba.class, name = "prueba")
}) // Esto es para testear nomas
public interface Conexion {
    Map<String,Object> siguienteHecho(String url, LocalDateTime fechaUltimaConsulta);
}