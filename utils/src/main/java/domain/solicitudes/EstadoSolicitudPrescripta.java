package domain.solicitudes;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class EstadoSolicitudPrescripta extends EstadoSolicitud {
    public EstadoSolicitudPrescripta(SolicitudEliminacion slt) {
        super(slt);
    }

    @Override
    public void aceptar () {
    }

    @Override
    public void rechazar () {
        solicitud.setEstado(new EstadoSolicitudRechazada(solicitud));
        solicitud.setFechaResolucion(LocalDateTime.now());
    }

    @Override
    public void prescribir () {
    }

    @Override
    public void marcarSpam () {
        solicitud.setEstado(new EstadoSolicitudSpam(solicitud));
        solicitud.setFechaResolucion(LocalDateTime.now());
    }

    @Override
    public void anularAceptacion () {
    }

    @Override
    public void anularRechazo () {
    }

    @Override
    public void anularPrescripcion () {
        solicitud.setEstado(new EstadoSolicitudPrescripta(solicitud));
    }

    @Override
    public void anularMarcaSpam () {
    }
    @Override
    public String getNombreEstado() {
        return "PRESCRIPTA";
    }
}
