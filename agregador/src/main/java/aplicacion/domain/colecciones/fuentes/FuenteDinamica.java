package aplicacion.domain.colecciones.fuentes;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("dinamica")
@NoArgsConstructor
public class FuenteDinamica extends Fuente {
    public FuenteDinamica(FuenteId id, String ip, Integer puerto) {
        super(id, ip, puerto);
    }

    @Override
    public String pathIntermedio() {
        return "fuentesDinamicas";
    }
}
