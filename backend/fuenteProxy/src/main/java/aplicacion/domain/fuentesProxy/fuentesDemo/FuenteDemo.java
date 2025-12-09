package aplicacion.domain.fuentesProxy.fuentesDemo;

import aplicacion.domain.fuentesProxy.FuenteProxy;
import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// FUENTE DEMO
@Entity
@Getter
@Setter
public class FuenteDemo extends FuenteProxy {
    private LocalDateTime ultimaConsulta;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Conexion biblioteca;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Hecho> hechos;
    private String url;
    @Transient
    private HechoBuilder hechoBuilder;

    public FuenteDemo(Conexion biblioteca, String url) {
        this.ultimaConsulta = LocalDateTime.now();
        this.biblioteca = biblioteca;
        this.url = url;
        this.hechos = new ArrayList<>();
        this.hechoBuilder = new HechoBuilder();
    }

    public FuenteDemo() {
        this.hechoBuilder = new HechoBuilder();
    }
}