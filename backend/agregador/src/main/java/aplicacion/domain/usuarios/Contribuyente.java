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
    @Embedded
    private IdentidadContribuyente identidad;
    @OneToMany(mappedBy = "solicitante", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SolicitudEliminacion> solicitudesEliminacion;
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Hecho> hechosContribuidos;
    String mail;

    public Contribuyente(Boolean esAdministrador, IdentidadContribuyente identidad, String mail) {
        this.esAdministrador = esAdministrador;
        this.identidad = identidad;
        this.mail = mail;
        this.solicitudesEliminacion = new ArrayList<>();
        this.hechosContribuidos = new ArrayList<>();
    }

    public void agregarSolicitudEliminacion(SolicitudEliminacion solicitud) {
        solicitudesEliminacion.add(solicitud);
    }
}