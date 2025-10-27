package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Multimedia;
import aplicacion.dto.input.MultimediaInputDto;
import org.springframework.stereotype.Component;

@Component
public class MultimediaInputMapper implements Mapper<MultimediaInputDto, Multimedia>{
    public Multimedia map(MultimediaInputDto multimediaInputDto){
        return new Multimedia(multimediaInputDto.getUrl());
    }
}
