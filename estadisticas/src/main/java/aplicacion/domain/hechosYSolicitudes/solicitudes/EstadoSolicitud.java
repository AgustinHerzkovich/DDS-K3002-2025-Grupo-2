package aplicacion.domain.hechosYSolicitudes.solicitudes;

// ESTADO
/*
Implementacion vieja:
public enum Estado{
    ACEPTADA,
    RECHAZADA,
    PENDIENTE,
    PRESCRIPTA,
    SPAM
}*/

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,      // usamos el nombre para diferenciar subclases
        include = JsonTypeInfo.As.PROPERTY, // el tipo estará como propiedad en el JSON
        property = "tipo"                // nombre del campo que indica el tipo concreto
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EstadoSolicitudAceptada.class, name = "aceptada"),
        @JsonSubTypes.Type(value = EstadoSolicitudPendiente.class, name = "pendiente"),
        @JsonSubTypes.Type(value = EstadoSolicitudPrescripta.class, name = "prescripta"),
        @JsonSubTypes.Type(value = EstadoSolicitudRechazada.class, name = "rechazada"),
        @JsonSubTypes.Type(value = EstadoSolicitudSpam.class, name = "spam")
})
public abstract class EstadoSolicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public abstract String getNombreEstado();
}
