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
        solicitudes.add(solicitud);
    }

    public void quitar(SolicitudEliminacion solicitud) {
        solicitudes.remove(solicitud);
    }

    public SolicitudEliminacion buscar(SolicitudEliminacion solicitud) {
        return solicitudes.stream().filter(h -> h.equals(solicitud)).findFirst().orElse(null);
    }

    public List<SolicitudEliminacion> listar(){
        return solicitudes;
    }
}
