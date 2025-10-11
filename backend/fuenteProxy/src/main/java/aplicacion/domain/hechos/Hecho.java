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

    @ManyToOne(cascade = CascadeType.ALL)
    private Categoria categoria;

    @ManyToOne(cascade = CascadeType.ALL)
    private Ubicacion ubicacion;

    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaUltimaModificacion;

    @Enumerated(EnumType.STRING)
    private Origen origen;

    private String contenidoTexto;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private List<Multimedia> contenidoMultimedia;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
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