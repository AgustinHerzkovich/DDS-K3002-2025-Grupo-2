package aplicacion.dto.output;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CriterioDeFechaOutputDto extends CriterioDePertenenciaOutputDto{
    private LocalDateTime fechaInicial;
    private LocalDateTime fechaFinal;

    public CriterioDeFechaOutputDto(Long id, LocalDateTime fechaInicial, LocalDateTime fechaFinal) {
        super(id);
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
    }
}
