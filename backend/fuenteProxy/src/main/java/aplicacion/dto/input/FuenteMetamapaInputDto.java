package aplicacion.dto.input;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FuenteMetamapaInputDto {
    @Size(max = 1000, message = "El agregadorId no puede tener más de 1000 caracteres")
    private String agregadorId;
}
