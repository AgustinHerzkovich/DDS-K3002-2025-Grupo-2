package domain.Hechos;

import domain.Multimedias.Multimedia;
import domain.Usuarios.IdentidadContribuyente;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


// HECHO
public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion_acontecimiento;
    private LocalDateTime fecha_acontecimiento;
    private LocalDateTime fecha_carga;
    private LocalDateTime fecha_ultimaModificacion;
    private Origen origen;
    private String contenido_texto;
    private Optional<List<Multimedia>> contenido_multimedia;
    private List<Etiqueta> etiquetas;
    private Boolean visible;
    private Boolean anonimato;
    private IdentidadContribuyente autor;

    public Hecho(String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDateTime fecha_acontecimiento, Origen origen, String contenido_texto, List<Multimedia> contenido_multimedia, Boolean anonimato, IdentidadContribuyente autor) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion_acontecimiento = new Ubicacion(latitud, longitud);
        this.fecha_acontecimiento = fecha_acontecimiento;
        this.fecha_carga = LocalDateTime.now();
        this.fecha_ultimaModificacion = this.fecha_carga;
        this.origen = origen;
        this.contenido_texto = contenido_texto;
        this.contenido_multimedia = Optional.ofNullable(contenido_multimedia);
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

    public void ocultar() {
        visible = false;
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

    public Boolean ocurrioEntre(LocalDateTime fecha_inicial, LocalDateTime fecha_final) {
        return fecha_acontecimiento.isAfter(fecha_inicial) && fecha_acontecimiento.isBefore(fecha_final);
    }
}
