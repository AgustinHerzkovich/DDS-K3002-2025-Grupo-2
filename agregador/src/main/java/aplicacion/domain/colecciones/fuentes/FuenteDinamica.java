package aplicacion.domain.colecciones.fuentes;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class FuenteDinamica extends Fuente {
    public FuenteDinamica(String id, String ip, Integer puerto) {
        super(id, ip, puerto);
    }

    @Override
    public String pathIntermedio() {
        return "fuentesDinamicas";
    }
}
