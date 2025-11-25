package aplicacion.dto.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RevisionSolicitudInputDto {
    private String nuevoEstado;
    private Long adminId;
}
