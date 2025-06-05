package domain.Hechos;

import domain.Multimedias.Multimedia;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// EDITOR
//@Getter
public class Editor{
    private Optional<String> titulo;
    private Optional<String> descripcion;
    private Optional<Categoria> categoria;
    private Optional<Ubicacion> ubicacion_acontecimiento;
    private Optional<LocalDateTime> fecha_acontecimiento;
    private Optional<String> contenido_texto;
    private Optional<List<Multimedia>> contenido_multimedia;

    public Editor(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion_acontecimiento, LocalDateTime fecha_acontecimiento, String contenido_texto, List<Multimedia> contenido_multimedia){
        this.titulo = Optional.ofNullable(titulo);
        this.descripcion = Optional.ofNullable(descripcion);
        this.categoria = Optional.ofNullable(categoria);
        this.ubicacion_acontecimiento = Optional.ofNullable(ubicacion_acontecimiento);
        this.fecha_acontecimiento = Optional.ofNullable(fecha_acontecimiento);
        this.contenido_texto = Optional.ofNullable(contenido_texto);
        this.contenido_multimedia = Optional.ofNullable(contenido_multimedia);
    }

    public Optional<LocalDateTime> getFechaAcontecimiento() {
        return fecha_acontecimiento;
    }

    public Optional<Ubicacion> getUbicacionAcontecimiento() {
        return ubicacion_acontecimiento;
    }

    public Optional<Categoria> getCategoria() {
        return categoria;
    }

    public Optional<String> getDescripcion() {
        return descripcion;
    }


    public Optional<String> getTitulo() {
        return titulo;
    }

    public Optional<String> getContenidoTexto() {
        return contenido_texto;
    }

    public Optional<List<Multimedia>> getContenidoMultimedia() {
        return contenido_multimedia;
    }
}