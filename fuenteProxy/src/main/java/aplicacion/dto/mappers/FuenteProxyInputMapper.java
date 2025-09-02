package aplicacion.dto.mappers;

import aplicacion.domain.FuenteProxy;
import aplicacion.domain.fuentesDemo.FuenteDemo;
import aplicacion.domain.fuentesMetamapa.FuenteMetamapa;
import aplicacion.dto.input.FuenteProxyInputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteProxyInputMapper {
    public FuenteProxy map(FuenteProxyInputDto fuenteProxyInputDto) {
        if (fuenteProxyInputDto.getBiblioteca() == null) {
            return new FuenteMetamapa(fuenteProxyInputDto.getUrl());
        } else {
            return new FuenteDemo(fuenteProxyInputDto.getBiblioteca(), fuenteProxyInputDto.getUrl());
        }
    }
}