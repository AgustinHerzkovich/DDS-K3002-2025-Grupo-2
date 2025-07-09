package domain.algoritmos;

import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;

import java.util.*;

public class AlgoritmoAbsoluto implements Algoritmo {
    @Override
    public List<Hecho> curarHechos(Map<Fuente, List<Hecho>> hechosPorFuente) {
        if (hechosPorFuente.isEmpty()) return List.of();

        Iterator<List<Hecho>> iterador = hechosPorFuente.values().iterator();

        Set<Hecho> interseccion = new HashSet<>(iterador.next());

        while (iterador.hasNext()) {
            interseccion.retainAll(new HashSet<>(iterador.next()));
        }

        return new ArrayList<>(interseccion);
    }
}