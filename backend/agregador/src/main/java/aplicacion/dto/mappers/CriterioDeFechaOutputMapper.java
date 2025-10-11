package aplicacion.dto.mappers;

import aplicacion.domain.criterios.CriterioDeFecha;
import aplicacion.dto.output.CriterioDeFechaOutputDto;
import org.springframework.stereotype.Component;

@Component
public class CriterioDeFechaOutputMapper implements Mapper<CriterioDeFecha, CriterioDeFechaOutputDto> {
    public CriterioDeFechaOutputDto map(CriterioDeFecha criterio) {
        return new CriterioDeFechaOutputDto(
                criterio.getId(),
                criterio.getFechaInicial(),
                criterio.getFechaFinal()
        );
    }
}
