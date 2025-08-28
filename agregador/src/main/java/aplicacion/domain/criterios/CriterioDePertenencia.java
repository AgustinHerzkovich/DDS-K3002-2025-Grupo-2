package aplicacion.domain.criterios;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import domain.hechos.Hecho;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

// CRITERIO DE PERTENENCIA
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@DiscriminatorColumn(name = "tipo_criterio")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,      // usamos el nombre para diferenciar subclases
        include = JsonTypeInfo.As.PROPERTY, // el tipo estará como propiedad en el JSON
        property = "tipo"                // nombre del campo que indica el tipo concreto
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CriterioDeDistancia.class, name = "distancia"),
        @JsonSubTypes.Type(value = CriterioDeFecha.class, name = "fecha")
})
public abstract class CriterioDePertenencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public abstract Boolean cumpleCriterio(Hecho hecho);
}