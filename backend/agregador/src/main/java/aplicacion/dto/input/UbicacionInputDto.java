package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UbicacionInputDto {
    private Double latitud;
    private Double longitud;
}
