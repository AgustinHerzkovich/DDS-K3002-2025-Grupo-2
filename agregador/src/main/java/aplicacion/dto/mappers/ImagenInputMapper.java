package aplicacion.dto.mappers;

import aplicacion.domain.hechos.multimedias.Imagen;
import aplicacion.dto.input.MultimediaInputDto;
import org.springframework.stereotype.Component;

@Component
public class ImagenInputMapper {
    public Imagen map(MultimediaInputDto multimediaInputDto) {
        return new Imagen(
                multimediaInputDto.getFormato(),
                multimediaInputDto.getTamanio(),
                multimediaInputDto.getResolucion()
        );
    }
}
