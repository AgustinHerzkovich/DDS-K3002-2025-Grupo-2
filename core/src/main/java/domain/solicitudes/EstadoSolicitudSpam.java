package domain.solicitudes;

import java.time.LocalDate;

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
        solicitud.setFecha_resolucion(LocalDate.now());
    }
}
