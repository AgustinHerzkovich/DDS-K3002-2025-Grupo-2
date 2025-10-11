package aplicacion.domain.solicitudes;

import jakarta.persistence.Entity;

@Entity
public class EstadoSolicitudRechazada extends EstadoSolicitud {
    @Override
    public void anularRechazo(SolicitudEliminacion solicitud) {
        if (solicitud.hechoVisible())
            solicitud.setEstado(new EstadoSolicitudPendiente());
        else
            solicitud.setEstado(new EstadoSolicitudPrescripta());
        solicitud.setFechaResolucion(null);
    }

    @Override
    public String getNombreEstado() {
        return "RECHAZADA";
    }
}
