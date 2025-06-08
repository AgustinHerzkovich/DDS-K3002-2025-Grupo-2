package domain.solicitudes;

public class EstadoSolicitudSpam extends EstadoSolicitud {
    @Override
    void aceptar(SolicitudEliminacion s) {
    }

    @Override
    void rechazar(SolicitudEliminacion s) {
    }

    @Override
    void prescribir(SolicitudEliminacion s) {
    }

    @Override
    void marcarSpam(SolicitudEliminacion s) {
    }

    @Override
    void anularAceptacion(SolicitudEliminacion s) {
    }

    @Override
    void anularRechazo(SolicitudEliminacion s) {
    }

    @Override
    void anularPrescripcion(SolicitudEliminacion s) {
    }

    @Override
    void anularMarcaSpam(SolicitudEliminacion s) {
    s.setEstado(new EstadoSolicitudPendiente());
    }
}
