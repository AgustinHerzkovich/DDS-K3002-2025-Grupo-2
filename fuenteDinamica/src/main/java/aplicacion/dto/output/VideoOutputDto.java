package aplicacion.dto.output;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VideoOutputDto extends MultimediaOutputDto{
    private String resolucion;
    private Integer duracion;

    public VideoOutputDto(Long id, String formato, Integer tamanio, String tipo, String resolucion, Integer duracion) {
        super(id, formato, tamanio, tipo);
        this.resolucion = resolucion;
        this.duracion = duracion;
    }
}
