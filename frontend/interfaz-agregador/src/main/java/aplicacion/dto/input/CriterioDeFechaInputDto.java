package aplicacion.dto.input;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CriterioDeFechaInputDto extends CriterioDePertenenciaInputDto {
    private LocalDateTime fechaInicial;
    private LocalDateTime fechaFinal;
}
