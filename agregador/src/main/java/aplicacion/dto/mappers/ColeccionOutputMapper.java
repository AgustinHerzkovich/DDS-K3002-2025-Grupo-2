package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.dto.output.ColeccionOutputDTO;
import org.springframework.stereotype.Component;

@Component
public class ColeccionOutputMapper {
    public ColeccionOutputMapper() {}
    // Completar
    public ColeccionOutputDTO map(Coleccion coleccion) {
        return new ColeccionOutputDTO(
                coleccion.getId(),
                coleccion.getTitulo(),
                coleccion.getDescripcion(),
                coleccion.getCriteriosDePertenencia(),
                coleccion.getFuentes(),
                coleccion.getAlgoritmoConsenso()
        );
    }

}
