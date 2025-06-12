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
    public abstract void aceptar();
    public abstract void rechazar();
    public abstract void prescribir();
    public abstract void marcarSpam();
    public abstract void anularAceptacion();
    public abstract void anularRechazo();
    public abstract void anularPrescripcion();
    public abstract void anularMarcaSpam();
}
