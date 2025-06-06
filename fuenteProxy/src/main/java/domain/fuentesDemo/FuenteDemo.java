package domain.fuentesDemo;

import java.time.LocalDateTime;
import java.util.Map;


// FUENTE DEMO
public class FuenteDemo extends FuenteProxy {
    private LocalDateTime ultima_consulta;
    private Conexion biblioteca;

    public FuenteDemo(Conexion biblioteca) {
        this.ultima_consulta = LocalDateTime.now();
        this.biblioteca = biblioteca;
    }

    public  Map<String,Object> siguienteHecho(String url, LocalDateTime fecha_ultima_consulta) {
        //TODO
    }
}