package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.dto.output.ColeccionOutputDto;
import org.springframework.stereotype.Component;

@Component
public class ColeccionOutputMapper {
    private final FuenteOutputMapper fuenteOutputMapper;
    private final AlgoritmoOutputMapper algoritmoOutputMapper;

    public ColeccionOutputMapper(FuenteOutputMapper fuenteOutputMapper, AlgoritmoOutputMapper algoritmoOutputMapper) {
        this.fuenteOutputMapper = fuenteOutputMapper;
        this.algoritmoOutputMapper = algoritmoOutputMapper;
    }
    // Completar
    public ColeccionOutputDto map(Coleccion coleccion) {
        return new ColeccionOutputDto(
                coleccion.getId(),
                coleccion.getTitulo(),
                coleccion.getDescripcion(),
                coleccion.getCriteriosDePertenencia(),
                coleccion.getFuentes().stream().map(fuenteOutputMapper::map).toList(),
                algoritmoOutputMapper.map(coleccion.getAlgoritmoConsenso()),
                coleccion.getTipoAlgoritmoConsenso()
        );
    }

}
