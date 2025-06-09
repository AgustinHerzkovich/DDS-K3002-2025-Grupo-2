package domain.solicitudes;

import java.time.LocalDate;

public class EstadoSolicitudPrescripta extends EstadoSolicitud {
    public EstadoSolicitudPrescripta(SolicitudEliminacion slt) {
        super(slt);
    }

    @Override
    void aceptar () {
    }

    @Override
    void rechazar () {
        solicitud.setEstado(new EstadoSolicitudRechazada(solicitud));
        solicitud.setFecha_resolucion(LocalDate.now());
    }

    @Override
    void prescribir () {
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
        solicitud.setEstado(new EstadoSolicitudPrescripta(solicitud));
    }

    @Override
    void anularMarcaSpam () {
    }
}
