package domain.criterios;

import domain.hechos.Hecho;
import domain.hechos.Ubicacion;

// CRITERIO DE DISTANCIA
public class CriterioDeDistancia implements CriterioDePertenencia {
    private Ubicacion ubicacionBase;
    private Double distanciaMinima;

    public CriterioDeDistancia(Ubicacion ubicacionBase, Double distanciaMinima) {
        this.ubicacionBase = ubicacionBase;
        this.distanciaMinima = distanciaMinima;
    }

    @Override
    public Boolean cumpleCriterio(Hecho hecho){
        Ubicacion ubicacion_hecho = hecho.getUbicacion();
        return ubicacionBase.distanciaA(ubicacion_hecho) >= distanciaMinima;
    }
}
