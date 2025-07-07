package domain.algoritmos;

import domain.hechos.Hecho;

import java.util.List;

public interface Algoritmo {
    public List<Hecho> curarHechos(List<List<Hecho>> listalistahechos);

}