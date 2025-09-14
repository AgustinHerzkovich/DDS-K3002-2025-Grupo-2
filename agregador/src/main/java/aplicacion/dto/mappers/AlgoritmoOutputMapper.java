package aplicacion.dto.mappers;

import aplicacion.dto.output.AlgoritmoOutputDto;
import org.springframework.stereotype.Component;
import aplicacion.domain.algoritmos.*;

@Component
public class AlgoritmoOutputMapper {
    public AlgoritmoOutputDto map(AlgoritmoConsenso algoritmo){
        return new AlgoritmoOutputDto(algoritmo.getClass().getName());
    }
}
