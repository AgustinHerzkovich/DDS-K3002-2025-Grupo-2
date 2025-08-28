package aplicacion.domain.hechos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import aplicacion.domain.hechos.multimedias.Multimedia;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

// HECHO
@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Solo se incluyen los minimos para diferenciar a dos hechos. Fechas de cargas, solicitudes de eliminacion y otras cosas no
@NoArgsConstructor
@AllArgsConstructor
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @EqualsAndHashCode.Include
    private String titulo;
    @Column(length = 1000) // Le asigno VARCHAR(1000)
    @EqualsAndHashCode.Include
    private String descripcion;
    @Embedded
    @EqualsAndHashCode.Include
    private Categoria categoria;
    @Embedded
    @EqualsAndHashCode.Include
    private Ubicacion ubicacion;
    @EqualsAndHashCode.Include
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaUltimaModificacion;
    @EqualsAndHashCode.Include
    private String contenidoTexto;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) // CascadeType.ALL permite que las operaciones de persistencia se propaguen a las entidades relacionadas
    @JoinColumn(name = "hecho_id") // le dice a Hibernate que la FK va en Multimedia
    @EqualsAndHashCode.Include
    private List<Multimedia> contenidoMultimedia;
    private Boolean anonimato;
    @ManyToOne(cascade = CascadeType.ALL) // TODO: Cambiar en un futuro, habría que persistir antes el usuario?
    private IdentidadContribuyente autor; // TODO: Revisar como se persiste el autor
    @Enumerated(EnumType.STRING)
    private EstadoRevision estadoRevision;
    private String sugerencia;

    @JsonCreator
    public Hecho(@JsonProperty("titulo") String titulo,
                 @JsonProperty("descripcion") String descripcion,
                 @JsonProperty("categoria") Categoria categoria,
                 @JsonProperty("ubicacion") Ubicacion ubicacion,
                 @JsonProperty("fechaAcontecimiento") LocalDateTime fechaAcontecimiento,
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
        this.fechaUltimaModificacion = this.fechaCarga;
        this.contenidoTexto = contenidoTexto;
        this.contenidoMultimedia = contenidoMultimedia;
        this.anonimato = anonimato;
        this.autor = anonimato ? autor : null;
        this.estadoRevision = EstadoRevision.PENDIENTE;
        this.sugerencia = null;
    }

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
        this.setFechaUltimaModificacion(LocalDateTime.now()); // Se auto-updatea la fecha de última edición
    }
}