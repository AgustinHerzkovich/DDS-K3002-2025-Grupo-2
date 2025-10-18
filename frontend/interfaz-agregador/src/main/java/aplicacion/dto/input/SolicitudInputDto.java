package aplicacion.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SolicitudInputDto {
    private Long solicitanteId;
    private String hechoId;
    private String motivo;
}
