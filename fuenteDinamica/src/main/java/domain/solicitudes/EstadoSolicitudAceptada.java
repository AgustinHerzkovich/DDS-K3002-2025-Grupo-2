package domain.solicitudes;

public class EstadoSolicitudAceptada extends EstadoSolicitud {
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
        s.setEstado(new EstadoSolicitudPendiente());
        s.anularPrescripcionCosolicitudes();
        s.mostrarHecho();
        s.setFechaResolucion(null);

    }

    @Override
    void anularRechazo(SolicitudEliminacion s) {
    }

    @Override
    void anularPrescripcion(SolicitudEliminacion s) {
    }

    @Override
    void anularMarcaSpam(SolicitudEliminacion s) {
    }
}
