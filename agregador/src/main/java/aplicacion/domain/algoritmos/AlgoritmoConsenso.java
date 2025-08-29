package aplicacion.domain.algoritmos;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

// Regla de negocio: Consideramos hecho repetido por fuente a aquellos que contengan los mismos atributos que incluimos en EqualsAndHashCode en el Hecho.
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_algoritmo")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AlgoritmoConsensoIrrestricto.class, name = "irrestricto"),
        @JsonSubTypes.Type(value = AlgoritmoConsensoAbsoluto.class, name = "absoluto"),
        @JsonSubTypes.Type(value = AlgoritmoConsensoMayoriaSimple.class, name = "mayoriaSimple"),
        @JsonSubTypes.Type(value = AlgoritmoConsensoMultiplesMenciones.class, name = "multiplesMenciones")
})
public abstract class AlgoritmoConsenso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public abstract List<Hecho> curarHechos(Map<Fuente,List<Hecho>> hechosPorFuente);
}