package aplicacion.domain.algoritmos;

import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.*;

import java.util.*;

@Entity
@DiscriminatorValue("absoluto")
public class AlgoritmoConsensoAbsoluto extends AlgoritmoConsenso {
    @Override
    public List<Hecho> curarHechos(Map<Hecho, Integer> cantidadPorHecho, Integer totalFuentes) {
        return cantidadPorHecho.entrySet().stream()
                .filter(e -> Objects.equals(e.getValue(), totalFuentes))
                .map(Map.Entry::getKey)
                .toList();
    }
}