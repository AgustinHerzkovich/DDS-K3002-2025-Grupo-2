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

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public abstract class EstadoSolicitud {
     @ManyToOne
     SolicitudEliminacion solicitud;  //Solicitud a la que apunta

    public EstadoSolicitud(SolicitudEliminacion slt){
        solicitud = slt;
    }

    public EstadoSolicitud() {

    }

    public abstract void aceptar();
    public abstract void rechazar();
    public abstract void prescribir();
    public abstract void marcarSpam();
    public abstract void anularAceptacion();
    public abstract void anularRechazo();
    public abstract void anularPrescripcion();
    public abstract void anularMarcaSpam();
}
