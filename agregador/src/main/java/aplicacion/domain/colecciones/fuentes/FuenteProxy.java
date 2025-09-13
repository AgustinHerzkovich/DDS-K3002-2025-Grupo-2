package aplicacion.domain.colecciones.fuentes;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("proxy")
@NoArgsConstructor
public class FuenteProxy extends Fuente{
    public FuenteProxy(FuenteId id, String ip, Integer puerto) {
        super(id, ip, puerto);
    }

    @Override
    public String pathIntermedio() {
        return "fuentesProxy/" + this.getId().getIdExterno();
    }
}
