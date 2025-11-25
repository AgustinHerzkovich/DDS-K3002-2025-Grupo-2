package aplicacion.dto.input;


import aplicacion.dto.EstadoRevision;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambioEstadoRevisionInputDto {
    private EstadoRevision estado;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sugerencia; // Opcional, en caso de que sea aceptado con sugerencia
}
