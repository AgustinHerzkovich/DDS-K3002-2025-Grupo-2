package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.FuenteProxy;
import aplicacion.dto.input.FuenteProxyInputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteProxyInputMapper implements Mapper<FuenteProxyInputDto, FuenteProxy>{
    public FuenteProxy map(FuenteProxyInputDto fuenteInputDto) {
        return new FuenteProxy(
                fuenteInputDto.getId()
        );
    }
}
