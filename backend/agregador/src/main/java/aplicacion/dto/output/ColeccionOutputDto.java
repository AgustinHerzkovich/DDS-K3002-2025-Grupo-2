package aplicacion.dto.output;

import aplicacion.domain.algoritmos.TipoAlgoritmoConsenso;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ColeccionOutputDto {
    private String id;
    private String titulo;
    private String descripcion;
    private List<CriterioDePertenenciaOutputDto> criteriosDePertenencia;
    private List<FuenteOutputDto> fuentes;
    private TipoAlgoritmoConsenso tipoAlgoritmoConsenso;
}
