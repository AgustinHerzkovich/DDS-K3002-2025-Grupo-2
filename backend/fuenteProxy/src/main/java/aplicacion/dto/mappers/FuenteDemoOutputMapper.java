package aplicacion.dto.mappers;

import aplicacion.domain.fuentesProxy.fuentesDemo.FuenteDemo;
import aplicacion.dto.output.FuenteDemoOutputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteDemoOutputMapper {
    public FuenteDemoOutputDto map(FuenteDemo fuenteDemo) {
        return new FuenteDemoOutputDto(fuenteDemo.getId());
    }
}
