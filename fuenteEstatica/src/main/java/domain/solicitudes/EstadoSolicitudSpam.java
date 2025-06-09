package domain.solicitudes;

import java.time.LocalDate;

public class EstadoSolicitudSpam extends EstadoSolicitud {
    public EstadoSolicitudSpam(SolicitudEliminacion slt) {
        super(slt);
    }

    @Override
    void aceptar () {
    }

    @Override
    void rechazar () {
    }

    @Override
    void prescribir () {
    }

    @Override
    void marcarSpam () {
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
        solicitud.setEstado(new EstadoSolicitudPendiente(solicitud));
        solicitud.setFecha_resolucion(LocalDate.now());
    }
}
