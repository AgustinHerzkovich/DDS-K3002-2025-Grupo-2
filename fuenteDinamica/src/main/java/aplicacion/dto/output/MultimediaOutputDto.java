package aplicacion.dto.output;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class MultimediaOutputDto {
    private Long id;
    private String formato;
    private Integer tamanio;
}
