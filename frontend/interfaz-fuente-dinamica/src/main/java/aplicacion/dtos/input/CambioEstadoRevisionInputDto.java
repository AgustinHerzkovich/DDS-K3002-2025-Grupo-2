package aplicacion.dtos.input;


import aplicacion.dtos.EstadoRevision;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class CambioEstadoRevisionInputDto {
    private EstadoRevision estado;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sugerencia; // Opcional, en caso de que sea aceptado con sugerencia
}
