package domain.hechos;

import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// HECHO
public class Hecho {
    @Getter
    private String titulo;
    @Getter private String descripcion;
    @Getter private Categoria categoria;
    @Getter private Ubicacion ubicacion;
    @Getter private LocalDate fechaAcontecimiento;
    private LocalDate fechaCarga;
    private Origen origen;
    private List<Etiqueta> etiquetas;
    private Boolean visible;

    public Hecho() {} // Constructor vacio para que se pueda deserealizar el JSON

    public Hecho(String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDate fechaAcontecimiento, Origen origen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = new Ubicacion(latitud, longitud);
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = LocalDate.now();
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

    public void editar(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDate fecha) {
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

    public Boolean ocurrioEntre(LocalDate fechaInicial, LocalDate fechaFinal) {
        return ocurrioDespuesDe(fechaInicial) && ocurrioAntesDe(fechaFinal);
    }

    public Boolean ocurrioAntesDe(LocalDate fecha)
    {
        return fechaAcontecimiento.isBefore(fecha);
    }

    public Boolean ocurrioDespuesDe(LocalDate fecha)
    {
        return fechaAcontecimiento.isAfter(fecha);
    }

    public Boolean seCargoAntesDe(LocalDate fecha) {
        return fechaCarga.isBefore(fecha);
    }

    public Boolean seCargoDespuesDe(LocalDate fecha) {
        return fechaCarga.isAfter(fecha);
    }
}
