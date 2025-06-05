package domain.Fuentes;
import domain.Hechos.Hecho;

import java.util.List;

// FUENTE
public interface Fuente {
    List<Hecho> importarHechos();
}