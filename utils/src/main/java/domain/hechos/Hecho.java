package domain.hechos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import domain.hechos.multimedias.Multimedia;
import domain.solicitudes.SolicitudEliminacion;
import domain.usuarios.IdentidadContribuyente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// HECHO
@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Hecho {
    @Id
    private String id;
    private String titulo;
    private String descripcion;
    @Embedded
    private Categoria categoria;
    @Embedded
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    @OneToMany(mappedBy = "hecho") // Indica que SolicitudEliminacion es el dueño de la relación bidireccional
    @JsonIgnore
    private List<SolicitudEliminacion> solicitudes;
    private LocalDateTime fechaUltimaModificacion;
    @Enumerated(EnumType.STRING)
    private Origen origen;
    private String contenidoTexto;
    // todo: quitar el fetch type eager, es temporal
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) // CascadeType.ALL permite que las operaciones de persistencia se propaguen a las entidades relacionadas
    @JoinColumn(name = "hecho_id") // le dice a Hibernate que la FK va en Multimedia
    private List<Multimedia> contenidoMultimedia;
    @ManyToMany
    @JsonIgnore
    private List<Etiqueta> etiquetas;
    private Boolean visible;
    private Boolean anonimato;
    @ManyToOne(cascade = CascadeType.PERSIST) // TODO: Cambiar en un futuro, habría que persistir antes el usuario?
    private IdentidadContribuyente autor; // TODO: Revisar como se persiste el autor

    @JsonCreator
    public Hecho(@JsonProperty("titulo") String titulo,
                 @JsonProperty("descripcion") String descripcion,
                 @JsonProperty("categoria") Categoria categoria,
                 @JsonProperty("ubicacion") Ubicacion ubicacion,
                 @JsonProperty("fechaAcontecimiento") LocalDateTime fechaAcontecimiento,
                 @JsonProperty("origen") Origen origen,
                 @JsonProperty("contenidoTexto") String contenidoTexto,
                 @JsonProperty("contenidoMultimedia") List<Multimedia> contenidoMultimedia,
                 @JsonProperty("anonimato") Boolean anonimato,
                 @JsonProperty("autor") IdentidadContribuyente autor) {
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = LocalDateTime.now();
        this.solicitudes = new ArrayList<>();
        this.fechaUltimaModificacion = this.fechaCarga;
        this.origen = origen;
        this.contenidoTexto = contenidoTexto;
        this.contenidoMultimedia = contenidoMultimedia;
        this.etiquetas = new ArrayList<>();
        this.visible = true;
        this.anonimato = anonimato;
        this.autor = anonimato ? autor : null;
    }

    public void ocultar() {
        visible = false;
    }

    public Boolean esVisible() {
        return visible;
    }

    public void mostrar() { visible = true; }

    public void editar(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDateTime fecha, String contenidoTexto, List<Multimedia> contenidoMultimedia) {
        if (titulo != null) {
            this.titulo = titulo;
        }
        if (descripcion != null) {
            this.descripcion = descripcion;
        }
        if (categoria != null) {
            this.categoria = categoria;
        }
        if (ubicacion != null) {
            this.ubicacion = ubicacion;
        }
        if (fecha != null) {
            this.fechaAcontecimiento = fecha;
        }
        if (contenidoTexto != null) {
            this.contenidoTexto = contenidoTexto;
        }
        if (contenidoMultimedia != null) {
            this.contenidoMultimedia = contenidoMultimedia;
        }
    }

    public Boolean tieneMismoTitulo(String otroTitulo) {
        return titulo.equals(otroTitulo);
    }

    public void etiquetar(Etiqueta etiqueta) {
        etiquetas.add(etiqueta);
    }

    public boolean contieneEtiqueta(Etiqueta etiqueta) {
        return etiquetas.contains(etiqueta);
    }

    public Boolean ocurrioEntre(LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        return ocurrioDespuesDe(fechaInicial) && ocurrioAntesDe(fechaFinal);
    }

    public void agregarASolicitudes(SolicitudEliminacion solicitud) {
        solicitudes.add(solicitud);
    }

    public void prescribirSolicitudes(){
        // Cuando se acepta una solicitud, todas las demas se prescriben (solo afecta las pendientes)
        for(SolicitudEliminacion sol : this.solicitudes){
            sol.prescribir();
        }
    }
    public void anularPrescripcionSolicitudes(){
        // Cuando se anula una solicitud aceptada, todas las demás se de-prescriben (Solo afecta a las prescriptas)
        for(SolicitudEliminacion sol : this.solicitudes){
            sol.anularPrescripcion();
        }
    }

    public Boolean ocurrioAntesDe(LocalDateTime fecha)
    {
        return fechaAcontecimiento.isBefore(fecha);
    }

    public Boolean ocurrioDespuesDe(LocalDateTime fecha)
    {
        return fechaAcontecimiento.isAfter(fecha);
    }

    public Boolean seCargoAntesDe(LocalDateTime fecha) {
        return fechaCarga.isBefore(fecha);
    }

    public Boolean seCargoDespuesDe(LocalDateTime fecha) {
        return fechaCarga.isAfter(fecha);
    }

    public Boolean seActualizoDespuesDe(LocalDateTime fecha) {
        return fechaUltimaModificacion.isAfter(fecha);
    }
}