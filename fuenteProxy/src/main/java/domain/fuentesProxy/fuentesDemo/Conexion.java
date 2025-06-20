package domain.fuentesProxy.fuentesDemo;
import java.time.LocalDate;
import java.util.Map;

//CONEXION
public interface Conexion {
    Map<String,Object> siguienteHecho(String url, LocalDate fecha_ultima_consulta);
}