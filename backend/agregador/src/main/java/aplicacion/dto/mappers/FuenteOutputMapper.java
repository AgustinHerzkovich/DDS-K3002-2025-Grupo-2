package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.Fuente;
import aplicacion.domain.colecciones.fuentes.FuenteDinamica;
import aplicacion.domain.colecciones.fuentes.FuenteEstatica;
import aplicacion.domain.colecciones.fuentes.FuenteProxy;
import aplicacion.dto.output.FuenteOutputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteOutputMapper implements Mapper<Fuente, FuenteOutputDto>{
    public FuenteOutputDto map(Fuente fuente){
        FuenteOutputDto retVal = new FuenteOutputDto();
        retVal.setId(fuente.getId());
        retVal.setAlias(fuente.getAlias());
        String tipo = switch (fuente) {
            case FuenteEstatica fuenteEstatica -> "estatica";
            case FuenteDinamica fuenteDinamica -> "dinamica";
            case FuenteProxy fuenteProxy -> "proxy";
            default -> "Desconocida";
        };
        retVal.setTipo(tipo);
        return retVal;
    }
}
