package domain.algoritmos;

import domain.hechos.Hecho;
import jakarta.persistence.EmbeddedId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlgoritmoMayoriaSimple implements Algoritmo{
    @Override
    public List<Hecho> curarHechos(List<List<Hecho>> listalistahechos) {

        int total = listalistahechos.size();
        if(total >1) {
            Map<Hecho, Integer> conteo = new HashMap<>();
            for (List<Hecho> fuente : listalistahechos) {
                for (Hecho hecho : fuente) {
                    conteo.merge(hecho, 1, Integer::sum);
                }
            }
            return conteo.entrySet().stream().filter(e -> e.getValue() >= total / 2).map(Map.Entry::getKey).toList();
        }
        else{
            return null;
        }

    }
}
