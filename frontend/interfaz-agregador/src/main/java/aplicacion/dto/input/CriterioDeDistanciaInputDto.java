package aplicacion.dto.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriterioDeDistanciaInputDto extends CriterioDePertenenciaInputDto {
    private UbicacionInputDto ubicacionBase;
    private Double distanciaMinima;
}
