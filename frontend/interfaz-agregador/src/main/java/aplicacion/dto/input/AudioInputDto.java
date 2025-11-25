package aplicacion.dto.input;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AudioInputDto extends MultimediaInputDto {
    private Integer duracion;
}
