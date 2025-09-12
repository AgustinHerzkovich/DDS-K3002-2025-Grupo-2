package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteId;
import aplicacion.dto.input.FuenteInputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteInputMapper {
    public Fuente map(FuenteInputDto fuenteInputDTO){
        Fuente retVal = new Fuente();
        retVal.setId(new FuenteId(fuenteInputDTO.getTipo(), fuenteInputDTO.getId().longValue()));
        retVal.setUltimaPeticion(null);
        return retVal;
    }
}
