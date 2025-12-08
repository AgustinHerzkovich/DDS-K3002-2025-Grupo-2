package aplicacion.dto.input;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoriaInputDto {
    @Size(max = 50, message = "El nombre de la categoría no puede tener más de 50 caracteres")
    private String nombre;
}
