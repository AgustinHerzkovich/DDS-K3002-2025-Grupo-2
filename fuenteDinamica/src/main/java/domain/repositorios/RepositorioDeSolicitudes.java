package domain.repositorios;

import domain.solicitudes.SolicitudEliminacion;

import java.util.ArrayList;
import java.util.List;

// REPOSITORIO DE SOLICITUDES
public class RepositorioDeSolicitudes implements Repositorio<SolicitudEliminacion> {
    private List<SolicitudEliminacion> solicitudes;

    public RepositorioDeSolicitudes() {
        this.solicitudes = new ArrayList<SolicitudEliminacion>();
    }

    public void agregar(SolicitudEliminacion solicitud){
        // TODO
    }

    public void quitar(SolicitudEliminacion solicitud) {
        // TODO
    }

    public SolicitudEliminacion buscar(SolicitudEliminacion solicitud) {
        // TODO
    }

    public List<SolicitudEliminacion> listar(){
        // TODO
    }
}
