package domain;


import java.util.Map;
import java.time.LocalDateTime;


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