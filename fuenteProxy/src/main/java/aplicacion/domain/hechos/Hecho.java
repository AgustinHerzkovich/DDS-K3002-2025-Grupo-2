package aplicacion.domain.hechos;

import aplicacion.domain.hechos.multimedias.Multimedia;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// HECHO
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaUltimaModificacion;
    private Origen origen;
    private String contenidoTexto;
    private List<Multimedia> contenidoMultimedia;
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