package domain.criterios;

import domain.hechos.Hecho;

// CRITERIO DE PERTENENCIA
public interface CriterioDePertenencia {
    public Boolean cumpleCriterio(Hecho hecho);
}