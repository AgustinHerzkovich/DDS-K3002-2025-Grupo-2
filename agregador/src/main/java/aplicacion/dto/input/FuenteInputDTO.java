package aplicacion.dto.input;

import aplicacion.domain.colecciones.fuentes.TipoFuente;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FuenteInputDTO {
    private Integer id;
    private TipoFuente tipo;
}
