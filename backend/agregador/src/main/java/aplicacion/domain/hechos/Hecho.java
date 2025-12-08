package aplicacion.domain.hechos;

import aplicacion.domain.solicitudes.SolicitudEliminacion;
import aplicacion.domain.usuarios.Contribuyente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// HECHO
@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Solo se incluyen los minimos para diferenciar a dos hechos. Fechas de cargas, solicitudes de eliminacion y otras cosas no
@NoArgsConstructor
@AllArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_hecho_titulo", columnList = "titulo")
        }
)
public class Hecho {
    @Id
    private String id;
    @EqualsAndHashCode.Include
    @Column(length = 200)
    private String titulo;
    @Column(length = 1000) // Le asigno VARCHAR(1000)
    @EqualsAndHashCode.Include
    private String descripcion;
    @ManyToOne
    @EqualsAndHashCode.Include
    private Categoria categoria;
    @Embedded
    @EqualsAndHashCode.Include
    private Ubicacion ubicacion;
    @EqualsAndHashCode.Include
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    @OneToMany(mappedBy = "hecho", fetch = FetchType.EAGER) // Indica que SolicitudEliminacion es el dueño de la relación bidireccional
    private List<SolicitudEliminacion> solicitudes;
    private LocalDateTime fechaUltimaModificacion;
    @Enumerated(EnumType.STRING)
    private Origen origen;
    @EqualsAndHashCode.Include
    @Column(length = 500)
    private String contenidoTexto;
    // todo: evaluar si es necesario el fetch type eager, es temporal para que pasen los tests
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER) // CascadeType.ALL permite que las operaciones de persistencia se propaguen a las entidades relacionadas
    @JoinColumn(name = "hecho_id") // le dice a Hibernate que la FK va en Multimedia
    private List<Multimedia> contenidoMultimedia;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "hecho_etiqueta",
            joinColumns = @JoinColumn(name = "hecho_id"),
            inverseJoinColumns = @JoinColumn(name = "etiqueta_id")
    )
    private List<Etiqueta> etiquetas;
    private Boolean visible;
    private Boolean anonimato;
    @ManyToOne
    private Contribuyente autor;

    public Hecho(String id,
                 String titulo,
                 String descripcion,
                 Categoria categoria,
                 Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento,
                 Origen origen,
                 String contenidoTexto,
                 List<Multimedia> contenidoMultimedia,
                 Boolean anonimato,
                 Contribuyente autor) {
        this.id = id != null ? id : java.util.UUID.randomUUID().toString(); // Si es null, genera UUID; si no, usa el proporcionado
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

    public void etiquetar(Etiqueta etiqueta) {
        etiquetas.add(etiqueta);
    }

    public boolean contieneEtiqueta(Etiqueta etiqueta) {
        return etiquetas.stream().anyMatch(et-> et.esIdenticaA(etiqueta.getNombre()));
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

    public String getClaveUnica() {
        return String.join("|",
                titulo,
                descripcion,
                categoria.getId().toString(),
                ubicacion.getLatitud().toString(),
                ubicacion.getLongitud().toString(),
                fechaAcontecimiento.toString(),
                Objects.toString(contenidoTexto, "")
        );
    }

    @Override
    public String toString(){
        return String.format("(Hecho){titulo: %s ; descripcion: %s ; id: %s}", titulo, descripcion, id);
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