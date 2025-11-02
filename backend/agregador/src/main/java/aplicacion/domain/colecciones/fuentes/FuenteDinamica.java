package aplicacion.domain.colecciones.fuentes;

import aplicacion.domain.conexiones.Conexion;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class FuenteDinamica extends Fuente {
    public FuenteDinamica(String id, Conexion conexion) {
        super(id, conexion);
    }

    public FuenteDinamica(String id) {
        super(id);
    }

    @Override
    public String pathIntermedio() {
        return "fuentesDinamicas";
    }
}
