package aplicacion.domain.hechosYSolicitudes.solicitudes;

import jakarta.persistence.Entity;

@Entity
public class EstadoSolicitudPendiente extends EstadoSolicitud {
    @Override
    public String getNombreEstado() {
        return "PENDIENTE";
    }
}
