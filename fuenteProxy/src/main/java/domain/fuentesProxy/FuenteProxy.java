package domain.fuentesProxy;

import domain.hechos.Hecho;
import java.util.List;

// FUENTE PROXY
public abstract class FuenteProxy extends Fuente {
    public abstract List<Hecho> importarHechos();

    public abstract void pedirHechos();

    public FuenteProxy(Long id) {
        super(id);
    }
}