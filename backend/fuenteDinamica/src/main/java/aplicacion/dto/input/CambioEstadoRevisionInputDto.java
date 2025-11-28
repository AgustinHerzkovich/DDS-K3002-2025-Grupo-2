package aplicacion.dto.input;

import aplicacion.domain.hechos.EstadoRevision;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CambioEstadoRevisionInputDto {
    @Size(max = 255, message = "El estado de la revisión no puede tener más de 255 caracteres")
    private EstadoRevision estado;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(max = 255, message = "La sugerencia no puede tener más de 255 caracteres")
    private String sugerencia; // Opcional, en caso de que sea aceptado con sugerencia
}
