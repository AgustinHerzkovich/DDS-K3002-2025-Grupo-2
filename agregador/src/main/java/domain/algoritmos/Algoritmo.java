package domain.algoritmos;

import domain.colecciones.fuentes.Fuente;
import domain.hechos.Hecho;

import java.util.List;
import java.util.Map;

// Regla de negocio: Consideramos hecho repetido por fuente a aquellos que contengan el mismo título.
public interface Algoritmo {
    List<Hecho> curarHechos(Map<Fuente,List<Hecho>> hechosPorFuente);
}