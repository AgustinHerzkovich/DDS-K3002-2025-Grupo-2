package aplicacion.dto.mappers;

import aplicacion.domain.algoritmos.AlgoritmoConsenso;
import aplicacion.dto.input.AlgoritmoInputDto;
import org.springframework.stereotype.Component;
import aplicacion.domain.algoritmos.*;
@Component
public class AlgoritmoInputMapper {
    public AlgoritmoInputMapper() {}
    public AlgoritmoConsenso map(AlgoritmoInputDto algoritmoConsenso) {
        switch (algoritmoConsenso.getNombre()) {
            case "irrestricto" -> {
                return new AlgoritmoConsensoIrrestricto();
            }
            case "absoluto" -> {
                return new AlgoritmoConsensoAbsoluto();
            }
            case "mayoriaSimple" -> {
                return new AlgoritmoConsensoMayoriaSimple();
            }
            case "multiplesMenciones" -> {
                return new AlgoritmoConsensoMultiplesMenciones();
            }
            default -> throw new IllegalArgumentException("Tipo de algoritmo no reconocido: " + algoritmoConsenso.getNombre());
        }
    }
}
