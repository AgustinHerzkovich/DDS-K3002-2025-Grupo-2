package aplicacion.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudOutputDto {
    private String hechoId;
    private String motivo;
    private Long id;
    private EstadoSolicitudOutputDto estado;
    private LocalDateTime fechaSubida;
}
