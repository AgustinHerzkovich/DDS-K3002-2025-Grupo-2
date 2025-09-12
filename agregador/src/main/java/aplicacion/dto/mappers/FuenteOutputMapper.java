package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.dto.output.FuenteOutputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteOutputMapper {
    public FuenteOutputDto map(Fuente fuente){
        FuenteOutputDto retVal = new FuenteOutputDto();
        retVal.setId(Math.toIntExact(fuente.getId().getIdExterno()));
        retVal.setTipo(fuente.getId().getTipo());
        return retVal;
    }
}
