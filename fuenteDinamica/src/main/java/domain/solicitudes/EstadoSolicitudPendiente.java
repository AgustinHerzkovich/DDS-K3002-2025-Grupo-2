package domain.solicitudes;

import java.time.LocalDateTime;

public class EstadoSolicitudPendiente extends EstadoSolicitud {
    @Override
    void aceptar(SolicitudEliminacion s) {
    s.setEstado(new EstadoSolicitudAceptada());
    s.preescribirCosolicitudes();
    s.esconderHecho();
    s.setFechaResolucion(LocalDateTime.now());
    }

    @Override
    void rechazar(SolicitudEliminacion s) {
    s.setEstado(new EstadoSolicitudRechazada());
    }

    @Override
    void prescribir(SolicitudEliminacion s) {
    s.setEstado(new EstadoSolicitudPrescripta());
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
    }

    @Override
    void anularMarcaSpam(SolicitudEliminacion s) {
    }
}
