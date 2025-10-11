package aplicacion.dto.output;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AudioOutputDto extends MultimediaOutputDto {
    private Integer duracion;

    public AudioOutputDto(Long id, String formato, Integer tamanio, String url, String tipo, Integer duracion) {
        super(id, formato, tamanio, url, tipo);
        this.duracion = duracion;
    }
}
