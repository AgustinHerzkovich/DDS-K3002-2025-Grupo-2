package aplicacion.dto.output;

import aplicacion.domain.algoritmos.AlgoritmoConsenso;
import aplicacion.domain.criterios.CriterioDePertenencia;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class ColeccionOutputDto {
    private String id;
    private String titulo;
    private String descripcion;
    private List<CriterioDePertenencia> criteriosDePertenencia;
    private List<FuenteOutputDto> fuentes;
    private AlgoritmoOutputDto algoritmoConsenso;


}
