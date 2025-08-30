package aplicacion.domain.algoritmos;

import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.*;

import java.util.*;

@Entity
@DiscriminatorValue("multiplesMenciones")
public class AlgoritmoConsensoMultiplesMenciones extends AlgoritmoConsenso {
    @Override
    public List<Hecho> curarHechos(Map<Hecho, Integer> cantidadPorHecho, Integer totalFuentes) {
        return cantidadPorHecho.entrySet().stream()
                .filter(e -> e.getValue() >= 2) // al menos 2 fuentes
                .filter(e -> !existeOtroHechoConMismoTituloPeroDiferentesAtributos(e.getKey(), cantidadPorHecho.keySet()))
                .map(Map.Entry::getKey)
                .toList();
    }

    // Metodo auxiliar que revisa si hay otro hecho con el mismo título pero atributos distintos
    private Boolean existeOtroHechoConMismoTituloPeroDiferentesAtributos(Hecho hecho, Set<Hecho> todosLosHechos) {
        return todosLosHechos.stream()
                .anyMatch(h -> !h.equals(hecho) && h.getTitulo().equals(hecho.getTitulo())); // Aclaración: equals no compara todos los atributos, solo los marcados como @EqualsAndHashCode.Include (ver clase Hecho)
    }
}