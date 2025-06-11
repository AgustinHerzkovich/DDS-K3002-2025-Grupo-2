package domain.fuentesDemo;
import java.time.LocalDate;
import java.util.Map;

//CONEXION
public interface Conexion {
    Map<String,Object> siguienteHecho(String url, LocalDate fechaUltimaConsulta);
}