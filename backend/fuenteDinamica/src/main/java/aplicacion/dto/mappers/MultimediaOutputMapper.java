package aplicacion.dto.mappers;


import aplicacion.domain.hechos.Multimedia;

import aplicacion.dto.output.MultimediaOutputDto;
import org.springframework.stereotype.Component;

@Component
public class MultimediaOutputMapper implements Mapper<Multimedia, MultimediaOutputDto> {
    public MultimediaOutputDto map(Multimedia multimedia) {
        return new MultimediaOutputDto(multimedia.getId(), multimedia.getUrl());
    }
}
