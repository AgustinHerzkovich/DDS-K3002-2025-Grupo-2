package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.dto.output.FuenteOutputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteOutputMapper implements Mapper<Fuente, FuenteOutputDto>{
    public FuenteOutputDto map(Fuente fuente){
        FuenteOutputDto retVal = new FuenteOutputDto();
        retVal.setId(fuente.getId());
        retVal.setAlias(fuente.getAlias());
        return retVal;
    }
}
