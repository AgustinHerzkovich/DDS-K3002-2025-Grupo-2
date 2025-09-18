package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.FuenteDinamica;
import aplicacion.dto.input.FuenteDinamicaInputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteDinamicaInputMapper implements Mapper<FuenteDinamicaInputDto, FuenteDinamica>{
    public FuenteDinamica map(FuenteDinamicaInputDto fuenteInputDto) {
        return new FuenteDinamica(
                fuenteInputDto.getId(),
                fuenteInputDto.getIp(),
                fuenteInputDto.getPuerto()
        );
    }
}
