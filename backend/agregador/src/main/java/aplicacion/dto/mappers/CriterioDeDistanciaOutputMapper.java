package aplicacion.dto.mappers;

import aplicacion.domain.criterios.CriterioDeDistancia;
import aplicacion.dto.output.CriterioDeDistanciaOutputDto;
import org.springframework.stereotype.Component;

@Component
public class CriterioDeDistanciaOutputMapper implements Mapper<CriterioDeDistancia, CriterioDeDistanciaOutputDto> {
    private final UbicacionOutputMapper ubicacionOutputMapper;

    public CriterioDeDistanciaOutputMapper(UbicacionOutputMapper ubicacionOutputMapper) {
        this.ubicacionOutputMapper = ubicacionOutputMapper;
    }

    public CriterioDeDistanciaOutputDto map(CriterioDeDistancia criterio) {
        return new CriterioDeDistanciaOutputDto(
                criterio.getId(),
                ubicacionOutputMapper.map(criterio.getUbicacionBase()),
                criterio.getDistanciaMinima()
        );
    }
}
