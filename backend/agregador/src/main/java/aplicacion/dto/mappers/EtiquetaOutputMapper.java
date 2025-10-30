package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Etiqueta;
import aplicacion.dto.output.EtiquetaOutputDTO;

public class EtiquetaOutputMapper {
    public static EtiquetaOutputDTO map(Etiqueta etiqueta){
        return new EtiquetaOutputDTO(etiqueta.getNombre());
    }
}
