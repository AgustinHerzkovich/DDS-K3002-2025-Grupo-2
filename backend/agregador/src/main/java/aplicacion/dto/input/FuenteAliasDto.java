package aplicacion.dto.input;

import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FuenteAliasDto {
    @Size(max = 255, message = "El alias no puede tener más de 255 caracteres")
    private String alias;
}
