package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoriaInputDto {
    @Size(max = 50, message = "El nombre de la categoría no puede tener más de 50 caracteres")
    private String nombre;
}
