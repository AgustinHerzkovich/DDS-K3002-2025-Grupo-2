package aplicacion.domain.algoritmos;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.hechos.Hecho;
import jakarta.persistence.*;

import java.util.*;

@Entity
@DiscriminatorValue("multiplesMenciones")
public class AlgoritmoConsensoMultiplesMenciones extends AlgoritmoConsenso {
    @Override
    public List<Hecho> curarHechos(Map<Fuente, List<Hecho>> hechosPorFuente) {
        Map<Hecho, Set<Fuente>> ocurrencias = new HashMap<>();
        Map<String, Set<Hecho>> hechosPorTitulo = new HashMap<>();

        // Agrupar hechos por objeto y por título
        for (Map.Entry<Fuente, List<Hecho>> entry : hechosPorFuente.entrySet()) {
            Fuente fuente = entry.getKey();
            for (Hecho hecho : entry.getValue()) {
                ocurrencias.computeIfAbsent(hecho, h -> new HashSet<>()).add(fuente);
                hechosPorTitulo.computeIfAbsent(hecho.getTitulo(), t -> new HashSet<>()).add(hecho);
            }
        }

        List<Hecho> consensuados = new ArrayList<>();

        for (Map.Entry<Hecho, Set<Fuente>> entry : ocurrencias.entrySet()) {
            Hecho hecho = entry.getKey();
            Set<Fuente> fuentesQueLoTienen = entry.getValue();

            if (fuentesQueLoTienen.size() >= 2) {
                // No debe haber otras versiones con mismo título pero distintos atributos
                Set<Hecho> versiones = hechosPorTitulo.get(hecho.getTitulo());
                if (versiones.size() == 1) {
                    consensuados.add(hecho);
                }
            }
        }

        return consensuados;
    }
}