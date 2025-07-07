package domain.fuentesDinamicas;

import domain.hechos.Hecho;
import domain.repositorios.Repositorio;
import domain.repositorios.RepositorioDeHechos;
import domain.repositorios.RepositorioDeSolicitudes;
import domain.solicitudes.SolicitudEliminacion;

import java.util.List;

// FUENTE DINAMICA
public class FuenteDinamica extends Fuente {
    private Long id;
    private final Repositorio<Hecho> repositorioHechos;
    private final Repositorio<SolicitudEliminacion> repositorioSolicitudes;

    public FuenteDinamica(RepositorioDeHechos repositorioHechos, RepositorioDeSolicitudes repositorioSolicitudes, Long id) {
        super(id);
        this.repositorioHechos = repositorioHechos;
        this.repositorioSolicitudes = repositorioSolicitudes;
    }

    public void agregarHecho(Hecho hecho){
        repositorioHechos.agregar(hecho);
    }

    public void quitarHecho(Hecho hecho){
        repositorioHechos.quitar(hecho);
    }

    public Hecho buscarHecho(Hecho hecho){
        return repositorioHechos.buscar(hecho);
    }

    public List<Hecho> importarHechos() {
        return repositorioHechos.listar();
    }

   public void agregarSolicitud(SolicitudEliminacion solicitud){
        repositorioSolicitudes.agregar(solicitud);
    }

    public void quitarSolicitud(SolicitudEliminacion solicitud){
        repositorioSolicitudes.quitar(solicitud);
    }

    public SolicitudEliminacion buscarSolicitud(SolicitudEliminacion solicitud){
        return repositorioSolicitudes.buscar(solicitud);
    }

    public List<SolicitudEliminacion> buscarSolicitudes() {
        return repositorioSolicitudes.listar();
    }
}