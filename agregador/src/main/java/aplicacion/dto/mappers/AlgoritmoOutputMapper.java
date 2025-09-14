package aplicacion.dto.mappers;

import aplicacion.dto.output.AlgoritmoOutputDto;
import org.springframework.stereotype.Component;
import aplicacion.domain.algoritmos.*;

@Component
public class AlgoritmoOutputMapper {
    public AlgoritmoOutputDto map(AlgoritmoConsenso algoritmo){
        String clase  = algoritmo.getClass().getSimpleName();
        switch(clase) {
            case "AlgoritmoConsensoIrrestricto" -> {
                return new AlgoritmoOutputDto("irrestricto");
            }
            case "AlgoritmoConsensoAbsoluto" -> {
                return new AlgoritmoOutputDto("absoluto");
            }
            case "AlgoritmoConsensoMayoriaSimple" -> {
                return new AlgoritmoOutputDto("mayoriaSimple");
            }
            case "AlgoritmoConsensoMultiplesMenciones" -> {
                return new AlgoritmoOutputDto("multiplesMenciones");
            }
            default -> throw new IllegalArgumentException("Tipo de algoritmo no reconocido: " + clase);
        }
        //return new AlgoritmoOutputDto(algoritmo.getClass().getName().toLowerCase());
    }
}
