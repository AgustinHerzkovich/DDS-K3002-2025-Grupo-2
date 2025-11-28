package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class MultimediaInputDto {
    @Size(max = 500, message = "La URL no puede tener más de 500 caracteres")
    private String url;
}
