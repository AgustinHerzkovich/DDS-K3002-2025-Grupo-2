package aplicacion.dto.mappers;

import aplicacion.domain.fuentesProxy.fuentesMetamapa.FuenteMetamapa;
import aplicacion.dto.input.FuenteMetamapaInputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteMetamapaInputMapper {
    public FuenteMetamapa map(FuenteMetamapaInputDto fuenteMetamapaInputDto) {
        return new FuenteMetamapa(fuenteMetamapaInputDto.getUrl());
    }
}
