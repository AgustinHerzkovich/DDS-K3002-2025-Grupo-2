package domain.fuentesMetamapa;
import domain.hechos.Hecho;

import java.util.List;

// FUENTE
public abstract class Fuente {
    private Long id;

    public Fuente(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public abstract List<Hecho> importarHechos();
}