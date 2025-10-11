package aplicacion.domain.hechosYSolicitudes.solicitudes;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class EstadoSolicitudPendiente extends EstadoSolicitud {
    @Override
    public String getNombreEstado() {
        return "PENDIENTE";
    }
}
