package aplicacion.dto.mappers;

import aplicacion.domain.hechos.multimedias.Video;
import aplicacion.dto.output.VideoOutputDto;
import org.springframework.stereotype.Component;

@Component
public class VideoOutputMapper implements Mapper<Video, VideoOutputDto> {
    public VideoOutputDto map(Video video) {
        return new VideoOutputDto(
                video.getId(),
                video.getFormato(),
                video.getTamanio(),
                video.getResolucion(),
                video.getDuracion()
        );
    }
}
