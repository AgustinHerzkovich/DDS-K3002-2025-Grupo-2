package aplicacion.domain.solicitudes;

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
@Inheritance(strategy = InheritanceType.JOINED)
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

    public void aceptar(SolicitudEliminacion solicitud) {}
    public void rechazar(SolicitudEliminacion solicitud) {}
    public void prescribir(SolicitudEliminacion solicitud) {}
    public void marcarSpam(SolicitudEliminacion solicitud) {}
    public void anularAceptacion(SolicitudEliminacion solicitud) {}
    public void anularRechazo(SolicitudEliminacion solicitud) {}
    public void anularPrescripcion(SolicitudEliminacion solicitud) {}
    public void anularMarcaSpam(SolicitudEliminacion solicitud) {}
    public abstract String getNombreEstado();
}
