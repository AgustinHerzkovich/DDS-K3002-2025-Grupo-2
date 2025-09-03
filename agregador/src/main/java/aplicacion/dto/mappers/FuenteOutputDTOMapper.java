package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.dto.output.FuenteOutputDTO;

public class FuenteOutputDTOMapper {
    public static FuenteOutputDTO map(Fuente fuente){
        FuenteOutputDTO retVal = new FuenteOutputDTO();
        retVal.setId(Math.toIntExact(fuente.getId().getIdExterno()));
        retVal.setTipo(fuente.getId().getTipo());
        return retVal;
    }
}
