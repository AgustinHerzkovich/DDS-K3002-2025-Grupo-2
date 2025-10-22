package aplicacion.domain.hechos;

import aplicacion.domain.hechos.multimedias.Multimedia;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

// HECHO
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(length = 200)
    private String titulo;
    @Column(length = 1000) // Le asigno VARCHAR(1000)
    private String descripcion;
    @ManyToOne(cascade = CascadeType.ALL)
    private Categoria categoria;
    @ManyToOne(cascade = CascadeType.ALL)
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaUltimaModificacion;
    @Enumerated(EnumType.STRING)
    private Origen origen;
    @Column(length = 500)
    private String contenidoTexto;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    // le dice a Hibernate que la FK va en Multimedia
    private List<Multimedia> contenidoMultimedia;
    private Boolean anonimato;
    @ManyToOne(cascade = CascadeType.ALL) // TODO: Cambiar en un futuro, habría que persistir antes el usuario?
    private Contribuyente autor; // TODO: Revisar como se persiste el autor
    @Enumerated(EnumType.STRING)
    private EstadoRevision estadoRevision;
    private String sugerencia;

    public Hecho(String titulo,
                 String descripcion,
                 Categoria categoria,
                 Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento,
                 Origen origen,
                 String contenidoTexto,
                 List<Multimedia> contenidoMultimedia,
                 Boolean anonimato,
                 Contribuyente autor) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.origen = origen;
        this.fechaCarga = LocalDateTime.now();
        this.fechaUltimaModificacion = this.fechaCarga;
        this.contenidoTexto = contenidoTexto;
        this.contenidoMultimedia = contenidoMultimedia;
        this.anonimato = anonimato;
        this.autor = anonimato ? null : autor;
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
        this.estadoRevision = EstadoRevision.PENDIENTE; // Cada vez que se edita, vuelve a estar pendiente de revisión
    }

    public boolean estaAceptado() {
        return this.estadoRevision == EstadoRevision.ACEPTADO;
    }
    public boolean estaPendiente() {
        return this.estadoRevision == EstadoRevision.PENDIENTE;
    }
}