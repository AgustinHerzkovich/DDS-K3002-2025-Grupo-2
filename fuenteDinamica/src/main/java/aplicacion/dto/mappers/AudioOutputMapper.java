package aplicacion.dto.mappers;

import aplicacion.domain.hechos.multimedias.Audio;
import aplicacion.dto.output.AudioOutputDto;
import org.springframework.stereotype.Component;

@Component
public class AudioOutputMapper implements Mapper<Audio, AudioOutputDto> {
    public AudioOutputDto map(Audio audio) {
        return new AudioOutputDto(
                audio.getId(),
                audio.getFormato(),
                audio.getTamanio(),
                audio.getDuracion()
        );
    }
}
