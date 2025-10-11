package aplicacion.domain.algoritmos;

import aplicacion.domain.hechos.Hecho;

import java.util.*;


public class AlgoritmoConsensoAbsoluto implements AlgoritmoConsenso {
    public List<Hecho> curarHechos(Map<Hecho, Long> cantidadPorHecho, Long totalFuentes) {
        return cantidadPorHecho.entrySet().stream()
                .filter(e -> Objects.equals(e.getValue(), totalFuentes))
                .map(Map.Entry::getKey)
                .toList();
    }
}