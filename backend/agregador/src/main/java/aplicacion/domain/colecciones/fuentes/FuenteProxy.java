package aplicacion.domain.colecciones.fuentes;

import aplicacion.domain.conexiones.Conexion;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class FuenteProxy extends Fuente{
    public FuenteProxy(String id, Conexion conexion) {
        super(id, conexion);
    }

    public FuenteProxy(String id) {
    }

    @Override
    public String pathIntermedio() {
        return "fuentesProxy/" + this.getId();
    }
}
