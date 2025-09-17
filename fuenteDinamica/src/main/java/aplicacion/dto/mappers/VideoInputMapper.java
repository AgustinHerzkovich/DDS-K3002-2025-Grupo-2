package aplicacion.dto.mappers;

import aplicacion.domain.hechos.multimedias.Video;
import aplicacion.dto.input.VideoInputDto;
import org.springframework.stereotype.Component;

@Component
public class VideoInputMapper implements Mapper<VideoInputDto, Video>{
    public Video map(VideoInputDto videoInputDto) {
        return new Video(
                videoInputDto.getFormato(),
                videoInputDto.getTamanio(),
                videoInputDto.getResolucion(),
                videoInputDto.getDuracion()
        );
    }
}
