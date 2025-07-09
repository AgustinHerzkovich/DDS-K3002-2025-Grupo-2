package domain.solicitudes;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class EstadoSolicitudSpam extends EstadoSolicitud {
    public EstadoSolicitudSpam(SolicitudEliminacion slt) {
        super(slt);
    }

    @Override
    public void aceptar () {
    }

    @Override
    public void rechazar () {
    }

    @Override
    public void prescribir () {
    }

    @Override
    public void marcarSpam () {
    }

    @Override
    public void anularAceptacion () {
    }

    @Override
    public void anularRechazo () {
    }

    @Override
    public void anularPrescripcion () {
    }

    @Override
    public void anularMarcaSpam () {
        solicitud.setEstado(new EstadoSolicitudPendiente(solicitud));
        solicitud.setFechaResolucion(LocalDateTime.now());
    }

    @Override
    public String getNombreEstado() {
        return "SPAM";
    }
}
