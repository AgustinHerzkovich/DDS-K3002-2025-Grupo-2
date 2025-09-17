package aplicacion.dto.output;

import lombok.Setter;

@Setter
public class VideoOutputDto extends MultimediaOutputDto{
    private String resolucion;
    private Integer duracion;

    public VideoOutputDto(Long id, String formato, Integer tamanio, String resolucion, Integer duracion) {
        super(id, formato, tamanio);
        this.resolucion = resolucion;
        this.duracion = duracion;
    }
}
