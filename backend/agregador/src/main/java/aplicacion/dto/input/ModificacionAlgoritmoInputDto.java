package aplicacion.dto.input;

import aplicacion.domain.algoritmos.TipoAlgoritmoConsenso;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModificacionAlgoritmoInputDto {
    private TipoAlgoritmoConsenso algoritmoConsenso;
}
