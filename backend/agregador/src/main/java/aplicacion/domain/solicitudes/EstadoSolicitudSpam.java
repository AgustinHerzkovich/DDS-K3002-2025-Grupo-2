package aplicacion.domain.solicitudes;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class EstadoSolicitudSpam extends EstadoSolicitud {
    @Override
    public void anularMarcaSpam(SolicitudEliminacion solicitud) {
        solicitud.setEstado(new EstadoSolicitudPendiente());
        solicitud.setFechaResolucion(LocalDateTime.now());
    }

    @Override
    public String getNombreEstado() {
        return "SPAM";
    }
}
