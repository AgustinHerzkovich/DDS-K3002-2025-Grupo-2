package aplicacion.domain.hechosYSolicitudes.solicitudes;

import jakarta.persistence.Entity;

@Entity
public class EstadoSolicitudAceptada extends EstadoSolicitud {
    @Override
    public String getNombreEstado() {
        return "ACEPTADA";
    }
}
