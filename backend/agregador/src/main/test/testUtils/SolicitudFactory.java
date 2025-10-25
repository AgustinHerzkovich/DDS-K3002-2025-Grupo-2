package testUtils;

import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.solicitudes.SolicitudEliminacion;

public class SolicitudFactory {
    public SolicitudEliminacion generarSolicitudAleatoria(Hecho hecho){
        return new SolicitudEliminacion(ContribuyenteFactory.crearContribuyenteAleatorio(), hecho, RandomThingsGenerator.generarMotivoEliminacionLargo());
    }
}
