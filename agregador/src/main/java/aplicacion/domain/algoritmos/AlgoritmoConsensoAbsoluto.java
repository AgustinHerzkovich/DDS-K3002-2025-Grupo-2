package aplicacion.domain.algoritmos;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.*;

import java.util.*;

@Entity
@DiscriminatorValue("absoluto")
public class AlgoritmoConsensoAbsoluto extends AlgoritmoConsenso {
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