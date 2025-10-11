package aplicacion.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class SolicitudOutputDto {
    private String hechoId;
    private String motivo;
    private Long id;
    private EstadoSolicitudOutputDto estado;
    private LocalDateTime fechaSubida;
}
