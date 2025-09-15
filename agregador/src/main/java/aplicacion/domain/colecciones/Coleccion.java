package aplicacion.domain.colecciones;

import aplicacion.domain.algoritmos.AlgoritmoConsenso;
import aplicacion.domain.algoritmos.AlgoritmoConsensoIrrestricto;
import aplicacion.domain.algoritmos.TipoAlgoritmoConsenso;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.criterios.CriterioDePertenencia;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// COLECCION
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Coleccion{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(length = 50)
    private String titulo;
    @Column(length = 150)
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "coleccion_id")
    private List<CriterioDePertenencia> criteriosDePertenencia;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Fuente> fuentes;
    @Transient
    private AlgoritmoConsenso algoritmoConsenso = new AlgoritmoConsensoIrrestricto();
    @Enumerated(EnumType.STRING)
    private TipoAlgoritmoConsenso tipoAlgoritmoConsenso = TipoAlgoritmoConsenso.IRRESTRICTO;

    public Coleccion(
            String titulo,
            String descripcion,
            List<CriterioDePertenencia> criteriosDePertenencia,
            List<Fuente> fuentes,
            AlgoritmoConsenso algoritmo,
            TipoAlgoritmoConsenso tipoAlgoritmo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criteriosDePertenencia = criteriosDePertenencia;
        this.fuentes = fuentes;
        this.algoritmoConsenso = algoritmo;
        this.tipoAlgoritmoConsenso = tipoAlgoritmo;
    }

    public Boolean cumpleCriterios(Hecho hecho){
        return criteriosDePertenencia.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));
    }

    public void agregarFuente(Fuente fuente) {
        if (!fuentes.contains(fuente)) {
            fuentes.add(fuente);
        }
    }

    public void quitarFuente(Fuente fuente) {
        fuentes.remove(fuente);
    }
}