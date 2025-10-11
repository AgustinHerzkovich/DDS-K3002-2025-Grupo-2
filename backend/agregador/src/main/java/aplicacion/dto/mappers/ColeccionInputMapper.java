package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.dto.input.ColeccionInputDto;
import org.springframework.stereotype.Component;

@Component
public class ColeccionInputMapper implements Mapper<ColeccionInputDto, Coleccion> {
    private final FuenteInputMapper fuenteInputMapper;
    private final CriterioDePertenenciaInputMapper criterioDePertenenciaInputMapper;

    public ColeccionInputMapper(FuenteInputMapper fuenteInputMapper, CriterioDePertenenciaInputMapper criterioDePertenenciaInputMapper) {
        this.fuenteInputMapper = fuenteInputMapper;
        this.criterioDePertenenciaInputMapper = criterioDePertenenciaInputMapper;
    }

    public Coleccion map(ColeccionInputDto coleccionInputDTO) {
        return new Coleccion(
                coleccionInputDTO.getTitulo(),
                coleccionInputDTO.getDescripcion(),
                coleccionInputDTO.getCriteriosDePertenencia().stream().map(criterioDePertenenciaInputMapper::map).toList(),
                coleccionInputDTO.getFuentes().stream().map(fuenteInputMapper::map).toList(),
                coleccionInputDTO.getTipoAlgoritmoConsenso()
        );
    }
}
