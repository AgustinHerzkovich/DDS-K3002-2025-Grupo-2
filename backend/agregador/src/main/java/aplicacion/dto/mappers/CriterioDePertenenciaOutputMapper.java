package aplicacion.dto.mappers;

import aplicacion.domain.criterios.CriterioDeDistancia;
import aplicacion.domain.criterios.CriterioDeFecha;
import aplicacion.domain.criterios.CriterioDePertenencia;
import aplicacion.dto.output.CriterioDePertenenciaOutputDto;
import org.springframework.stereotype.Component;

@Component
public class CriterioDePertenenciaOutputMapper implements Mapper<CriterioDePertenencia, CriterioDePertenenciaOutputDto> {
    private final UbicacionOutputMapper ubicacionOutputMapper;

    public CriterioDePertenenciaOutputMapper(UbicacionOutputMapper ubicacionOutputMapper) {
        this.ubicacionOutputMapper = ubicacionOutputMapper;
    }

    public CriterioDePertenenciaOutputDto map(CriterioDePertenencia criterio) {
        return switch(criterio) {
            case CriterioDeDistancia criterioDeDistancia -> new CriterioDeDistanciaOutputMapper(ubicacionOutputMapper).map(criterioDeDistancia);
            case CriterioDeFecha criterioDeFecha -> new CriterioDeFechaOutputMapper().map(criterioDeFecha);
            default -> throw new IllegalArgumentException("Tipo de criterio de pertenencia no soportado: " + criterio.getClass().getName());
        };
    }
}
