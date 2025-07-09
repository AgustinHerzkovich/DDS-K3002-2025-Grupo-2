package domain.fuentesDinamicas;

import lombok.Getter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// FUENTE DINAMICA
public class FuenteDinamica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

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