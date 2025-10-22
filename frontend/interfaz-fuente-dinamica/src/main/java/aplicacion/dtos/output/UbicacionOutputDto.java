package aplicacion.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UbicacionOutputDto {
    private Long id;
    private Double latitud;
    private Double longitud;
}
