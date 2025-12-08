package aplicacion.domain.colecciones.fuentes;

import aplicacion.domain.conexiones.Conexion;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class FuenteProxy extends Fuente{
    public FuenteProxy(String id) {
        super(id, "Fuente Proxy " + id.substring(0, 10) );
    }







}
