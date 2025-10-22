package aplicacion.dtos.output;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImagenOutputDto extends MultimediaOutputDto {
    private String resolucion;

    public ImagenOutputDto(Long id, String formato, Integer tamanio, String url, String tipo, String resolucion) {
        super(id, formato, tamanio, url, tipo);
        this.resolucion = resolucion;
    }
}
