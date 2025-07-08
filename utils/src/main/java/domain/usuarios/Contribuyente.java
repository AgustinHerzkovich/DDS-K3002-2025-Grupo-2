package domain.usuarios;

import domain.solicitudes.SolicitudEliminacion;
import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


//CONTRIBUYENTE
@Entity
public class Contribuyente {
    @Id
    private String contribuyenteId;
    @Setter
    private Boolean esAdministrador;
    @OneToMany
    private List<IdentidadContribuyente> identidades;
    @OneToMany
    private List<SolicitudEliminacion> solicitudesEliminacion;

    public Contribuyente(String contribuyenteId, Boolean esAdministrador) {
        this.contribuyenteId = contribuyenteId;
        this.esAdministrador = esAdministrador;
        this.identidades = new ArrayList<>();
        this.solicitudesEliminacion = new ArrayList<>();
    }

    public Contribuyente() {

    }

    public void modificarIdentidad(String nombre, String apellido, LocalDateTime fechaNacimiento) {
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