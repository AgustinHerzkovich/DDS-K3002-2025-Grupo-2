package aplicacion.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class SolicitudInputDto {
    private Long solicitanteId;
    private String hechoId;
    private String motivo;
}
