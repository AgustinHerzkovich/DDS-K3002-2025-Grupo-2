package aplicacion.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MultimediaOutputDto {
    private Long id;
    private String formato;
    private Integer tamanio;
    private String url;
    private String tipo;
}
