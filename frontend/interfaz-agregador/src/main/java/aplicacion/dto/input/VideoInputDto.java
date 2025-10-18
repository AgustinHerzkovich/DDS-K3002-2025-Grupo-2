package aplicacion.dto.input;

import lombok.Getter;

@Getter
public class VideoInputDto extends MultimediaInputDto {
    private String resolucion;
    private Integer duracion;
}
