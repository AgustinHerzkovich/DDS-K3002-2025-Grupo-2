package aplicacion.dto.mappers;

import aplicacion.domain.fuentesProxy.fuentesDemo.FuenteDemo;
import aplicacion.dto.input.FuenteDemoInputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteDemoInputMapper {
    public FuenteDemo map(FuenteDemoInputDto fuenteDemoInputDto) {
        return new FuenteDemo(fuenteDemoInputDto.getBiblioteca(), fuenteDemoInputDto.getUrl());
    }
}