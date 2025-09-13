package aplicacion.dto.mappers;

import aplicacion.domain.hechos.multimedias.Multimedia;
import aplicacion.dto.input.MultimediaInputDto;
import org.springframework.stereotype.Component;

@Component
public class MultimediaInputMapper {
    public Multimedia map(MultimediaInputDto multimediaInputDto){
        String tipoMultimedia = multimediaInputDto.getTipo().toLowerCase();
        switch (tipoMultimedia) {
            case "imagen" -> {
                return new ImagenInputMapper().map(multimediaInputDto);
            }
            case "video" -> {
                return new VideoInputMapper().map(multimediaInputDto);
            }
            case "audio" -> {
                return new AudioInputMapper().map(multimediaInputDto);
            }
            default -> throw new IllegalArgumentException("Tipo de multimedia no reconocido: " + tipoMultimedia);
        }
    }
}
