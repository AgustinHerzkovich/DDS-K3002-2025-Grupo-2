package aplicacion.dto.output;

import aplicacion.domain.colecciones.fuentes.TipoFuente;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FuenteOutputDto {
    private Integer id;
    private TipoFuente tipo;
}
