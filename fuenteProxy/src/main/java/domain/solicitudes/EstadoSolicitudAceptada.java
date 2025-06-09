package domain.solicitudes;

public class EstadoSolicitudAceptada extends EstadoSolicitud {
    public EstadoSolicitudAceptada(SolicitudEliminacion slt) {
        super(slt);
    }

    @Override
    void aceptar( ) {
    }

    @Override
    void rechazar( ) {
    }

    @Override
    void prescribir( ) {
    }

    @Override
    void marcarSpam( ) {
    }

    @Override
    void anularAceptacion( ) {
        solicitud.setEstado(new EstadoSolicitudPendiente(solicitud));
        solicitud.anularPrescripcionCosolicitudes();
        solicitud.mostrarHecho();
        solicitud.setFecha_resolucion(null);

    }

    @Override
    void anularRechazo() {
    }

    @Override
    void anularPrescripcion() {
    }

    @Override
    void anularMarcaSpam() {
    }
}
