package aplicacion.dto.mappers;

import aplicacion.domain.hechos.multimedias.Audio;
import aplicacion.domain.hechos.multimedias.Imagen;
import aplicacion.domain.hechos.multimedias.Multimedia;
import aplicacion.domain.hechos.multimedias.Video;
import aplicacion.dto.output.MultimediaOutputDto;
import org.springframework.stereotype.Component;

@Component
public class MultimediaOutputMapper implements Mapper<Multimedia, MultimediaOutputDto> {
    public MultimediaOutputDto map(Multimedia multimedia) {
        return switch(multimedia) {
            case Audio audio -> new AudioOutputMapper().map(audio);
            case Imagen imagen -> new ImagenOutputMapper().map(imagen);
            case Video video -> new VideoOutputMapper().map(video);
            default -> throw new IllegalArgumentException("Tipo de multimedia no reconocido: " + multimedia.getClass().getName());
        };
    }
}
