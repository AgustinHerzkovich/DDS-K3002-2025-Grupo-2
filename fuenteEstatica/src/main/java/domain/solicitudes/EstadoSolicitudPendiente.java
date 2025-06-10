package domain.solicitudes;

import java.time.LocalDate;

public class EstadoSolicitudPendiente extends EstadoSolicitud {
    public EstadoSolicitudPendiente(SolicitudEliminacion slt) {
        super(slt);
    }

    @Override
    void aceptar () {
    solicitud.setEstado(new EstadoSolicitudAceptada(solicitud));
    //solicitud.preescribirCosolicitudes();
    solicitud.esconderHecho();
    solicitud.setFecha_resolucion(LocalDate.now());
    }

    @Override
    void rechazar () {
    solicitud.setEstado(new EstadoSolicitudRechazada(solicitud));
    solicitud.setFecha_resolucion(LocalDate.now());
    }

    @Override
    void prescribir () {
    solicitud.setEstado(new EstadoSolicitudPrescripta(solicitud));
    }

    @Override
    void marcarSpam () {
        solicitud.setEstado(new EstadoSolicitudSpam(solicitud));
        solicitud.setFecha_resolucion(LocalDate.now());
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
