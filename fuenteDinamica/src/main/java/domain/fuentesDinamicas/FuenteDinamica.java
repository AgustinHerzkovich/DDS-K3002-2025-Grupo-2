package domain.fuentesDinamicas;


// FUENTE DINAMICA
public class FuenteDinamica extends Fuente {
    private Long id;

    public FuenteDinamica(Long id) {
        super(id);
    }
/*
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
    }*/ //TODO: Mover esto al service
}