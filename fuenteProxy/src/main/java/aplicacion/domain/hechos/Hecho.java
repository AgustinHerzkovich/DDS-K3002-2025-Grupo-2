package aplicacion.domain.hechos;

import aplicacion.domain.hechos.multimedias.Multimedia;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// HECHO
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String titulo;
    private String descripcion;

    @ManyToOne
    private Categoria categoria;

    @ManyToOne
    private Ubicacion ubicacion;

    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaUltimaModificacion;

    @Enumerated(EnumType.STRING)
    private Origen origen;

    private String contenidoTexto;

    @ManyToMany
    private List<Multimedia> contenidoMultimedia;

    @ManyToMany
    private List<Etiqueta> etiquetas;


    public Hecho(String titulo,
                 String descripcion,
                 Categoria categoria,
                 Ubicacion ubicacion,
                 LocalDateTime fechaAcontecimiento,
                 Origen origen,
                 String contenidoTexto,
                 List<Multimedia> contenidoMultimedia) {
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