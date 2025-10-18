package aplicacion.dtos;

import lombok.Getter;

@Getter
public class VideoInputDto extends MultimediaInputDto {
    private String resolucion;
    private Integer duracion;
}
