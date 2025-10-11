package aplicacion.domain.solicitudes;

import jakarta.persistence.Entity;

@Entity
public class EstadoSolicitudAceptada extends EstadoSolicitud {
    @Override
    public void anularAceptacion(SolicitudEliminacion solicitud) {
        solicitud.setEstado(new EstadoSolicitudPendiente());
        solicitud.anularPrescripcionCosolicitudes();
        solicitud.mostrarHecho();
        solicitud.setFechaResolucion(null);
    }

    @Override
    public String getNombreEstado() {
        return "ACEPTADA";
    }
}
