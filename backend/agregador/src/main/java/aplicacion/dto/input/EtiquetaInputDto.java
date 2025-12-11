package aplicacion.dto.input;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EtiquetaInputDto {
    @Size(max = 50, message = "El nombre de la etiqueta no puede tener más de 50 caracteres")
    private String nombre;
}
