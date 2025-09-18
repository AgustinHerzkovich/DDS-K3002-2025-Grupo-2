package aplicacion.dto.output;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImagenOutputDto extends MultimediaOutputDto{
    private String resolucion;

    public ImagenOutputDto(Long id, String formato, Integer tamanio, String resolucion) {
        super(id, formato, tamanio);
        this.resolucion = resolucion;
    }
}
