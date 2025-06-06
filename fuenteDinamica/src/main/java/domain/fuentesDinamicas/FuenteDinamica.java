package domain.fuentesDinamicas;

import domain.hechos.Hecho;
import domain.repositorios.Repositorio;
import domain.solicitudes.SolicitudEliminacion;

import java.util.List;

// FUENTE DINAMICA
public class FuenteDinamica implements Fuente {
    private Repositorio repositorio;

    public FuenteDinamica(Repositorio repositorio){
        this.repositorio = repositorio;
    }

    public void agregarHecho(Hecho hecho){
        repositorio.agregar(hecho);
    }

    public void quitarHecho(Hecho hecho){
        repositorio.quitar(hecho);
    }

    public Hecho buscarHecho(Hecho hecho){
        return repositorio.buscar(hecho);
    }

    public List<Hecho> importarHechos() {
        return repositorio.listar();
    }

    public void agregarSolicitud(SolicitudEliminacion solicitud){
        repositorio.agregar(solicitud);
    }

    public void quitarSolicitud(SolicitudEliminacion solicitud){
        repositorio.quitar(solicitud);
    }

    public SolicitudEliminacion buscarSolicitud(SolicitudEliminacion solicitud){
        return repositorio.buscar(solicitud);
    }
}