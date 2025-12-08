package aplicacion.domain.colecciones.fuentes;

import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public abstract class Fuente {

    @Id
    private String id;
    private String alias;
    private LocalDateTime ultimaPeticion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "hecho_fuente",
            joinColumns = @JoinColumn(name = "fuente_id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id"))

    private List<Hecho> hechos;


    public Fuente(String id, String alias) {
        this.id = id;
        this.ultimaPeticion = null; // Arranca en null para que si es la primera petición, traer todos los hechos
        this.hechos = new ArrayList<>();
        this.alias = alias;
    }
    public Fuente(String id) {
        this.id = id;
        this.ultimaPeticion = null;
        this.hechos = new ArrayList<>();
        this.alias = "Fuente sin titulo";
    }



    public void agregarHechos(List<Hecho> hechosAAgregar) {
        this.hechos.addAll(hechosAAgregar);
    }

    public void eliminarTodosLosHechos() {
        this.hechos.clear();
    }
}
