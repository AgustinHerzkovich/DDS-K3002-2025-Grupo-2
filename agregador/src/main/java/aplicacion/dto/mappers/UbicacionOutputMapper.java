package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Ubicacion;
import aplicacion.dto.output.UbicacionOutputDto;
import org.springframework.stereotype.Component;

@Component
public class UbicacionOutputMapper implements Mapper<Ubicacion, UbicacionOutputDto> {
    public UbicacionOutputDto map(Ubicacion ubicacion) {
        return new UbicacionOutputDto(
                ubicacion.getLatitud(),
                ubicacion.getLongitud()
        );
    }
}
