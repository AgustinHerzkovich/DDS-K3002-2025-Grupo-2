package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.FuenteProxy;
import aplicacion.dto.input.FuenteInputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteProxyInputMapper {
    public FuenteProxy map(FuenteInputDto fuenteInputDto) {
        return new FuenteProxy(
                fuenteInputDto.getId(),
                fuenteInputDto.getIp(),
                fuenteInputDto.getPuerto()
        );
    }
}
