package aplicacion.dto.output;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CriterioDeDistanciaOutputDto extends CriterioDePertenenciaOutputDto {
    private UbicacionOutputDto ubicacionBase;
    private Double distanciaMaxima;

    public CriterioDeDistanciaOutputDto(Long id, UbicacionOutputDto ubicacionBase, Double distanciaMaxima) {
        super(id, "DISTANCIA");
        this.ubicacionBase = ubicacionBase;
        this.distanciaMaxima = distanciaMaxima;
    }
}
