package domain.algoritmos;

import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AlgoritmoMayoriaSimple implements Algoritmo{
    @Override
    public List<Hecho> curarHechos(Map<Fuente, List<Hecho>> hechosPorFuente) {
        if (hechosPorFuente.isEmpty()) {
            return List.of();
        }

        int totalFuentes = hechosPorFuente.size();
        int umbral = (int) Math.ceil(totalFuentes / 2.0); // al menos la mitad

        // Contar cuántas fuentes tienen cada título
        Map<String, Integer> conteoTitulos = new HashMap<>();

        for (List<Hecho> hechos : hechosPorFuente.values()) {
            Set<String> titulosUnicos = hechos.stream()
                    .map(Hecho::getTitulo)
                    .collect(Collectors.toSet()); // evitar contar dos veces en la misma fuente

            for (String titulo : titulosUnicos) {
                conteoTitulos.merge(titulo, 1, Integer::sum);
            }
        }

        // Filtrar títulos consensuados
        Set<String> titulosConsensuados = conteoTitulos.entrySet().stream()
                .filter(entry -> entry.getValue() >= umbral)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        // Devolver todos los hechos con título consensuado, sin duplicados
        return hechosPorFuente.values().stream()
                .flatMap(List::stream)
                .filter(hecho -> titulosConsensuados.contains(hecho.getTitulo()))
                .distinct()
                .collect(Collectors.toList());
    }
}