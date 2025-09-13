package aplicacion.dto.mappers;

import aplicacion.domain.hechos.multimedias.Audio;
import aplicacion.dto.input.MultimediaInputDto;
import org.springframework.stereotype.Component;

@Component
public class AudioInputMapper {
    public Audio map(MultimediaInputDto multimediaInputDto) {
        return new Audio(
                multimediaInputDto.getFormato(),
                multimediaInputDto.getTamanio(),
                multimediaInputDto.getDuracion()
        );
    }
}
