package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.FuenteEstatica;
import aplicacion.dto.input.FuenteEstaticaInputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteEstaticaInputMapper implements Mapper<FuenteEstaticaInputDto, FuenteEstatica> {
    public FuenteEstatica map(FuenteEstaticaInputDto fuenteInputDto){
        return new FuenteEstatica(
                fuenteInputDto.getId()
        );
    }
}
