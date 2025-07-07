package domain.usuarios;

import domain.solicitudes.SolicitudEliminacion;
import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//CONTRIBUYENTE
@Entity
public class Contribuyente {
    @Id
    private String contribuyente_id;
    @Setter
    private Boolean es_administrador;
    @OneToMany
    private List<IdentidadContribuyente> identidades;
    @OneToMany
    private List<SolicitudEliminacion> solicitudes_eliminacion;

    public Contribuyente(String contribuyente_id, Boolean es_administrador) {
        this.contribuyente_id = contribuyente_id;
        this.es_administrador = es_administrador;
        this.identidades = new ArrayList<>();
        this.solicitudes_eliminacion = new ArrayList<>();
    }

    public Contribuyente() {

    }

    public void modificarIdentidad(String nombre, String apellido, LocalDate fecha_nacimiento) {
        IdentidadContribuyente nueva_identidad = new IdentidadContribuyente(nombre, apellido, fecha_nacimiento, this);
        this.agregarIdentidad(nueva_identidad);
    }

    public IdentidadContribuyente getUltimaIdentidad() {
        return identidades.getLast();
    }

    public void agregarIdentidad(IdentidadContribuyente identidad){
        this.identidades.add(identidad);
    }

    public void agregarSolicitudEliminacion(SolicitudEliminacion solicitud) {
        this.solicitudes_eliminacion.add(solicitud);
    }
}