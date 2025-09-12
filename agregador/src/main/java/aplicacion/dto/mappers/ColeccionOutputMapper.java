package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.dto.output.ColeccionOutputDto;
import org.springframework.stereotype.Component;

@Component
public class ColeccionOutputMapper {
    public ColeccionOutputMapper() {}
    // Completar
    public ColeccionOutputDto map(Coleccion coleccion) {
        return new ColeccionOutputDto(
                coleccion.getId(),
                coleccion.getTitulo(),
                coleccion.getDescripcion(),
                coleccion.getCriteriosDePertenencia(),
                coleccion.getFuentes(),
                coleccion.getAlgoritmoConsenso()
        );
    }

}
