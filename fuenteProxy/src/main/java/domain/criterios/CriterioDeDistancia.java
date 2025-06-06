package domain.criterios;

import domain.hechos.Hecho;
import domain.hechos.Ubicacion;

// CRITERIO DE DISTANCIA
public class CriterioDeDistancia implements CriterioDePertenencia {
    private Ubicacion ubicacion_base;
    private Double distancia_minima;

    public CriterioDeDistancia(Ubicacion ubicacion_base,  Double distancia_minima) {
        this.ubicacion_base = ubicacion_base;
        this.distancia_minima = distancia_minima;
    }

    @Override
    public Boolean cumpleCriterio(Hecho hecho){
        Ubicacion ubicacion_hecho = hecho.getUbicacion();
        return ubicacion_base.distanciaA(ubicacion_hecho) >= distancia_minima;
    }
}
