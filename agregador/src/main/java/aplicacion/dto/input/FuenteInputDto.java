package aplicacion.dto.input;

import aplicacion.domain.colecciones.fuentes.FuenteId;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FuenteInputDto {
    private FuenteId id;
    private String ip;
    private Integer puerto;
}
