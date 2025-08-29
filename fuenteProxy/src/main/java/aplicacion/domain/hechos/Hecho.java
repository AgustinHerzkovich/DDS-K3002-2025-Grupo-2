package aplicacion.domain.hechos;

import aplicacion.domain.hechos.multimedias.Multimedia;
import com.fasterxml.jackson.annotation.JsonCreator;
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
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @EqualsAndHashCode.Include
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
    private LocalDateTime fechaUltimaModificacion;
    @Enumerated(EnumType.STRING)
    private Origen origen;
    @EqualsAndHashCode.Include
    private String contenidoTexto;
    // todo: evaluar si es necesario el fetch type eager, es temporal para que pasen los tests
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER) // CascadeType.ALL permite que las operaciones de persistencia se propaguen a las entidades relacionadas
    @JoinColumn(name = "hecho_id") // le dice a Hibernate que la FK va en Multimedia
    @EqualsAndHashCode.Include
    private List<Multimedia> contenidoMultimedia;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Etiqueta> etiquetas;

    @JsonCreator
    public Hecho(@JsonProperty("titulo") String titulo,
                 @JsonProperty("descripcion") String descripcion,
                 @JsonProperty("categoria") Categoria categoria,
                 @JsonProperty("ubicacion") Ubicacion ubicacion,
                 @JsonProperty("fechaAcontecimiento") LocalDateTime fechaAcontecimiento,
                 @JsonProperty("origen") Origen origen,
                 @JsonProperty("contenidoTexto") String contenidoTexto,
                 @JsonProperty("contenidoMultimedia") List<Multimedia> contenidoMultimedia) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = LocalDateTime.now();
        this.fechaUltimaModificacion = this.fechaCarga;
        this.origen = origen;
        this.contenidoTexto = contenidoTexto;
        this.contenidoMultimedia = contenidoMultimedia;
        this.etiquetas = new ArrayList<>();
    }

    public Boolean tieneMismoTitulo(String otroTitulo) {
        return titulo.equals(otroTitulo);
    }
}