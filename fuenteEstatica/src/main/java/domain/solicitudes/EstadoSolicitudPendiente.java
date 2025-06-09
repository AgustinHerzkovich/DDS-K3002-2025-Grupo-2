package domain.solicitudes;

import java.time.LocalDateTime;

public class EstadoSolicitudPendiente extends EstadoSolicitud {
    public EstadoSolicitudPendiente(SolicitudEliminacion slt) {
        super(slt);
    }

    @Override
    void aceptar () {
    solicitud.setEstado(new EstadoSolicitudAceptada(solicitud));
    //solicitud.preescribirCosolicitudes();
    solicitud.esconderHecho();
    solicitud.setFechaResolucion(LocalDateTime.now());
    }

    @Override
    void rechazar () {
    solicitud.setEstado(new EstadoSolicitudRechazada(solicitud));
    }

    @Override
    void prescribir () {
    solicitud.setEstado(new EstadoSolicitudPrescripta(solicitud));
    }

    @Override
    void marcarSpam () {
    solicitud.setEstado(new EstadoSolicitudSpam(solicitud));
    }

    @Override
    void anularAceptacion () {
    }

    @Override
    void anularRechazo () {
    }

    @Override
    void anularPrescripcion () {
    }

    @Override
    void anularMarcaSpam () {
    }
}
