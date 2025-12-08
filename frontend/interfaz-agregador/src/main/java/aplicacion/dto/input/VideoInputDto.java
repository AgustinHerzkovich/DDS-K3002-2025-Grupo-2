package aplicacion.dto.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoInputDto extends MultimediaInputDto {
    private String resolucion;
    private Integer duracion;
}
