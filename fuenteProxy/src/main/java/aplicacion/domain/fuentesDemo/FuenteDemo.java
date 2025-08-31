package aplicacion.domain.fuentesDemo;

import aplicacion.domain.FuenteProxy;
import aplicacion.domain.hechos.Hecho;
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
        Map<String, Object> datos;
        Hecho hecho;
        while((datos = biblioteca.siguienteHecho(url, ultimaConsulta)) != null) {
            hecho = hechoBuilder.construirHecho(datos);
            hechos.add(hecho);
        }
    }

    @Override
    public List<Hecho> importarHechos() {
        return hechos;
    }
}