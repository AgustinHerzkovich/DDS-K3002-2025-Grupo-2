package aplicacion.dto;

import aplicacion.domain.hechos.EstadoRevision;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CambioEstadoRevisionDto {
    private EstadoRevision estado;
    private String sugerencia; // Opcional, en caso de que sea aceptado con sugerencia
}
