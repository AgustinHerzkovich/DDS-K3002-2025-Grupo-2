package domain.solicitudes;

public class EstadoSolicitudPrescripta extends EstadoSolicitud {
    public EstadoSolicitudPrescripta(SolicitudEliminacion slt) {
        super(slt);
    }

    @Override
    void aceptar () {
    }

    @Override
    void rechazar () {
        solicitud.setEstado(new EstadoSolicitudRechazada(solicitud));
    }

    @Override
    void prescribir () {
    }

    @Override
    void marcarSpam () {
        solicitud.setEstado(new EstadoSolicitudSpam(solicitud));
    }

    @Override
    void anularAceptacion () {
    }

    @Override
    void anularRechazo () {
    }

    @Override
    void anularPrescripcion () {
        solicitud.setEstado(new EstadoSolicitudPrescripta(solicitud));
    }

    @Override
    void anularMarcaSpam () {
    }
}
