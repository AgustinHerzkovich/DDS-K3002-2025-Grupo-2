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
/// + boolean esSpam(String texto)
/// - void preescribirCosolicitudes()
/// - void anularPrescripcionCosolicitudes()


// TODO: Cuando se haga esto de la persistencia, hay que hacer que los cambios de estado de la solicitud se vean reflejados en el medio persistente

// SOLICITUD DE ELIMINACION
public class SolicitudEliminacion implements DetectorDeSpam{
    private Estado estado;
    private Contribuyente solicitante;
    private Contribuyente administrador;
    private LocalDateTime fecha_subida;
    private LocalDateTime fecha_resolucion;
    private Hecho hecho;
    private String motivo;

    /*
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public void setFechaResolucion(LocalDateTime fecha){
        this.fecha_resolucion = fecha;
    }

    */

    public void setAdministrador(Contribuyente administrador) { // La dejo como un metodo aparte por ahora, tal vez se lo podria integrar a aceptar(), rechazar(), etc.
        this.administrador = administrador;
    }

    public SolicitudEliminacion(Contribuyente solicitante, Hecho hecho, String motivo) {
        if(hecho.esVisible()){
            this.estado = Estado.PENDIENTE;
        }else{
            this.estado = Estado.PRESCRIPTA; // Si por algun motivo "llega tarde" una solicitud y ya se elimino el hecho
        }
        this.solicitante = solicitante;
        this.administrador = null;
        this.fecha_subida = LocalDateTime.now();
        this.fecha_resolucion = null;
        this.hecho = hecho;
        this.motivo = motivo;
        hecho.agregarASolicitudes(this); // Le manda mensaje a su hecho para que lo agregue (debe estar cargado en memoria)
        if (this.esSpam(motivo))
            this.marcarSpam();          // Soy consciente que si es SPAM se agrega y se desagrega al hecho, pero queda más legible así, por lo que me da igual
    }

    /////////////////////////////////////

    public void aceptar(){
        if (this.estado == Estado.PENDIENTE)
        {
            this.estado = Estado.ACEPTADA;
            this.fecha_resolucion = LocalDateTime.now();
            this.hecho.ocultar();
            this.preescribirCosolicitudes();
        }
        else
            System.out.print("Error: se intento aceptar una solicitud de eliminacion que no esta en pendiente");
    }

    public void anularAceptacion(){
        // Supongo que solo hay una aceptada a la vez
        // Sino se deberia anular verificar que no haya aceptadas antes de que se haga el mostrar()
        if(this.estado == Estado.ACEPTADA) {        // Solo si esta aceptada
            this.estado = Estado.PENDIENTE;
            this.fecha_resolucion = null;
            this.hecho.mostrar();                   //Muestro el hecho, ya que no esta mas eliminado
            this.anularPrescripcionCosolicitudes(); // Como ya no esta eliminada, actualizo el estado de las demas
        }
        else
            System.out.print("Error: se intento anular la aceptacion de una solicitud de eliminacion que no estaba aceptada");
    }

    //////////////////////////////////////

    public void rechazar(){
        if (this.estado == Estado.PENDIENTE){
            this.estado = Estado.RECHAZADA;
            this.fecha_resolucion = LocalDateTime.now();
        }
        else
            System.out.print("Error: se intento rechazar de una solicitud de eliminacion que no esta en pendiente");
    }

    public void anularRechazo(){
        // Supongo que se puede desRechazar una que igual tiene su hecho eliminado (por algun motivo)
        if(this.estado == Estado.RECHAZADA) { // Solo si esta rechazada:
            if (this.hecho.esVisible())
                this.estado = Estado.PENDIENTE;        // Si el hecho esta activo
            else //
                this.estado = Estado.PRESCRIPTA;         // Si el hecho esta eliminado
            this.fecha_resolucion = null;
        }
        else
            System.out.print("Error: se intento anular el rechazo de una solicitud de eliminacion que no estaba rechazada");
    }

    //////////////////////////////////////

    public void prescribir(){
        if (this.estado == Estado.PENDIENTE)
            this.estado = Estado.PRESCRIPTA;}

    public void anularPrescripcion(){
        if (this.estado  == Estado.PRESCRIPTA)
            this.estado = Estado.PENDIENTE;
        // Esta no tira error al usarse sin estar rechazada, ya que se la puede usar en masa (aplique o no)
    }

    //////////////////////////////////////

    public void marcarSpam(){
        if (this.estado == Estado.PENDIENTE)
        {
            this.estado = Estado.SPAM;
            hecho.eliminarDeSolicitudes(this);
        }
        else{
            System.out.print("Error: se intento marcar como SPAM una solicitud de eliminacion que no estaba pendiente");
        }
    }

    public void anularMarcaSpam(){
        if(this.estado == Estado.SPAM) {
            if (this.hecho.esVisible())
                this.estado = Estado.PENDIENTE;
            else
                this.estado = Estado.PRESCRIPTA;
            hecho.agregarASolicitudes(this);
        }
        else
            System.out.print("Error: se intento desmarcar como SPAM una solicitud de eliminacion que no era SPAM");
    }

    /////////////////////////////////////

    private void preescribirCosolicitudes(){
        cosolicitudes = this.hecho.getSolicitudesDeEliminacion();
        for(SolicitudEliminacion sol : cosolicitudes){
            sol.prescribir();
            // prescribir() chequea si esta pendiente y solamente si lo está, prescribe
        }
    }

    private void anularPrescripcionCosolicitudes(){
        cosolicitudes = this.hecho.getSolicitudesDeEliminacion();
        for(SolicitudEliminacion sol : cosolicitudes){
            sol.anularPrescripcion();
            // anularPrescripcion() chequea si esta prescripta y solamente si lo está, pasa a pendiente
        }
    }

    //////////////////////////////////////

    public Boolean esSpam(String texto){
        // TODO : NO SABEMOS AÚN CÓMO SE IMPLEMENTA
        if (texto.contains("wasd")) // PLACEHOLDER
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
}