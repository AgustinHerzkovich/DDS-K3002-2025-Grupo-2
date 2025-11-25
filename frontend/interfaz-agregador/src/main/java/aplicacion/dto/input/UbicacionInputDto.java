package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UbicacionInputDto {
    private Double latitud;
    private Double longitud;
}
