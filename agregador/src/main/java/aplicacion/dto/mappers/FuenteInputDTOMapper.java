package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteId;
import aplicacion.dto.input.FuenteInputDTO;

public class FuenteInputDTOMapper{
    public static Fuente map(FuenteInputDTO fuenteInputDTO){
        Fuente retVal = new Fuente();
        retVal.setId(new FuenteId(fuenteInputDTO.getTipo(), fuenteInputDTO.getId().longValue()));
        retVal.setUltimaPeticion(null);
        return retVal;
    }
}
