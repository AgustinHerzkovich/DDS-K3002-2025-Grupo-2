package domain.fuentesDemo;
import domain.hechos.Hecho;

import java.util.List;

// FUENTE
public interface Fuente {
    List<Hecho> importarHechos();
}