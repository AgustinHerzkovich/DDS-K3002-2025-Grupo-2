package aplicacion.domain.colecciones.fuentes;

import aplicacion.domain.conexiones.Conexion;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class FuenteDinamica extends Fuente {
    public FuenteDinamica(String id) {
        super(id,"FUENTEDINAMICA");
    }
    public FuenteDinamica(String id, String serviceName) {
        super(id, serviceName);
    }


    @Override
    public String pathIntermedio() {
        return "fuentesDinamicas";
    }

    @Override
    protected String hechosPathParam() {
        return "fuentesDinamicas/" + this.getId() + "/hechos";
    }
}
