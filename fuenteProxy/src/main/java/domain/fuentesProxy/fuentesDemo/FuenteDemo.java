package domain.fuentesProxy.fuentesDemo;

import domain.fuentesProxy.FuenteProxy;
import domain.hechos.Hecho;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// FUENTE DEMO
public class FuenteDemo extends FuenteProxy {
    private LocalDateTime ultimaConsulta;
    private Conexion biblioteca;
    private List<Hecho> hechos;
    private String url;

    public FuenteDemo(Long id, Conexion biblioteca, String url) {
        super(id);
        this.ultimaConsulta = LocalDateTime.now();
        this.biblioteca = biblioteca;
        this.url = url;
    }
    @Override
    public void pedirHechos() {
        // todo: basicamente pide hechos hasta que el map que llega esta vacio. Es la logica de negocio que indica el enunciado y es lo que hay que seguir

        // delegar peticion de hechos a la biblioteca

        HechoBuilder hechoBuilder = new HechoBuilder();
        Hecho hecho = hechoBuilder.construirHecho(biblioteca.siguienteHecho(url, ultimaConsulta));
        while (hecho != null){
            Map<String, Object> datos = new HashMap<String, Object>();
            datos = biblioteca.siguienteHecho(url, ultimaConsulta);
            hecho = hechoBuilder.construirHecho(datos); //TODO hechobuilder es una clase costruidora de hechos
            // la idea esta en que recibe un map de objetos y strings con lo que son y lo construye.
            // si el map esta en null, devuelve null y se corta la recursividad del while

            hechos.add(hecho);
        }
    }

    @Override
    public List<Hecho> importarHechos() {
        return hechos;
    }
}