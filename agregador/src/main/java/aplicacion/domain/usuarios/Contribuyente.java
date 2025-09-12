package aplicacion.domain.usuarios;

import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.solicitudes.SolicitudEliminacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

//CONTRIBUYENTE
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Contribuyente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean esAdministrador;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private IdentidadContribuyente identidad;
    @OneToMany(mappedBy = "solicitante", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SolicitudEliminacion> solicitudesEliminacion;
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Hecho> hechosContribuidos;

    public Contribuyente(Boolean esAdministrador, IdentidadContribuyente identidad) {
        this.esAdministrador = esAdministrador;
        this.identidad = identidad;
        this.solicitudesEliminacion = new ArrayList<>();
        this.hechosContribuidos = new ArrayList<>();
    }

    public void agregarSolicitudEliminacion(SolicitudEliminacion solicitud) {
        solicitudesEliminacion.add(solicitud);
    }

    public void agregarHechoContribuido(Hecho hecho) { this.hechosContribuidos.add(hecho); }
}