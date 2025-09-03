package aplicacion.domain.hechos;

import aplicacion.domain.hechos.multimedias.Multimedia;
import aplicacion.domain.solicitudes.SolicitudEliminacion;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// HECHO
@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Solo se incluyen los minimos para diferenciar a dos hechos. Fechas de cargas, solicitudes de eliminacion y otras cosas no
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Para que no aparezcan los atributos nulos en el JSON
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(length = 200)
    private String titulo;
    @Column(length = 1000) // Le asigno VARCHAR(1000)
    @EqualsAndHashCode.Include
    private String descripcion;
    @ManyToOne
    @EqualsAndHashCode.Include
    private Categoria categoria;
    @ManyToOne
    @EqualsAndHashCode.Include
    private Ubicacion ubicacion;
    @EqualsAndHashCode.Include
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    @OneToMany(mappedBy = "hecho", fetch = FetchType.EAGER) // Indica que SolicitudEliminacion es el dueño de la relación bidireccional
    @JsonManagedReference // Evita que se serialice la lista de solicitudes al convertir a JSON, para evitar ciclos infinitos
    private List<SolicitudEliminacion> solicitudes;
    private LocalDateTime fechaUltimaModificacion;
    @Enumerated(EnumType.STRING)
    private Origen origen;
    @EqualsAndHashCode.Include
    @Column(length = 500)
    private String contenidoTexto;
    // todo: evaluar si es necesario el fetch type eager, es temporal para que pasen los tests
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) // CascadeType.ALL permite que las operaciones de persistencia se propaguen a las entidades relacionadas
    @JoinColumn(name = "hecho_id") // le dice a Hibernate que la FK va en Multimedia
    private List<Multimedia> contenidoMultimedia;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Etiqueta> etiquetas;
    private Boolean visible;
    private Boolean anonimato;
    @ManyToOne(cascade = CascadeType.ALL) // TODO: Cambiar en un futuro, habría que persistir antes el usuario?
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
        this.anonimato = anonimato != null ? anonimato : true;
        this.autor = Boolean.TRUE.equals(anonimato) ? null : autor;
    }

    public void ocultar() {
        visible = false;
    }

    public Boolean esVisible() {
        return visible;
    }

    public void mostrar() { visible = true; }

    public Boolean tieneMismoTitulo(String otroTitulo) {
        return titulo.equals(otroTitulo);
    }

    public void etiquetar(Etiqueta etiqueta) {
        etiquetas.add(etiqueta);
    }

    public boolean contieneEtiqueta(Etiqueta etiqueta) {
        return etiquetas.contains(etiqueta);
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
}