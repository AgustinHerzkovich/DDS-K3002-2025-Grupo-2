package domain.algoritmos;

import domain.hechos.Hecho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

suñiga
public class AlgoritmoAbsoluto implements Algoritmo {
    @Override
    public List<Hecho> curarHechos(List<List<Hecho>> listalistahechos) {
        // Se verifica cada hecho que este en todas las fuentes
        int total = listalistahechos.size();
        Map<Hecho, Integer> conteo = new HashMap<>();
        if (total > 1) {
            for (List<Hecho> lista : listalistahechos) {
                for (Hecho hecho : lista) {
                    conteo.merge(hecho, 1, Integer::sum);
                }
            }
            List<Hecho> nuevaLista = conteo.entrySet().stream().filter(e -> e.getValue() == total).map(Map.Entry::getKey).toList();
            return nuevaLista;
        }else{

            return listalistahechos.stream().flatMap(List::stream).collect(Collectors.toList());
        }

        // o

        // Se agrupa por hecho y se cuentan ocurrencias. Si cant > cantidad de fuentes / 2, entra

        // o

        // Se busca un hecho que aparezca en 2 fuentes y no este contradecido por otra fuente

    }

}
