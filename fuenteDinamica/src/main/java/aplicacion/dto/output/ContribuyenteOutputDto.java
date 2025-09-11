package aplicacion.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ContribuyenteOutputDto {
    private Long id;
    private Boolean esAdministrador;
    private IdentidadContribuyenteOutputDto identidad;
}
