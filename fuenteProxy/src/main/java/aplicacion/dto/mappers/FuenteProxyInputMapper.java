package aplicacion.dto.mappers;

import aplicacion.domain.fuentesProxy.FuenteProxy;
import aplicacion.domain.fuentesProxy.fuentesDemo.FuenteDemo;
import aplicacion.domain.fuentesProxy.fuentesMetamapa.FuenteMetamapa;
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