package domain.solicitudes;

public class EstadoSolicitudRechazada extends EstadoSolicitud {
    public EstadoSolicitudRechazada(SolicitudEliminacion slt) {
        super(slt);
    }

    @Override
    public void aceptar () {
    }

    @Override
    public void rechazar () {
    }

    @Override
    public void prescribir () {
    }

    @Override
    public void marcarSpam () {
    }

    @Override
    public void anularAceptacion () {
    }

    @Override
    public void anularRechazo () {
        if (solicitud.hechoVisible())
            solicitud.setEstado(new EstadoSolicitudPendiente(solicitud));
        else
            solicitud.setEstado(new EstadoSolicitudPrescripta(solicitud));
        solicitud.setFecha_resolucion(null);
    }

    @Override
    public void anularPrescripcion () {
    }

    @Override
    public void anularMarcaSpam () {
    }
}
