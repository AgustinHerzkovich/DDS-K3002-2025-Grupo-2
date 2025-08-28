package aplicacion.domain.fuentesDemo;

import aplicacion.domain.FuenteProxy;
import domain.hechos.Hecho;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// FUENTE DEMO
@Entity
@NoArgsConstructor
public class FuenteDemo extends FuenteProxy {
    private LocalDateTime ultimaConsulta;
    @Embedded
    private Conexion biblioteca;
    @ManyToMany
    private List<Hecho> hechos;
    private String url;

    public FuenteDemo(Conexion biblioteca, String url) {
        this.ultimaConsulta = LocalDateTime.now();
        this.biblioteca = biblioteca;
        this.url = url;
        this.hechos = new ArrayList<>();
    }

    @Override
    public void pedirHechos() {
        // basicamente pide hechos hasta que el map que llega esta vacio. Es la logica de negocio que indica el enunciado y es lo que hay que seguir

        // delegar peticion de hechos a la biblioteca

        HechoBuilder hechoBuilder = new HechoBuilder();
        Hecho hecho = hechoBuilder.construirHecho(biblioteca.siguienteHecho(url, ultimaConsulta));
        while (hecho != null){
            Map<String, Object> datos = new HashMap<String, Object>();
            datos = biblioteca.siguienteHecho(url, ultimaConsulta);
            hecho = hechoBuilder.construirHecho(datos);
            // la idea esta en que recibe un map de objetos y strings con lo que son y lo construye.
            // si el map esta en null, devuelve null y se corta la recursividad del while

            hechos.add(hecho);
        }
    }

    //@Override
    public List<Hecho> importarHechos() {
        return hechos;
    }
}