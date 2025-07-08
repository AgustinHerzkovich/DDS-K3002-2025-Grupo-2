package domain.solicitudes;

import java.time.LocalDateTime;

public class EstadoSolicitudPendiente extends EstadoSolicitud {
    public EstadoSolicitudPendiente(SolicitudEliminacion slt) {
        super(slt);
    }

    @Override
    public void aceptar () {
    solicitud.setEstado(new EstadoSolicitudAceptada(solicitud));
    solicitud.preescribirCosolicitudes();
    solicitud.esconderHecho();
    solicitud.setFechaResolucion(LocalDateTime.now());
    }

    @Override
    public void rechazar () {
    solicitud.setEstado(new EstadoSolicitudRechazada(solicitud));
    solicitud.setFechaResolucion(LocalDateTime.now());
    }

    @Override
    public void prescribir () {
    solicitud.setEstado(new EstadoSolicitudPrescripta(solicitud));
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
    }

    @Override
    public void anularMarcaSpam () {
    }
}
