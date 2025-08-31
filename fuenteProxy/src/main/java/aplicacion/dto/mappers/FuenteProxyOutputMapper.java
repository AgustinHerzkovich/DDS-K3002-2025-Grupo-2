package aplicacion.dto.mappers;

import aplicacion.domain.FuenteProxy;
import aplicacion.dto.output.FuenteProxyOutputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteProxyOutputMapper {
    public FuenteProxyOutputDto map(FuenteProxy fuenteProxy) {
        return new FuenteProxyOutputDto(fuenteProxy.getId());
    }
}
