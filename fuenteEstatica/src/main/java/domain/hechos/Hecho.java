package domain.hechos;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// HECHO
public class Hecho {
    @Getter
    private String titulo;
    @Getter private String descripcion;
    @Getter private Categoria categoria;
    @Getter private Ubicacion ubicacion;
    @Getter private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Origen origen;
    private List<Etiqueta> etiquetas;
    private Boolean visible;

    public Hecho() {} // Constructor vacio para que se pueda deserealizar el JSON

    public Hecho(String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDateTime fechaAcontecimiento, Origen origen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = new Ubicacion(latitud, longitud);
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = LocalDateTime.now();
        this.origen = origen;
        this.etiquetas = new ArrayList<>();
        this.visible = true;
    }

    public void ocultar() {
        visible = false;
    }

    public Boolean esVisible() {
        return visible;
    }

    public void mostrar() { visible = true; }

    public void editar(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDateTime fecha) {
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
}
