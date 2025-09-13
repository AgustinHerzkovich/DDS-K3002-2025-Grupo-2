package aplicacion.dto.mappers;

import aplicacion.domain.colecciones.fuentes.*;
import aplicacion.dto.input.FuenteInputDto;
import org.springframework.stereotype.Component;

@Component
public class FuenteInputMapper {
    public Fuente map(FuenteInputDto fuenteInputDTO){
        String tipoFuente = fuenteInputDTO.getId().getTipo().toString().toLowerCase();
        switch (tipoFuente) {
            case "estatica" -> {
                return new FuenteEstaticaInputMapper().map(fuenteInputDTO);
            }
            case "dinamica" -> {
                return new FuenteDinamicaInputMapper().map(fuenteInputDTO);
            }
            case "proxy" -> {
                return new FuenteProxyInputMapper().map(fuenteInputDTO);
            }
            default -> throw new IllegalArgumentException("Tipo de fuente no reconocido: " + tipoFuente);
        }
    }
}
