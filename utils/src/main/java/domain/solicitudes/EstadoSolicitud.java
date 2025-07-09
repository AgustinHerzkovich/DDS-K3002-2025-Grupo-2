package domain.solicitudes;

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

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
public abstract class EstadoSolicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "estado")
    protected SolicitudEliminacion solicitud;  //Solicitud a la que apunta

    public EstadoSolicitud(SolicitudEliminacion slt){
        solicitud = slt;
    }

    public abstract void aceptar();
    public abstract void rechazar();
    public abstract void prescribir();
    public abstract void marcarSpam();
    public abstract void anularAceptacion();
    public abstract void anularRechazo();
    public abstract void anularPrescripcion();
    public abstract void anularMarcaSpam();
    public abstract String getNombreEstado();
}
