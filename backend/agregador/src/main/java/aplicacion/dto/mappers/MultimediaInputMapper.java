package aplicacion.dto.mappers;

import aplicacion.domain.hechos.multimedias.Multimedia;
import aplicacion.dto.input.AudioInputDto;
import aplicacion.dto.input.ImagenInputDto;
import aplicacion.dto.input.MultimediaInputDto;
import aplicacion.dto.input.VideoInputDto;
import org.springframework.stereotype.Component;

@Component
public class MultimediaInputMapper implements Mapper<MultimediaInputDto, Multimedia>{
    public Multimedia map(MultimediaInputDto multimediaInputDto){
        return switch (multimediaInputDto) {
            case ImagenInputDto imagenInputDto -> new ImagenInputMapper().map(imagenInputDto);
            case VideoInputDto videoInputDto -> new VideoInputMapper().map(videoInputDto);
            case AudioInputDto audioInputDto -> new AudioInputMapper().map(audioInputDto);
            default -> throw new IllegalArgumentException("Tipo de multimedia no reconocido: " + multimediaInputDto.getClass().getName());
        };
    }
}
