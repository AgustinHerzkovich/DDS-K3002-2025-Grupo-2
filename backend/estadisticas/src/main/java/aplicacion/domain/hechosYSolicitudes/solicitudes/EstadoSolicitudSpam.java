package aplicacion.domain.hechosYSolicitudes.solicitudes;

import jakarta.persistence.Entity;


@Entity
public class EstadoSolicitudSpam extends EstadoSolicitud {
    @Override
    public String getNombreEstado() {
        return "SPAM";
    }
}
