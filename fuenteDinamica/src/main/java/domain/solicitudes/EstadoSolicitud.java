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

abstract class Estado{
    abstract void aceptar(SolicitudEliminacion s);
    abstract void rechazar(SolicitudEliminacion s);
    abstract void prescribir(SolicitudEliminacion s);
    abstract void marcarSpam(SolicitudEliminacion s);
    abstract void anularAceptacion(SolicitudEliminacion s);
    abstract void anularRechazo(SolicitudEliminacion s);
    abstract void anularPrescripcion(SolicitudEliminacion s);
    abstract void anularMarcaSpam(SolicitudEliminacion s);
}
