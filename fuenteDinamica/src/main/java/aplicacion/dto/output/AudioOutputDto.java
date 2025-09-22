package aplicacion.dto.output;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AudioOutputDto extends MultimediaOutputDto {
    private Integer duracion;

    public AudioOutputDto(Long id, String formato, Integer tamanio, String tipo, Integer duracion) {
        super(id, formato, tamanio, tipo);
        this.duracion = duracion;
    }
}
