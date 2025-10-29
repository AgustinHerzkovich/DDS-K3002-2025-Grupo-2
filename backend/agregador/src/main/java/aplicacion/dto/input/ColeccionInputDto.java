package aplicacion.dto.input;

import aplicacion.domain.algoritmos.TipoAlgoritmoConsenso;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ColeccionInputDto {
    private String titulo;
    private String descripcion;
    private List<CriterioDePertenenciaInputDto> criteriosDePertenencia;
    private List<FuenteInputDto> fuentes;
    private TipoAlgoritmoConsenso tipoAlgoritmoConsenso;
}
