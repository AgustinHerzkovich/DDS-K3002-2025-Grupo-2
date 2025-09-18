package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.*;
import aplicacion.dto.input.FuenteDinamicaInputDto;
import aplicacion.dto.input.FuenteEstaticaInputDto;
import aplicacion.dto.input.FuenteInputDto;
import aplicacion.dto.input.FuenteProxyInputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteInputMapper implements Mapper<FuenteInputDto, Fuente>{
    public Fuente map(FuenteInputDto fuenteInputDto){
        return switch(fuenteInputDto) {
            case FuenteEstaticaInputDto fuenteEstaticaInputDto -> new FuenteEstaticaInputMapper().map(fuenteEstaticaInputDto);
            case FuenteDinamicaInputDto fuenteDinamicaInputDto -> new FuenteDinamicaInputMapper().map(fuenteDinamicaInputDto);
            case FuenteProxyInputDto fuenteProxyInputDto -> new FuenteProxyInputMapper().map(fuenteProxyInputDto);
            default -> throw new IllegalArgumentException("Tipo de fuente no reconocido: " + fuenteInputDto.getClass().getName());
        };
    }
}
