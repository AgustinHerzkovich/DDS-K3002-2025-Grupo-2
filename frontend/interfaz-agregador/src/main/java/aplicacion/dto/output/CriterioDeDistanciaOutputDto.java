package aplicacion.dto.output;

import aplicacion.dto.output.CriterioDePertenenciaOutputDto;
import aplicacion.dto.output.UbicacionOutputDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CriterioDeDistanciaOutputDto extends CriterioDePertenenciaOutputDto {
    private UbicacionOutputDto ubicacionBase;
    private Double distanciaMinima;

    public CriterioDeDistanciaOutputDto(Long id, UbicacionOutputDto ubicacionBase, Double distanciaMinima) {
        super(id);
        this.ubicacionBase = ubicacionBase;
        this.distanciaMinima = distanciaMinima;
    }
}
