package domain.solicitudes;

import java.time.LocalDate;

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
        solicitud.setFecha_resolucion(LocalDate.now());
    }

    @Override
    public void prescribir () {
    }

    @Override
    public void marcarSpam () {
        solicitud.setEstado(new EstadoSolicitudSpam(solicitud));
        solicitud.setFecha_resolucion(LocalDate.now());
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
}
