package domain.usuarios;

import domain.solicitudes.SolicitudEliminacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//CONTRIBUYENTE
@Entity
@NoArgsConstructor
public class Contribuyente {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contribuyenteId;
    @Setter
    private Boolean esAdministrador;
    @OneToMany(mappedBy = "contribuyente")
    private List<IdentidadContribuyente> identidades;
    @OneToMany(mappedBy = "solicitante")
    private List<SolicitudEliminacion> solicitudesEliminacion;

    public Contribuyente(Long contribuyenteId, Boolean esAdministrador) {
        this.contribuyenteId = contribuyenteId;
        this.esAdministrador = esAdministrador;
        this.identidades = new ArrayList<>();
        this.solicitudesEliminacion = new ArrayList<>();
    }

    public void modificarIdentidad(String nombre, String apellido, LocalDate fechaNacimiento) {
        IdentidadContribuyente nueva_identidad = new IdentidadContribuyente(nombre, apellido, fechaNacimiento, this);
        this.agregarIdentidad(nueva_identidad);
    }

    public IdentidadContribuyente getUltimaIdentidad() {
        return identidades.getLast();
    }

    public void agregarIdentidad(IdentidadContribuyente identidad){
        this.identidades.add(identidad);
    }

    public void agregarSolicitudEliminacion(SolicitudEliminacion solicitud) {
        this.solicitudesEliminacion.add(solicitud);
    }
}