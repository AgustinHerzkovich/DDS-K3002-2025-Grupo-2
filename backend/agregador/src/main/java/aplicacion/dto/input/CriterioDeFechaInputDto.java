package aplicacion.dto.input;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CriterioDeFechaInputDto extends CriterioDePertenenciaInputDto {
    private LocalDateTime fechaInicial;
    private LocalDateTime fechaFinal;
}
