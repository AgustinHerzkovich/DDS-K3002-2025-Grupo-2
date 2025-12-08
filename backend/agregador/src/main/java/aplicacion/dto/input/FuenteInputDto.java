package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
        @JsonSubTypes.Type(value = FuenteEstaticaInputDto.class, name = "estatica"),
        @JsonSubTypes.Type(value = FuenteDinamicaInputDto.class, name = "dinamica"),
        @JsonSubTypes.Type(value = FuenteProxyInputDto.class, name = "proxy")
})
@AllArgsConstructor
@Getter
public class FuenteInputDto {
    @Size(max = 255, message = "El id no puede tener más de 255 caracteres")
    private String id; // nombre
}
