package aplicacion.dto.input;

import lombok.Getter;

@Getter
public class CriterioDeDistanciaInputDto extends CriterioDePertenenciaInputDto {
    private UbicacionInputDto ubicacionBase;
    private Double distanciaMinima;
}
