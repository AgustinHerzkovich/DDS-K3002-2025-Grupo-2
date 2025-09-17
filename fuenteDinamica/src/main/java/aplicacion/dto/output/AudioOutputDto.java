package aplicacion.dto.output;

import lombok.Setter;

@Setter
public class AudioOutputDto extends MultimediaOutputDto {
    private Integer duracion;

    public AudioOutputDto(Long id, String formato, Integer tamanio, Integer duracion) {
        super(id, formato, tamanio);
        this.duracion = duracion;
    }
}
