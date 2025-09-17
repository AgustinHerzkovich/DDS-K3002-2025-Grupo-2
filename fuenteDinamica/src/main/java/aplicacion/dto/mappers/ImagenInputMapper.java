package aplicacion.dto.mappers;

import aplicacion.domain.hechos.multimedias.Imagen;
import aplicacion.dto.input.ImagenInputDto;
import org.springframework.stereotype.Component;

@Component
public class ImagenInputMapper implements Mapper<ImagenInputDto, Imagen>{
    public Imagen map(ImagenInputDto imagenInputDto) {
        return new Imagen(
                imagenInputDto.getFormato(),
                imagenInputDto.getTamanio(),
                imagenInputDto.getResolucion()
        );
    }
}
