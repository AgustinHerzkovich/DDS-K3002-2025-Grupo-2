package domain.colecciones;

import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;
import domain.criterios.CriterioDePertenencia;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

// TODO: Los Transient son temporales porque estamos testeando con la base de datos en memoria H2.
// COLECCION
@Entity
public class Coleccion{
    private String titulo;
    private String descripcion;
    @Transient
    private List<CriterioDePertenencia> criteriosDePertenencia;
    @Getter
    @Transient
    private List<Fuente> fuentes;
    @Getter
    @Id
    private String identificadorHandle;
    @Getter
    @Enumerated(EnumType.STRING)
    private AlgoritmoConsenso algoritmoConsenso = AlgoritmoConsenso.IRRESTRICTO;
    //tiene todos los hechos que de las fuentes de esta coleccion
    //SUPONEMOS QUE REPOSITORIO HECHOS X COLECCION ES UN REPOSITORIO QUE CONTIENE TODOS LOS HECHOS DE ESTA COLECCION
    //JUNTO CON SUS RESPECTIVOS ATRIBUTOS.

    // La base de datos nos pasa una tupla:
            //          (Hecho, EsConsensuado)
    // Como sabemos establecemos el valor de EsConsensuado en la BD?
            // Se corre un proceso a las 2 de la mañana que actualiza la BD
    // Como se sabe que algoritmo usar?

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

    public Coleccion() {

    }

    public Boolean cumpleCriterios(List<CriterioDePertenencia> criterios, Hecho hecho){
        return criterios.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));
    }
}