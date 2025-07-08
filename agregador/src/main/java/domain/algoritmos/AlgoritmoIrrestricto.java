package domain.algoritmos;

import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlgoritmoIrrestricto implements Algoritmo{
    @Override
    public List<Hecho> curarHechos(Map<Fuente, List<Hecho>> hechosPorFuente) {
        return hechosPorFuente.values().stream() // obtenemos todas las listas de hechos
                .flatMap(List::stream)  // las combinamos en un solo stream
                .distinct()             // eliminamos objetos duplicados (basado en equals y hashCode)
                .collect(Collectors.toList()); // los convertimos en lista
    }

}