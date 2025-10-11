package aplicacion.domain.colecciones.fuentes;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class FuenteProxy extends Fuente{
    public FuenteProxy(String id, String ip, Integer puerto) {
        super(id, ip, puerto);
    }

    @Override
    public String pathIntermedio() {
        return "fuentesProxy/" + this.getId();
    }
}
