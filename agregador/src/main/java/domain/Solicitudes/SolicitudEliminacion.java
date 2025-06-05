package domain.Solicitudes;

import domain.Usuarios.Contribuyente;
import domain.Hechos.Hecho;

import java.time.LocalDateTime;


// SOLICITUD DE ELIMINACION
public class SolicitudEliminacion implements DetectorDeSpam{
    private Estado estado;
    private Contribuyente solicitante;
    private Contribuyente administrador;
    private LocalDateTime fecha_subida;
    private LocalDateTime fecha_resolucion;
    private Hecho hecho;
    private String motivo;

    public SolicitudEliminacion(Contribuyente solicitante, Hecho hecho, String motivo) {
        this.estado = Estado.PENDIENTE;
        this.solicitante = solicitante;
        this.administrador = null;
        this.fecha_subida = LocalDateTime.now();
        this.fecha_resolucion = null;
        this.hecho = hecho;
        this.motivo = motivo;
	if (this.esSpam(motivo)) {
		this.rechazar();
	}
    }

    public void setFechaResolucion(LocalDateTime fecha){
        this.fecha_resolucion = fecha;
    }

    public void setAdministrador(Contribuyente administrador){
        this.administrador = administrador;
    }

    public void setEstado(Estado estado){
        this.estado = estado;
    }

    public void aceptar(){
        this.setEstado(Estado.ACEPTADA);
        // TODO
    }

    public void rechazar(){
        this.setEstado(Estado.RECHAZADA);
	this.fecha_resolucion = LocalDateTime.now();
        // TODO
    }

    public Boolean esSpam(String texto){
        // TODO : NO SABEMOS AÚN CÓMO SE IMPLEMENTA
    }
}