package aplicacion.dto.input;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RevisionSolicitudInputDto {
    @Size(max = 31, message = "El nuevo estado no puede tener más de 31 caracteres")
    private String nuevoEstado;
    private String adminId;
}
