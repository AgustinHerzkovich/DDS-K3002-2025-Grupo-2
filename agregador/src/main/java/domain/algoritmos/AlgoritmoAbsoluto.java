package domain.algoritmos;

import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlgoritmoAbsoluto implements Algoritmo {
    @Override
    public List<Hecho> curarHechos(Map<Fuente, List<Hecho>> hechosPorFuente) {
        // Si el mapa está vacío, devolvemos una lista vacía
        if (hechosPorFuente.isEmpty()) {
            return List.of();
        }

        var iterador = hechosPorFuente.values().iterator();

        // Empezamos con el conjunto de títulos de la primera lista
        var interseccion = iterador.next().stream()
                .map(Hecho::getTitulo)
                .collect(Collectors.toSet());

        // Intersectamos con los títulos de las listas restantes
        while (iterador.hasNext()) {
            var titulos = iterador.next().stream()
                    .map(Hecho::getTitulo)
                    .collect(Collectors.toSet());
            interseccion.retainAll(titulos); // Retiene solo los títulos comunes
        }

        // Recopilamos los objetos Hecho cuyo título está en la intersección
        return hechosPorFuente.values().stream()
                .flatMap(List::stream)
                .filter(hecho -> interseccion.contains(hecho.getTitulo()))
                .distinct()
                .collect(Collectors.toList());
    }
}