package aplicacion.dto.input;

import aplicacion.domain.fuentesDemo.Conexion;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuenteProxyInputDto {
    private Conexion biblioteca;
    private String url;
}
