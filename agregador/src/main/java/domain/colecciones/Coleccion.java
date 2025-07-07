package domain.colecciones;

import domain.algoritmos.Algoritmo;
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
    private List<CriterioDePertenencia> criterios_pertenencia;
    @Getter
    @Transient
    private List<Fuente> fuentes;
    @Getter
    @Id
    private String identificador_handle;
    @Getter
    @Enumerated(EnumType.STRING)
    private AlgoritmoConsenso algoritmo_consenso;
    //tiene todos los hechos que de las fuentes de esta coleccion
    //TODO: SUPONEMOS QUE REPOSITORIO HECHOS X COLECCION ES UN REPOSITORIO QUE CONTIENE TODOS LOS HECHOS DE ESTA COLECCION
    //JUNTO CON SUS RESPECTIVOS ATRIBUTOS.

    // La base de datos nos pasa una tupla:
            //          (Hecho, EsConsensuado)
    // Como sabemos establecemos el valor de EsConsensuado en la BD?
            // Se corre un proceso a las 2 de la mañana que actualiza la BD
    // Como se sabe que algoritmo usar?

    // TODO: Ver que no se cargue dos veces el mismo hecho si dos colecciones comparten la fuente


    public Coleccion(String titulo, String descripcion, List<CriterioDePertenencia> criterios_pertenencia, List<Fuente> fuentes, AlgoritmoConsenso algoritmo) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.criterios_pertenencia = criterios_pertenencia;
        this.fuentes = fuentes;
        this.identificador_handle = UUID.randomUUID().toString().replace("-", "");
        this.algoritmo_consenso = algoritmo;
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

    public Boolean cumpleCriterios(List<CriterioDePertenencia> criterios_pertenencia, Hecho hecho){
        return criterios_pertenencia.stream().allMatch(criterio -> criterio.cumpleCriterio(hecho));
    }
}
