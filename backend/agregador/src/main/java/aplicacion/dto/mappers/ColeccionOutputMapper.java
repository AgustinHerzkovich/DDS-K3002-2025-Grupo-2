package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.Coleccion;
import aplicacion.dto.output.ColeccionOutputDto;
import org.springframework.stereotype.Component;

@Component
public class ColeccionOutputMapper implements Mapper<Coleccion, ColeccionOutputDto>{
    private final FuenteOutputMapper fuenteOutputMapper;
    private final CriterioDePertenenciaOutputMapper criterioDePertenenciaOutputMapper;

    public ColeccionOutputMapper(FuenteOutputMapper fuenteOutputMapper, CriterioDePertenenciaOutputMapper criterioDePertenenciaOutputMapper) {
        this.fuenteOutputMapper = fuenteOutputMapper;
        this.criterioDePertenenciaOutputMapper = criterioDePertenenciaOutputMapper;
    }

    public ColeccionOutputDto map(Coleccion coleccion) {
        return new ColeccionOutputDto(
                coleccion.getId(),
                coleccion.getTitulo(),
                coleccion.getDescripcion(),
                coleccion.getCriteriosDePertenencia().stream().map(criterioDePertenenciaOutputMapper::map).toList(),
                coleccion.getFuentes().stream().map(fuenteOutputMapper::map).toList(),
                coleccion.getTipoAlgoritmoConsenso()
        );
    }

}
