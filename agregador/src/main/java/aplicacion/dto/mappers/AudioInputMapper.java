package aplicacion.dto.mappers;

import aplicacion.domain.hechos.multimedias.Audio;
import aplicacion.dto.input.AudioInputDto;
import org.springframework.stereotype.Component;

@Component
public class AudioInputMapper implements Mapper<AudioInputDto, Audio>{
    public Audio map(AudioInputDto audioInputDto) {
        return new Audio(
                audioInputDto.getFormato(),
                audioInputDto.getTamanio(),
                audioInputDto.getUrl(),
                audioInputDto.getDuracion()
        );
    }
}
