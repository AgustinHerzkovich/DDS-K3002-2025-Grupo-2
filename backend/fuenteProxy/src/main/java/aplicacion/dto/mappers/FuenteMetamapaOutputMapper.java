package aplicacion.dto.mappers;

import aplicacion.domain.fuentesProxy.fuentesMetamapa.FuenteMetamapa;
import aplicacion.dto.output.FuenteMetamapaOutputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteMetamapaOutputMapper {
    public FuenteMetamapaOutputDto map(FuenteMetamapa fuenteMetamapa) {
        return new FuenteMetamapaOutputDto(fuenteMetamapa.getId(), fuenteMetamapa.getUrl());
    }
}
