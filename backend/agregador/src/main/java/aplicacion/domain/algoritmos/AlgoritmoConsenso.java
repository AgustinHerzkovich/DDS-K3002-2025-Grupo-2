package aplicacion.domain.algoritmos;

import aplicacion.domain.hechos.Hecho;

import java.util.List;
import java.util.Map;

// Regla de negocio: Consideramos hecho repetido por fuente a aquellos que contengan los mismos atributos que incluimos en `EqualsAndHashCode` en el Hecho.
public interface AlgoritmoConsenso {
    List<Hecho> curarHechos(Map<Hecho, Long> cantidadPorHecho, Long totalFuentes);
}