package domain.fuentesProxy;
import domain.hechos.Hecho;
import lombok.Getter;

import java.util.List;

// FUENTE
public abstract class Fuente {
    @Getter
    private Long id;

    public Fuente(Long id) {
        this.id = id;
    }

    public abstract List<Hecho> importarHechos();
}