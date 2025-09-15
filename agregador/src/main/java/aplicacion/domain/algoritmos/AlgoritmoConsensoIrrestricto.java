package aplicacion.domain.algoritmos;

import aplicacion.domain.hechos.Hecho;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlgoritmoConsensoIrrestricto extends AlgoritmoConsenso {
    @Override
    public List<Hecho> curarHechos(Map<Hecho, Long> cantidadPorHecho, Long totalFuentes) {
        return new ArrayList<>(cantidadPorHecho.keySet());
    }
}