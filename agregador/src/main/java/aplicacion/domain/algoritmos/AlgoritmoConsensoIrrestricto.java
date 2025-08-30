package aplicacion.domain.algoritmos;

import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("irrestricto")
public class AlgoritmoConsensoIrrestricto extends AlgoritmoConsenso {
    @Override
    public List<Hecho> curarHechos(Map<Hecho, Integer> cantidadPorHecho, Integer totalFuentes) {
        return new ArrayList<>(cantidadPorHecho.keySet());
    }
}