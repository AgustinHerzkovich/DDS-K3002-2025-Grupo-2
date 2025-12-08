package aplicacion.domain.hechosYSolicitudes.solicitudes;

import jakarta.persistence.Entity;

@Entity
public class EstadoSolicitudPrescripta extends EstadoSolicitud {
    @Override
    public String getNombreEstado() {
        return "PRESCRIPTA";
    }
}
