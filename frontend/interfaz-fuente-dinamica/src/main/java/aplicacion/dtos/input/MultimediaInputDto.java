package aplicacion.dtos.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,      // usamos el nombre para diferenciar subclases
        include = JsonTypeInfo.As.PROPERTY, // el tipo estará como propiedad en el JSON
        property = "tipo"                // nombre del campo que indica el tipo concreto
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AudioInputDto.class, name = "audio"),
        @JsonSubTypes.Type(value = ImagenInputDto.class, name = "imagen"),
        @JsonSubTypes.Type(value = VideoInputDto.class, name = "video")
})
public class MultimediaInputDto {
    private String formato;
    private Integer tamanio;
    private String url;
}
