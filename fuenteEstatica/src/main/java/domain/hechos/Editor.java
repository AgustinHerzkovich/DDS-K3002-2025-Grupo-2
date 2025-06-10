package domain.hechos;

import domain.hechos.multimedias.Multimedia;

import java.time.LocalDateTime;
import java.util.List;

// EDITOR
public class Editor{
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion_acontecimiento;
    private LocalDateTime fecha_acontecimiento;
    private String contenido_texto;
    private List<Multimedia> contenido_multimedia;

    public Editor(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion_acontecimiento, LocalDateTime fecha_acontecimiento, String contenido_texto, List<Multimedia> contenido_multimedia){
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion_acontecimiento = ubicacion_acontecimiento;
        this.fecha_acontecimiento = fecha_acontecimiento;
        this.contenido_texto = contenido_texto;
        this.contenido_multimedia = contenido_multimedia;
    }

    public LocalDateTime getFechaAcontecimiento() {
        return fecha_acontecimiento;
    }

    public Ubicacion getUbicacionAcontecimiento() {
        return ubicacion_acontecimiento;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenidoTexto() {
        return contenido_texto;
    }

    public List<Multimedia> getContenidoMultimedia() {
        return contenido_multimedia;
    }
}