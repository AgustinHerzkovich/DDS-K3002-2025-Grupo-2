package aplicacion.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RevisionSolicitudInputDto {
    private String nuevoEstado;
    private Long adminId;
}
