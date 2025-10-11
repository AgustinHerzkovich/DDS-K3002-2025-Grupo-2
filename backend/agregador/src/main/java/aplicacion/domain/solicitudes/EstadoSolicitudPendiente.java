package aplicacion.domain.solicitudes;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class EstadoSolicitudPendiente extends EstadoSolicitud {
    @Override
    public void aceptar(SolicitudEliminacion solicitud) {
        solicitud.setEstado(new EstadoSolicitudAceptada());
        solicitud.preescribirCosolicitudes();
        solicitud.esconderHecho();
        solicitud.setFechaResolucion(LocalDateTime.now());
    }

    @Override
    public void rechazar(SolicitudEliminacion solicitud) {
        solicitud.setEstado(new EstadoSolicitudRechazada());
        solicitud.setFechaResolucion(LocalDateTime.now());
    }

    @Override
    public void prescribir(SolicitudEliminacion solicitud) {
        solicitud.setEstado(new EstadoSolicitudPrescripta());
    }

    @Override
    public void marcarSpam(SolicitudEliminacion solicitud) {
        solicitud.setEstado(new EstadoSolicitudSpam());
        solicitud.setFechaResolucion(LocalDateTime.now());
    }

    @Override
    public String getNombreEstado() {
        return "PENDIENTE";
    }
}
