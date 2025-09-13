package aplicacion.dto.mappers;

import aplicacion.domain.hechos.multimedias.Video;
import aplicacion.dto.input.MultimediaInputDto;
import org.springframework.stereotype.Component;

@Component
public class VideoInputMapper {
    public Video map(MultimediaInputDto multimediaInputDto) {
        return new Video(
                multimediaInputDto.getFormato(),
                multimediaInputDto.getTamanio(),
                multimediaInputDto.getResolucion(),
                multimediaInputDto.getDuracion()
        );
    }
}
