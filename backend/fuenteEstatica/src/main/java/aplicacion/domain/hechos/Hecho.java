package aplicacion.domain.hechos;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// HECHO
@NoArgsConstructor
@Getter
public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Origen origen;

    public Hecho(String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDateTime fechaAcontecimiento, Origen origen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = new Ubicacion(latitud, longitud);
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = LocalDateTime.now();
        this.origen = origen;
    }
}
