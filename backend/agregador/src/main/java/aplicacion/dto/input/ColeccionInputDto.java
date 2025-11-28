package aplicacion.dto.input;

import aplicacion.domain.algoritmos.TipoAlgoritmoConsenso;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColeccionInputDto {
    @Size(max = 50, message = "El título no puede tener más de 50 caracteres")
    private String titulo;
    @Size(max = 150, message = "La descripción no puede tener más de 150 caracteres")
    private String descripcion;
    private List<CriterioDePertenenciaInputDto> criteriosDePertenencia;
    private List<FuenteInputDto> fuentes;
    @Size(max = 31, message = "El tipo de algoritmo de consenso no puede tener más de 31 caracteres")
    private TipoAlgoritmoConsenso tipoAlgoritmoConsenso;
}
