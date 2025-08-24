package domain.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriterioDePertenenciaDTO {
    private String tipo;
    private LocalDateTime fechaInicial;
    private LocalDateTime fechaFinal;
}
