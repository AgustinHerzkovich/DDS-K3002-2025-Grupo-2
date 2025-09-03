package aplicacion.dto.input;

import aplicacion.domain.solicitudes.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SolicitudInputDTO {
    private Long solicitanteId;
    private String hechoId;
    private String motivo;
}
