package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.dto.output.ColeccionOutputDto;
import org.springframework.stereotype.Component;

@Component
public class ColeccionOutputMapper {
    private final FuenteOutputMapper fuenteOutputMapper;

    public ColeccionOutputMapper(FuenteOutputMapper fuenteOutputMapper) {
        this.fuenteOutputMapper = fuenteOutputMapper;
    }
    // Completar
    public ColeccionOutputDto map(Coleccion coleccion) {
        return new ColeccionOutputDto(
                coleccion.getId(),
                coleccion.getTitulo(),
                coleccion.getDescripcion(),
                coleccion.getCriteriosDePertenencia(),
                coleccion.getFuentes().stream().map(fuenteOutputMapper::map).toList(),
                coleccion.getAlgoritmoConsenso()
        );
    }

}
