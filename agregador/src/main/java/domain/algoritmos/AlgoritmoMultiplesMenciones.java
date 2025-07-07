package domain.algoritmos;

import domain.hechos.Hecho;

import java.util.*;

public class AlgoritmoMultiplesMenciones implements Algoritmo {
    @Override
    public List<Hecho> curarHechos(List<List<Hecho>> listalistahechos){
        Map<Hecho, Integer> conteo = new HashMap<>();
        Map<String, Set<Hecho>> hechosPorTitulo = new HashMap<>();

        for (List<Hecho> fuente : listalistahechos) {
            for (Hecho hecho : fuente) {
                // Contar ocurrencias exactas de cada hecho
                conteo.merge(hecho, 1, Integer::sum);

                // Agrupar hechos por título
                String titulo = hecho.getTitulo();
                if (!hechosPorTitulo.containsKey(titulo)) {
                    hechosPorTitulo.put(titulo, new HashSet<>());
                }
                hechosPorTitulo.get(titulo).add(hecho);
            }
        }

        // Filtrar hechos con al menos dos menciones y sin contradicción por título
        return conteo.entrySet().stream()
                .filter(e -> e.getValue() >= 2) // condición 1: al menos dos fuentes lo mencionan
                .map(Map.Entry::getKey)
                .filter(hecho -> hechosPorTitulo.get(hecho.getTitulo()).size() == 1) // condición 2: no hay otros con mismo título y distinto contenido
                .toList();
    }
}
