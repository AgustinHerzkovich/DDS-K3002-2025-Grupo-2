package domain.usuarios;

import domain.solicitudes.SolicitudEliminacion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//CONTRIBUYENTE
public class Contribuyente {
    private String contribuyente_id;
    private Boolean es_administrador;
    private List<IdentidadContribuyente> identidades;
    private List<SolicitudEliminacion> solicitudes_eliminacion;

    public Contribuyente(String contribuyente_id, Boolean es_administrador) {
        this.contribuyente_id = contribuyente_id;
        this.es_administrador = es_administrador;
        this.identidades = new ArrayList<>();
        this.solicitudes_eliminacion = new ArrayList<>();
    }

    public void modificarIdentidad(String nombre, String apellido, LocalDate fecha_nacimiento) {
        IdentidadContribuyente nueva_identidad = new IdentidadContribuyente(nombre, apellido, fecha_nacimiento, this);
        this.agregarIdentidad(nueva_identidad);
    }

    public IdentidadContribuyente getUltimaIdentidad() {
        return identidades.getLast();
    }

    public void setAdministrador(Boolean admin) {
        this.es_administrador = admin;
    }

    public void agregarIdentidad(IdentidadContribuyente identidad){
        this.identidades.add(identidad);
    }

    public void agregarSolicitudEliminacion(SolicitudEliminacion solicitud) {
        this.solicitudes_eliminacion.add(solicitud);
    }
}