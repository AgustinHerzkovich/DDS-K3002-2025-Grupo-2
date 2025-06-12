package domain.solicitudes;

public class EstadoSolicitudAceptada extends EstadoSolicitud {
    public EstadoSolicitudAceptada(SolicitudEliminacion slt) {
        super(slt);
    }

    @Override
    public void aceptar( ) {
    }

    @Override
    public void rechazar( ) {
    }

    @Override
    public void prescribir( ) {
    }

    @Override
    public void marcarSpam( ) {
    }

    @Override
    public void anularAceptacion( ) {
        solicitud.setEstado(new EstadoSolicitudPendiente(solicitud));
        solicitud.anularPrescripcionCosolicitudes();
        solicitud.mostrarHecho();
        solicitud.setFecha_resolucion(null);

    }

    @Override
    public void anularRechazo() {
    }

    @Override
    public void anularPrescripcion() {
    }

    @Override
    public void anularMarcaSpam() {
    }
}
