package aplicacion.domain.hechosYSolicitudes.solicitudes;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class EstadoSolicitudPrescripta extends EstadoSolicitud {
    @Override
    public String getNombreEstado() {
        return "PRESCRIPTA";
    }
}
