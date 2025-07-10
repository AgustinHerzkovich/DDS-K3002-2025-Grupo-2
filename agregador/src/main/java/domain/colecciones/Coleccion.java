package domain.colecciones;

import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;
import domain.criterios.CriterioDePertenencia;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

// TODO: Los Transient son temporales porque estamos testeando con la base de datos en memoria H2.
// COLECCION
@Entity
@NoArgsConstructor
public class Coleccion{
    private String titulo;
    private String descripcion;
    @Transient
    @Getter
    private List<CriterioDePertenencia> criteriosDePertenencia;
    @Getter
    @Transient
    private List<Fuente> fuentes;
    @Getter
    @Id
    private String identificadorHandle;
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private AlgoritmoConsenso algoritmoConsenso = AlgoritmoConsenso.IRRESTRICTO;

    public Coleccion(String titulo, String descripcion, List<CriterioDePertenencia> criteriosDePertenencia, List<Fuente> fuentes, AlgoritmoConsenso algoritmo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criteriosDePertenencia = criteriosDePertenencia;
        this.fuentes = fuentes;
        this.identificadorHandle = UUID.randomUUID().toString().replace("-", "");
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
}