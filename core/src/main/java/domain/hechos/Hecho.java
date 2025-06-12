package domain.hechos;


import domain.hechos.multimedias.Multimedia;
import domain.solicitudes.SolicitudEliminacion;
import domain.usuarios.IdentidadContribuyente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


// HECHO
public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion_acontecimiento;
    private LocalDate fecha_acontecimiento;
    private LocalDate fecha_carga;
    private List<SolicitudEliminacion> solicitudes;
    private LocalDate fecha_ultimaModificacion;
    private Origen origen;
    private String contenido_texto;
    private List<Multimedia> contenido_multimedia;
    private List<Etiqueta> etiquetas;
    private Boolean visible;
    private Boolean anonimato;
    private IdentidadContribuyente autor;

    public Hecho(String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDate fecha_acontecimiento, Origen origen, String contenido_texto, List<Multimedia> contenido_multimedia, Boolean anonimato, IdentidadContribuyente autor) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion_acontecimiento = new Ubicacion(latitud, longitud);
        this.fecha_acontecimiento = fecha_acontecimiento;
        this.fecha_carga = LocalDate.now();
        this.solicitudes = new ArrayList<>();
        this.fecha_ultimaModificacion = this.fecha_carga;
        this.origen = origen;
        this.contenido_texto = contenido_texto;
        this.contenido_multimedia = contenido_multimedia;
        this.etiquetas = new ArrayList<>();
        this.visible = true;
        this.anonimato = anonimato;
        if (anonimato) {
            this.autor = autor;
        }
        else {
            this.autor = null;
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public LocalDate getFechaAcontecimiento() {
        return fecha_acontecimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void ocultar() {
        visible = false;
    }

    public Boolean esVisible() {
        return visible;
    }

    public void mostrar() { visible = true; }

    public void editar(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDate fecha, String contenido_texto, List<Multimedia> contenido_multimedia) {
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
            this.ubicacion_acontecimiento = ubicacion;
        }
        if (fecha != null) {
            this.fecha_acontecimiento = fecha;
        }
        if (contenido_texto != null) {
            this.contenido_texto = contenido_texto;
        }
        if (contenido_multimedia != null) {
            this.contenido_multimedia = contenido_multimedia;
        }
    }

    public Boolean tieneMismoTitulo(String otro_titulo) {
        return titulo.equals(otro_titulo);
    }

    public void etiquetar(Etiqueta etiqueta) {
        etiquetas.add(etiqueta);
    }

    public Ubicacion getUbicacion() {
        return ubicacion_acontecimiento;
    }

    public boolean contieneEtiqueta(Etiqueta etiqueta) {
        return etiquetas.contains(etiqueta);
    }

    public Boolean ocurrioEntre(LocalDate fecha_inicial, LocalDate fecha_final) {
        return fecha_acontecimiento.isAfter(fecha_inicial) && fecha_acontecimiento.isBefore(fecha_final);
    }

    public void agregarASolicitudes(SolicitudEliminacion solicitud) {
        solicitudes.add(solicitud);
    }
}
