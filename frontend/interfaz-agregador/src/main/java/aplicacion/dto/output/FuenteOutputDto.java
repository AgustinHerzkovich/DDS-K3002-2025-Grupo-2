package aplicacion.dto.output;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@Data
@NoArgsConstructor
public class FuenteOutputDto {
    private String id;
    private String alias;
    private String tipo;
}
