package domain.fuentesDinamicas;

import domain.hechos.Hecho;
import domain.repositorios.Repositorio;
import domain.repositorios.RepositorioDeHechos;
import domain.repositorios.RepositorioDeSolicitudes;
import domain.solicitudes.SolicitudEliminacion;

import java.util.List;

// FUENTE DINAMICA
public class FuenteDinamica implements Fuente {
    private Repositorio<Hecho> repositorio_hechos;
    private Repositorio<SolicitudEliminacion> repositorio_solicitudes;

    public FuenteDinamica(RepositorioDeHechos repositorio_hechos, RepositorioDeSolicitudes repositorio_solicitudes) {
        this.repositorio_hechos = repositorio_hechos;
        this.repositorio_solicitudes = repositorio_solicitudes;
    }

    public void agregarHecho(Hecho hecho){
        repositorio_hechos.agregar(hecho);
    }

    public void quitarHecho(Hecho hecho){
        repositorio_hechos.quitar(hecho);
    }

    public Hecho buscarHecho(Hecho hecho){
        return repositorio_hechos.buscar(hecho);
    }

    public List<Hecho> importarHechos() {
        return repositorio_hechos.listar();
    }

   public void agregarSolicitud(SolicitudEliminacion solicitud){
        repositorio_solicitudes.agregar(solicitud);
    }

    public void quitarSolicitud(SolicitudEliminacion solicitud){
        repositorio_solicitudes.quitar(solicitud);
    }

    public SolicitudEliminacion buscarSolicitud(SolicitudEliminacion solicitud){
        return repositorio_solicitudes.buscar(solicitud);
    }
}