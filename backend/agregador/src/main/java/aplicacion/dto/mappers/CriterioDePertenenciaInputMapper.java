package aplicacion.dto.mappers;

import aplicacion.domain.criterios.CriterioDePertenencia;
import aplicacion.dto.input.CriterioDeDistanciaInputDto;
import aplicacion.dto.input.CriterioDeFechaInputDto;
import aplicacion.dto.input.CriterioDePertenenciaInputDto;
import org.springframework.stereotype.Component;

@Component
public class CriterioDePertenenciaInputMapper implements Mapper<CriterioDePertenenciaInputDto, CriterioDePertenencia> {
    private final UbicacionInputMapper ubicacionInputMapper;

    public CriterioDePertenenciaInputMapper(UbicacionInputMapper ubicacionInputMapper) {
        this.ubicacionInputMapper = ubicacionInputMapper;
    }

    public CriterioDePertenencia map(CriterioDePertenenciaInputDto criterioDePertenenciaInputDto) {
        return switch(criterioDePertenenciaInputDto) {
            case CriterioDeDistanciaInputDto criterioDeDistanciaInputDto ->  new CriterioDeDistanciaInputMapper(ubicacionInputMapper).map(criterioDeDistanciaInputDto);
            case CriterioDeFechaInputDto criterioDeFechaInputDto -> new CriterioDeFechaInputMapper().map(criterioDeFechaInputDto);
            default -> throw new IllegalArgumentException("Tipo de criterio de pertenencia no soportado: " + criterioDePertenenciaInputDto.getClass().getName());
        };
    }
}
