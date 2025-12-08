package aplicacion.dto.input;

import aplicacion.domain.fuentesProxy.fuentesDemo.Conexion;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuenteProxyInputDto {
    private Conexion biblioteca;
    @Size(max = 1000, message = "La URL no puede tener más de 1000 caracteres")
    private String url;
}
