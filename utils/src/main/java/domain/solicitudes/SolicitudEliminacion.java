package domain.solicitudes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import domain.hechos.Hecho;
import domain.usuarios.Contribuyente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/// Entiendase por "cosolicitud": Solicitud de eliminacion que apunta al mismo hecho que la solicitud actual
///
/// Si una solicitud:
/// Esta pendiente: Se la puede rechazar(), aceptar() o marcarSpam() manualmente
///                 tambien se la puede prescribir(), pero eso lo envia la misma solicitud que pasa a aceptar (por ahora, puede que cambie si envia las prescripciones el hecho)
/// Esta prescripta: Se la puede rechazar() o marcarSpam() -> Pasa a Rechazada o MarcarSpam
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
@Entity
@NoArgsConstructor
@Getter @Setter
public class SolicitudEliminacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera un ID autoincremental
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Contribuyente solicitante;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estado_id")
    private EstadoSolicitud estado;
    @ManyToOne
    private Contribuyente administrador;
    @Getter
    private LocalDateTime fechaSubida;
    private LocalDateTime fechaResolucion;
    @ManyToOne
    private Hecho hecho;
    private String motivo;

    @JsonCreator
    public SolicitudEliminacion
            (@JsonProperty("solicitante") Contribuyente solicitante,
             @JsonProperty("hecho") Hecho hecho,
             @JsonProperty("motivo") String motivo) {
        this.motivo = motivo;

        if (this.esSpam()){
            this.estado = new EstadoSolicitudSpam();
        }else{
            if(hecho.esVisible()){
                this.estado = new EstadoSolicitudPendiente();
            }else{
                this.estado = new EstadoSolicitudPrescripta();
                // Si por algun motivo "llega tarde" una solicitud y ya se elimino el hecho
            }
        }

        this.solicitante = solicitante;
        this.administrador = null;
        this.fechaSubida = LocalDateTime.now();
        this.fechaResolucion = null;
        this.hecho = hecho;
        hecho.agregarASolicitudes(this);
        // Le manda mensaje a su hecho para que lo agregue
        // IMPORTANTE: debe estar cargado el hecho en memoria
    }

    /////////////////////////////////////

    public void aceptar(Contribuyente admin){
        estado.aceptar(this);
        this.administrador = admin;
    }

    public void anularAceptacion(){
        estado.anularAceptacion(this);
    }

    public void rechazar(Contribuyente admin){
        estado.rechazar(this);
        this.administrador = admin;
    }

    public void anularRechazo(){
        estado.anularRechazo(this);
    }

    public void prescribir(){
        estado.prescribir(this);
    }

    public void anularPrescripcion(){
        estado.anularPrescripcion(this);
    }

    public void marcarSpam(Contribuyente admin){
        estado.marcarSpam(this);
        this.administrador = admin;
    }

    public void anularMarcaSpam(){
    estado.anularMarcaSpam(this);
    }

    /////////////////////////////////////
    ///
    public void preescribirCosolicitudes(){
        hecho.prescribirSolicitudes();
        // Redirige del estado que la llama al hecho, ya que el estado no conoce el hecho pero le quiere mandar un mensake a él
    }

    public void anularPrescripcionCosolicitudes(){
        hecho.anularPrescripcionSolicitudes();
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
        return new DetectorDeSpamPrueba().esSpam(this.getMotivo());
    }

}