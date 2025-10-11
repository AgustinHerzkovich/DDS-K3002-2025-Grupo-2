package aplicacion.dto.mappers;

import aplicacion.domain.criterios.CriterioDeDistancia;
import aplicacion.dto.input.CriterioDeDistanciaInputDto;
import org.springframework.stereotype.Component;

@Component
public class CriterioDeDistanciaInputMapper implements Mapper<CriterioDeDistanciaInputDto, CriterioDeDistancia> {
    private final UbicacionInputMapper ubicacionInputMapper;

    public CriterioDeDistanciaInputMapper(UbicacionInputMapper ubicacionInputMapper) {
        this.ubicacionInputMapper = ubicacionInputMapper;
    }

    public CriterioDeDistancia map(CriterioDeDistanciaInputDto criterioDeDistanciaInputDto) {
        return new CriterioDeDistancia(
                ubicacionInputMapper.map(criterioDeDistanciaInputDto.getUbicacionBase()),
                criterioDeDistanciaInputDto.getDistanciaMinima()
        );
    }
}
