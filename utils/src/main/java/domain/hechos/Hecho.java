package domain.hechos;

import domain.hechos.multimedias.Multimedia;
import domain.solicitudes.SolicitudEliminacion;
import domain.usuarios.IdentidadContribuyente;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// HECHO
@Getter
@Setter
@Entity
@EqualsAndHashCode
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // TODO: Cambiar por UUID jijojijo se jijean los jijolines
    private String titulo;
    private String descripcion;
    @Embedded
    private Categoria categoria;
    @Embedded
    private Ubicacion ubicacion;
    private LocalDateTime fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    @OneToMany
    private List<SolicitudEliminacion> solicitudes;
    private LocalDateTime fechaUltimaModificacion;
    @Embedded
    private Origen origen;
    private String contenidoTexto;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Multimedia> contenidoMultimedia;
    @ManyToMany
    private List<Etiqueta> etiquetas;
    private Boolean visible;
    private Boolean anonimato;
    @ManyToOne
    private IdentidadContribuyente autor;

    public Hecho() {} // Constructor vacio para que se pueda deserealizar el JSON

    public Hecho(String titulo, String descripcion, Categoria categoria, Double latitud, Double longitud, LocalDateTime fechaAcontecimiento, Origen origen, String contenidoTexto, List<Multimedia> contenidoMultimedia, Boolean anonimato, IdentidadContribuyente autor) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = new Ubicacion(latitud, longitud);
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = LocalDateTime.now();
        this.solicitudes = new ArrayList<>();
        this.fechaUltimaModificacion = this.fechaCarga;
        this.origen = origen;
        this.contenidoTexto = contenidoTexto;
        this.contenidoMultimedia = contenidoMultimedia;
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

    public void editar(String titulo, String descripcion, Categoria categoria, Ubicacion ubicacion, LocalDateTime fecha, String contenidoTexto, List<Multimedia> contenidoMultimedia) {
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
        if (contenidoTexto != null) {
            this.contenidoTexto = contenidoTexto;
        }
        if (contenidoMultimedia != null) {
            this.contenidoMultimedia = contenidoMultimedia;
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

    public Boolean seActualizoDespuesDe(LocalDateTime fecha) {
        return fechaUltimaModificacion.isAfter(fecha);
    }
}