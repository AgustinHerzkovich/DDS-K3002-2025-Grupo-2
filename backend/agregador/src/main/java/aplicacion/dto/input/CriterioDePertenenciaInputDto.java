package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,      // usamos el nombre para diferenciar subclases
        include = JsonTypeInfo.As.PROPERTY, // el tipo estará como propiedad en el JSON
        property = "tipo"                // nombre del campo que indica el tipo concreto
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CriterioDeDistanciaInputDto.class, name = "distancia"),
        @JsonSubTypes.Type(value = CriterioDeFechaInputDto.class, name = "fecha")
})
public class CriterioDePertenenciaInputDto {

}
