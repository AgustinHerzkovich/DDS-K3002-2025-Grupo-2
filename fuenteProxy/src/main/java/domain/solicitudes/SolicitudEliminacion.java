package domain.solicitudes;

import domain.hechos.Hecho;
import domain.usuarios.Contribuyente;

import java.time.LocalDateTime;

/// Entiendase por "cosolicitud": Solicitud de eliminacion que apunta al mismo hecho que la solicitud actual
///
/// Si una solicitud:
/// Esta pendiente: Se la puede rechazar(), aceptar() o marcarSpam() manualmente
///                 tambien se la puede prescribir(), pero eso lo envia la misma solicitud que pasa a aceptar (por ahora, puede que cambie si envia las prescripciones el hecho)
/// Esta prescripta: Se la puede rechazar() : Pasa a Rechazada
///                 tambien se la puede anularPrescripcion(), pero eso lo envia la misma solicitud que anula la aceptacion (por ahora)
/// Esta aceptada: Se la puede anularAceptacion() : Pasa a Pendiente
/// Esta rechazada: Se la puede anularRechazo() : Pasa a Pendiente o Prescripta
/// Esta spam: Se la puede anularMarcaSpam() : Pasa a Pendiente o Prescripta
///
/// Metodos de la clase:
/// + SolicitudEliminacion(Contribuyente solicitante, Hecho hecho, String motivo)
/// + void aceptar()
/// + void anularAceptacion()
/// + void rechazar()
/// + void anularRechazo()
/// + void prescribir()
/// + void anularPrescripcion()
/// + void marcarSpam()
/// + void anularMarcaSpam()
/// + boolean esSpam()
/// - void preescribirCosolicitudes()
/// - void anularPrescripcionCosolicitudes()


// TODO: Cuando se haga esto de la persistencia, hay que hacer que los cambios de estado de la solicitud se vean reflejados en el medio persistente

// SOLICITUD DE ELIMINACION
public class SolicitudEliminacion {
    private EstadoSolicitud estado;
    private final Contribuyente solicitante;
    private Contribuyente administrador;
    private final LocalDateTime fecha_subida;
    private LocalDateTime fecha_resolucion;
    private final Hecho hecho;
    private final String motivo;


    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public void setFechaResolucion(LocalDateTime fecha){
        this.fecha_resolucion = fecha;
    }


    public SolicitudEliminacion(Contribuyente solicitante, Hecho hecho, String motivo) {
        if (this.esSpam()){
            this.estado = new EstadoSolicitudSpam(this);
        }else{
            if(hecho.esVisible()){
                this.estado = new EstadoSolicitudPendiente(this);
            }else{
                this.estado = new EstadoSolicitudPrescripta(this);
                // Si por algun motivo "llega tarde" una solicitud y ya se elimino el hecho
            }
        }

        this.solicitante = solicitante;
        this.administrador = null;
        this.fecha_subida = LocalDateTime.now();
        this.fecha_resolucion = null;
        this.hecho = hecho;
        this.motivo = motivo;
        hecho.agregarASolicitudes(this);
        // Le manda mensaje a su hecho para que lo agregue
        // IMPORTANTE: debe estar cargado el hecho en memoria
    }

    /////////////////////////////////////

    public void aceptar(Contribuyente admin){
        estado.aceptar();
        this.administrador = admin;
    }

    public void anularAceptacion(){
        estado.anularAceptacion();
    }

    public void rechazar(Contribuyente admin){
        estado.rechazar();
        this.administrador = admin;
    }

    public void anularRechazo(){
        estado.anularRechazo();
    }

    public void prescribir(){
        estado.prescribir();
    }

    public void anularPrescripcion(){
        estado.anularPrescripcion();
    }

    public void marcarSpam(Contribuyente admin){
        estado.marcarSpam();
        this.administrador = admin;
    }

    public void anularMarcaSpam(){
    estado.anularMarcaSpam();
    }

    /////////////////////////////////////

    public void preescribirCosolicitudes(){
        SolicitudEliminacion[] cosolicitudes = this.hecho.getSolicitudesDeEliminacion();
        for(SolicitudEliminacion sol : cosolicitudes){
            sol.prescribir();
            // prescribir() chequea si esta pendiente y solamente si lo está, prescribe
        }
    }

    public void anularPrescripcionCosolicitudes(){
        SolicitudEliminacion[] cosolicitudes = this.hecho.getSolicitudesDeEliminacion();
        for(SolicitudEliminacion sol : cosolicitudes){
            sol.anularPrescripcion();
            // anularPrescripcion() chequea si esta prescripta y solamente si lo está, pasa a pendiente
        }
    }

    //////////////////////////////////////

        public Boolean hechoVisible(){
            return hecho.esVisible();
        }
        public void esconderHecho(){
            hecho.ocultar();
        }
        public void mostrarHecho(){
            hecho.mostrar();
        }

    //////////////////////////////////////


        public Boolean esSpam(){
                return new DetectorDeSpam().esSpam(this.motivo);
        }

    public String getMotivo() {
        return motivo;
    }

    public LocalDateTime getFecha_subida() {
        return fecha_subida;
    }

    public Contribuyente getSolicitante() {
        return solicitante;
    }

    public LocalDateTime getFecha_resolucion() {
        return fecha_resolucion;
    }

    public void setFecha_resolucion(LocalDateTime fecha_resolucion) {
        this.fecha_resolucion = fecha_resolucion;
    }

    public Contribuyente getAdministrador() {
        return administrador;
    }
}