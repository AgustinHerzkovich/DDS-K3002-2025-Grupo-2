package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.dto.input.ColeccionInputDto;
import org.springframework.stereotype.Component;

@Component
public class ColeccionInputMapper {
    private final FuenteInputMapper fuenteInputMapper;

    public ColeccionInputMapper(FuenteInputMapper fuenteInputMapper) {
        this.fuenteInputMapper = fuenteInputMapper;
    }

    public Coleccion map(ColeccionInputDto coleccionInputDTO) {
        return new Coleccion(
                coleccionInputDTO.getTitulo(),
                coleccionInputDTO.getDescripcion(),
                coleccionInputDTO.getCriteriosDePertenencia(),
                coleccionInputDTO.getFuentes().stream().map(fuenteInputMapper::map).toList(),
                coleccionInputDTO.getAlgoritmoConsenso()
        );
    }
}
