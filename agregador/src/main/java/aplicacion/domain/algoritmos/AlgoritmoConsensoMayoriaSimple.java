package aplicacion.domain.algoritmos;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("mayoriaSimple")
public class AlgoritmoConsensoMayoriaSimple extends AlgoritmoConsenso {
    @Override
    public List<Hecho> curarHechos(Map<Fuente, List<Hecho>> hechosPorFuente) {
        if (hechosPorFuente.isEmpty()) return List.of();

        int umbral = (int) Math.ceil(hechosPorFuente.size() / 2.0);

        Map<Hecho, Integer> conteo = new HashMap<>();

        for (List<Hecho> hechos : hechosPorFuente.values()) {
            Set<Hecho> hechosUnicos = new HashSet<>(hechos); // evitar duplicados en la misma fuente
            for (Hecho hecho : hechosUnicos) {
                conteo.merge(hecho, 1, Integer::sum);
            }
        }

        return conteo.entrySet().stream()
                .filter(e -> e.getValue() >= umbral)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}