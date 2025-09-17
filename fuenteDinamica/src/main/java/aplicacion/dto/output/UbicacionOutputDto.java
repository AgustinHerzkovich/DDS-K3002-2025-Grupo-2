package aplicacion.dto.output;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class UbicacionOutputDto {
    private Long id;
    private Double latitud;
    private Double longitud;
}
