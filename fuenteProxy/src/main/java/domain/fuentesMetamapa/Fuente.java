package domain.fuentesMetamapa;
import domain.hechos.Hecho;

import java.util.List;

// FUENTE
public interface Fuente {
    List<Hecho> importarHechos();
}