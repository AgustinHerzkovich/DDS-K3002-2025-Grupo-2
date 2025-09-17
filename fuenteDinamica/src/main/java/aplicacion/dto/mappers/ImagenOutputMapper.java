package aplicacion.dto.mappers;

import aplicacion.domain.hechos.multimedias.Imagen;
import aplicacion.dto.output.ImagenOutputDto;
import org.springframework.stereotype.Component;

@Component
public class ImagenOutputMapper implements Mapper<Imagen, ImagenOutputDto> {
    public ImagenOutputDto map(Imagen imagen) {
        return new ImagenOutputDto(
                imagen.getId(),
                imagen.getFormato(),
                imagen.getTamanio(),
                imagen.getResolucion()
        );
    }
}
