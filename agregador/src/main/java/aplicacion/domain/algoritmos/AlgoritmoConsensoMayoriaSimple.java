package aplicacion.domain.algoritmos;

import aplicacion.domain.hechos.Hecho;

import java.util.*;

public class AlgoritmoConsensoMayoriaSimple extends AlgoritmoConsenso {
    @Override
    public List<Hecho> curarHechos(Map<Hecho, Long> cantidadPorHecho, Long totalFuentes) {
        int umbral = (int) Math.ceil(totalFuentes / 2.0);
        return cantidadPorHecho.entrySet().stream()
                .filter(e -> e.getValue() >= umbral)
                .map(Map.Entry::getKey)
                .toList();
    }
}