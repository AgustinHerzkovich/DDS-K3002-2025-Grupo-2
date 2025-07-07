package domain.criterios;

import domain.hechos.Hecho;
import domain.hechos.Ubicacion;

// CRITERIO DE DISTANCIA
public class CriterioDeDistancia implements CriterioDePertenencia {
    private final Ubicacion ubicacionbase;
    private final Double distanciaMinima;

    public CriterioDeDistancia(Ubicacion ubicacionbase, Double distanciaMinima) {
        this.ubicacionbase = ubicacionbase;
        this.distanciaMinima = distanciaMinima;
    }

    @Override
    public Boolean cumpleCriterio(Hecho hecho){
        Ubicacion ubicacionHecho = hecho.getUbicacion();
        return ubicacionbase.distanciaA(ubicacionHecho) >= distanciaMinima;
    }
}
