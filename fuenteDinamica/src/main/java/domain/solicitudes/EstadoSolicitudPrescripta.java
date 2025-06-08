package domain.solicitudes;

public class EstadoSolicitudPrescripta extends EstadoSolicitud {
    @Override
    void aceptar(SolicitudEliminacion s) {
    }

    @Override
    void rechazar(SolicitudEliminacion s) {
        s.setEstado(new EstadoSolicitudRechazada());
    }

    @Override
    void prescribir(SolicitudEliminacion s) {
    }

    @Override
    void marcarSpam(SolicitudEliminacion s) {
        s.setEstado(new EstadoSolicitudSpam());
    }

    @Override
    void anularAceptacion(SolicitudEliminacion s) {
    }

    @Override
    void anularRechazo(SolicitudEliminacion s) {
    }

    @Override
    void anularPrescripcion(SolicitudEliminacion s) {
        s.setEstado(new EstadoSolicitudPrescripta());
    }

    @Override
    void anularMarcaSpam(SolicitudEliminacion s) {
    }
}
