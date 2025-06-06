package domain.fuentesDemo;
import java.time.LocalDateTime;
import java.util.Map;

//CONEXION
public interface Conexion {
    Map<String,Object> siguienteHecho(String url, LocalDateTime fechaUltimaConsulta);
}