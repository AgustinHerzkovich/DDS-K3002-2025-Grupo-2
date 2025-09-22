package aplicacion.dto.output;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImagenOutputDto extends MultimediaOutputDto{
    private String resolucion;

    public ImagenOutputDto(Long id, String formato, Integer tamanio, String tipo, String resolucion) {
        super(id, formato, tamanio, tipo);
        this.resolucion = resolucion;
    }
}
