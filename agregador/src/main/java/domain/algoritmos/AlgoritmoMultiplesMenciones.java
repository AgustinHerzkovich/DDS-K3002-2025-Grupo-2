package domain.algoritmos;

import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;

import java.util.*;

public class AlgoritmoMultiplesMenciones implements Algoritmo {
    @Override
    public List<Hecho> curarHechos(Map<Fuente, List<Hecho>> hechosPorFuente) {
        Map<String, Map<Hecho, Set<Fuente>>> hechosPorTitulo = new HashMap<>();

        // Agrupar por título → hecho → fuentes
        for (Map.Entry<Fuente, List<Hecho>> entrada : hechosPorFuente.entrySet()) {
            Fuente fuente = entrada.getKey();
            for (Hecho hecho : entrada.getValue()) {
                String titulo = hecho.getTitulo();

                hechosPorTitulo
                        .computeIfAbsent(titulo, t -> new HashMap<>())
                        .computeIfAbsent(hecho, h -> new HashSet<>())
                        .add(fuente);
            }
        }

        List<Hecho> consensuados = new ArrayList<>();

        for (Map.Entry<String, Map<Hecho, Set<Fuente>>> entradaPorTitulo : hechosPorTitulo.entrySet()) {
            Map<Hecho, Set<Fuente>> versionesDelHecho = entradaPorTitulo.getValue();

            // Buscamos una versión del hecho que esté en al menos 2 fuentes
            Optional<Map.Entry<Hecho, Set<Fuente>>> posibleConsensuado = versionesDelHecho.entrySet().stream()
                    .filter(e -> e.getValue().size() >= 2)
                    .findFirst();

            if (posibleConsensuado.isPresent()) {
                // Verificamos que no haya otras versiones con el mismo título en otras fuentes
                if (versionesDelHecho.size() == 1) {
                    consensuados.add(posibleConsensuado.get().getKey());
                }
            }
        }

        return consensuados;
    }
}