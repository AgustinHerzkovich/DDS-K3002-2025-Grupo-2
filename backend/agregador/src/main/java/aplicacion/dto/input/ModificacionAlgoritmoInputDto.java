package aplicacion.dto.input;

import aplicacion.domain.algoritmos.TipoAlgoritmoConsenso;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModificacionAlgoritmoInputDto {
    @Size(max = 31, message = "El tipo de algoritmo de consenso no puede tener más de 31 caracteres")
    private TipoAlgoritmoConsenso algoritmoConsenso;
}
