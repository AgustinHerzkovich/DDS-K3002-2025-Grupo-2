package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,      // usamos el nombre para diferenciar subclases
        include = JsonTypeInfo.As.PROPERTY, // el tipo estará como propiedad en el JSON
        property = "tipo"                // nombre del campo que indica el tipo concreto
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FuenteEstaticaInputDto.class, name = "ESTATICA"),
        @JsonSubTypes.Type(value = FuenteDinamicaInputDto.class, name = "DINAMICA"),
        @JsonSubTypes.Type(value = FuenteProxyInputDto.class, name = "PROXY")
})
@AllArgsConstructor
public class FuenteInputDto {
    private String id;
}
