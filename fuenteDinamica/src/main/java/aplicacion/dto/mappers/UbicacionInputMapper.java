package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Ubicacion;
import aplicacion.dto.input.UbicacionInputDto;
import org.springframework.stereotype.Component;

@Component
public class UbicacionInputMapper implements Mapper<UbicacionInputDto, Ubicacion> {
    public Ubicacion map(UbicacionInputDto ubicacionInputDto) {
        return new Ubicacion(
                ubicacionInputDto.getLatitud(),
                ubicacionInputDto.getLongitud()
        );
    }
}
