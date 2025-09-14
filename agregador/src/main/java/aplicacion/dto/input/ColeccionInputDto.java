package aplicacion.dto.input;

import aplicacion.domain.algoritmos.AlgoritmoConsenso;
import aplicacion.domain.criterios.CriterioDePertenencia;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ColeccionInputDto {
    private String titulo;
    private String descripcion;
    private List<CriterioDePertenencia> criteriosDePertenencia;
    private List<FuenteInputDto> fuentes;
    private AlgoritmoConsenso algoritmoConsenso;
}
