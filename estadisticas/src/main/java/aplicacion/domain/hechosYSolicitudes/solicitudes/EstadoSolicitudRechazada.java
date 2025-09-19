package aplicacion.domain.hechosYSolicitudes.solicitudes;

import jakarta.persistence.Entity;

@Entity
public class EstadoSolicitudRechazada extends EstadoSolicitud {
    @Override
    public String getNombreEstado() {
        return "RECHAZADA";
    }
}
