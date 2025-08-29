package aplicacion.domain.colecciones;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import aplicacion.domain.algoritmos.AlgoritmoConsenso;
import aplicacion.domain.algoritmos.AlgoritmoConsensoIrrestricto;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.criterios.CriterioDePertenencia;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;
import java.util.List;

// COLECCION
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Coleccion{
    private String titulo;
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "coleccion_id")
    private List<CriterioDePertenencia> criteriosDePertenencia;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Fuente> fuentes;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String identificadorHandle;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "algoritmo_id")
    private AlgoritmoConsenso algoritmoConsenso = new AlgoritmoConsensoIrrestricto();

    @JsonCreator
    public Coleccion(
            @JsonProperty("titulo") String titulo,
            @JsonProperty("descripcion") String descripcion,
            @JsonProperty("criteriosDePertenencia") List<CriterioDePertenencia> criteriosDePertenencia,
            @JsonProperty("fuentes") List<Fuente> fuentes,
            @JsonProperty("algoritmoConsenso") AlgoritmoConsenso algoritmo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criteriosDePertenencia = criteriosDePertenencia;
        this.fuentes = fuentes;
        this.algoritmoConsenso = algoritmo;
        // Tal vez convenga delegar esto en otra clase
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Leemos config.json desde el classpath (src/main/resources)
            InputStream input = getClass().getClassLoader().getResourceAsStream("config.json");
            if (input == null) {
                throw new RuntimeException("No se encontró config.json en resources");
            }

            // Parseamos el JSON y extraemos el campo "fuente"
            JsonNode root = mapper.readTree(input);
            JsonNode fuentesNode = root.path("fuentes");
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar config.json", e);
        }
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