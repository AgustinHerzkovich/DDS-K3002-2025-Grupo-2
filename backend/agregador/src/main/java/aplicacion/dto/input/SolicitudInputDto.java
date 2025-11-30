package aplicacion.dto.input;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SolicitudInputDto {
    private String solicitanteId;
    private String hechoId;
    @Size(min = 500, max = 2000, message = "El motivo debe tener entre 500 y 2000 caracteres")
    private String motivo;
}
