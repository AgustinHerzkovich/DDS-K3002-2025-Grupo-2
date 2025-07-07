package domain.algoritmos;

import domain.hechos.Hecho;
import domain.mappers.HechoInEstaticaDTOToHecho;

import java.util.List;
import java.util.stream.Collectors;

public class AlgoritmoIrrestricto implements Algoritmo{
    @Override
    public List<Hecho> curarHechos(List<List<Hecho>> hechos){

        return  hechos.stream().flatMap(List::stream).collect(Collectors.toList());

    }
}
