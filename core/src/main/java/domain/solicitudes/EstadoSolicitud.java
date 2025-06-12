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

public abstract class EstadoSolicitud {
     SolicitudEliminacion solicitud;  //Solicitud a la que apunta
    public EstadoSolicitud(SolicitudEliminacion slt){
        solicitud = slt;
    }
    abstract void aceptar();
    abstract void rechazar();
    abstract void prescribir();
    abstract void marcarSpam();
    abstract void anularAceptacion();
    abstract void anularRechazo();
    abstract void anularPrescripcion();
    abstract void anularMarcaSpam();
}
