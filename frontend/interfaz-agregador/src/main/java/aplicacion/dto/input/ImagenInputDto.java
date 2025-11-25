package aplicacion.dto.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagenInputDto extends MultimediaInputDto {
    private String resolucion;
}
