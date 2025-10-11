package aplicacion.domain.solicitudes;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class EstadoSolicitudPrescripta extends EstadoSolicitud {
    @Override
    public void rechazar(SolicitudEliminacion solicitud) {
        solicitud.setEstado(new EstadoSolicitudRechazada());
        solicitud.setFechaResolucion(LocalDateTime.now());
    }

    @Override
    public void marcarSpam(SolicitudEliminacion solicitud) {
        solicitud.setEstado(new EstadoSolicitudSpam());
        solicitud.setFechaResolucion(LocalDateTime.now());
    }

    @Override
    public void anularPrescripcion(SolicitudEliminacion solicitud) {
        solicitud.setEstado(new EstadoSolicitudPrescripta());
    }

    @Override
    public String getNombreEstado() {
        return "PRESCRIPTA";
    }
}
