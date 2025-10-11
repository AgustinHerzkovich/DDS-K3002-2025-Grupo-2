package aplicacion.dto.mappers;

import aplicacion.domain.criterios.CriterioDeFecha;
import aplicacion.dto.input.CriterioDeFechaInputDto;
import org.springframework.stereotype.Component;

@Component
public class CriterioDeFechaInputMapper implements Mapper<CriterioDeFechaInputDto, CriterioDeFecha> {
    public CriterioDeFecha map(CriterioDeFechaInputDto criterioDeFechaInputDto) {
        return new CriterioDeFecha(
                criterioDeFechaInputDto.getFechaInicial(),
                criterioDeFechaInputDto.getFechaFinal()
        );
    }
}
