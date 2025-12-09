package aplicacion.dto.input;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FuenteMetamapaInputDto {
    @Size(max = 1000, message = "La url no puede tener más de 1000 caracteres")
    private String url;
}
