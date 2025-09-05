package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteId;
import aplicacion.dto.input.FuenteInputDTO;
import org.springframework.stereotype.Component;

@Component
public class FuenteInputDTOMapper{
    public Fuente map(FuenteInputDTO fuenteInputDTO){
        Fuente retVal = new Fuente();
        retVal.setId(new FuenteId(fuenteInputDTO.getTipo(), fuenteInputDTO.getId().longValue()));
        retVal.setUltimaPeticion(null);
        return retVal;
    }
}
