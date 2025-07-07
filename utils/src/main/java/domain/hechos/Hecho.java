package domain.hechos;

import com.fasterxml.jackson.annotation.JsonProperty;
import domain.hechos.multimedias.Multimedia;
import domain.solicitudes.SolicitudEliminacion;
import domain.usuarios.IdentidadContribuyente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// HECHO
@Getter
@Setter
@Entity
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    @Embedded
    private Categoria categoria;
    @Embedded
    private Ubicacion ubicacion;
    @JsonProperty("fechaAcontecimiento")
    private LocalDate fecha_acontecimiento;
    private LocalDate fecha_carga;
    @OneToMany
    private List<SolicitudEliminacion> solicitudes;
    private LocalDate fecha_ultimaModificacion;
    @Embedded
    private Origen origen;
    private String contenido_texto;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Multimedia> contenido_multimedia;
    @ManyToMany
    private List<Etiqueta> etiquetas;
    private Boolean visible;
    private Boolean anonimato;
    @ManyToOne
    private IdentidadContribuyente autor;

    public Hecho() {} // Constructor vacio para que se pueda deserealizar el JSON

    public Hecho(String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDate fecha_acontecimiento, Origen origen, String contenido_texto, List<Multimedia> contenido_multimedia, Boolean anonimato, IdentidadContribuyente autor) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = new Ubicacion(latitud, longitud);
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
            this.ubicacion = ubicacion;
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

    public boolean contieneEtiqueta(Etiqueta etiqueta) {
        return etiquetas.contains(etiqueta);
    }

    public Boolean ocurrioEntre(LocalDate fecha_inicial, LocalDate fecha_final) {
        return ocurrioDespuesDe(fecha_inicial) && ocurrioAntesDe(fecha_final);
    }

    public void agregarASolicitudes(SolicitudEliminacion solicitud) {
        solicitudes.add(solicitud);
    }

    public void prescribirSolicitudes(){
        // Cuando se acepta una solicitud, todas las demas se prescriben (solo afecta las pendientes)
        for(SolicitudEliminacion sol : this.solicitudes){
            sol.prescribir();
        }
    }
    public void anularPrescripcionSolicitudes(){
        // Cuando se anula una solicitud aceptada, todas las demás se de-prescriben (Solo afecta a las prescriptas)
        for(SolicitudEliminacion sol : this.solicitudes){
            sol.anularPrescripcion();
        }
    }

    public Boolean ocurrioAntesDe(LocalDate fecha)
    {
        return fecha_acontecimiento.isBefore(fecha);
    }

    public Boolean ocurrioDespuesDe(LocalDate fecha)
    {
        return fecha_acontecimiento.isAfter(fecha);
    }

    public Boolean seCargoAntesDe(LocalDate fecha) {
        return fecha_carga.isBefore(fecha);
    }

    public Boolean seCargoDespuesDe(LocalDate fecha) {
        return fecha_carga.isAfter(fecha);
    }
}
