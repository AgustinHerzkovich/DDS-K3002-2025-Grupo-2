package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.FuenteDinamica;
import aplicacion.dto.input.FuenteInputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteDinamicaInputMapper {
    public FuenteDinamica map(FuenteInputDto fuenteInputDto) {
        return new FuenteDinamica(
                fuenteInputDto.getId(),
                fuenteInputDto.getIp(),
                fuenteInputDto.getPuerto()
        );
    }
}
