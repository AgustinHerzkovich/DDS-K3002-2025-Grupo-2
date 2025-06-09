package domain.solicitudes;

public class EstadoSolicitudSpam extends EstadoSolicitud {
    public EstadoSolicitudSpam(SolicitudEliminacion slt) {
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
    }

    @Override
    void anularPrescripcion () {
    }

    @Override
    void anularMarcaSpam () {
    solicitud.setEstado(new EstadoSolicitudPendiente(solicitud));
    }
}
