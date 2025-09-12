package aplicacion.dto.output;
import aplicacion.domain.solicitudes.EstadoSolicitud;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SolicitudOutputDto {
    private String hechoId;
    private String motivo;
    private Long id;
    private EstadoSolicitud estado;
    private LocalDateTime fechaSubida;
}
