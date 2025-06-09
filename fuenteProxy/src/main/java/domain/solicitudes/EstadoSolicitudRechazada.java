package domain.solicitudes;

public class EstadoSolicitudRechazada extends EstadoSolicitud {
    public EstadoSolicitudRechazada(SolicitudEliminacion slt) {
        super(slt);
    }

    @Override
    void aceptar () {
    }

    @Override
    void rechazar () {
    }

    @Override
    void prescribir () {
    }

    @Override
    void marcarSpam () {
    }

    @Override
    void anularAceptacion () {
    }

    @Override
    void anularRechazo () {
        if (solicitud.hechoVisible())
            solicitud.setEstado(new EstadoSolicitudPendiente(solicitud));
        else
            solicitud.setEstado(new EstadoSolicitudPrescripta(solicitud));
        solicitud.setFecha_resolucion(null);
    }

    @Override
    void anularPrescripcion () {
    }

    @Override
    void anularMarcaSpam () {
    }
}
