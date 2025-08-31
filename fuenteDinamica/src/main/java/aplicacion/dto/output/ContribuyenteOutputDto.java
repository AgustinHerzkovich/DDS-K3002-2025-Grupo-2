package aplicacion.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ContribuyenteOutputDto {
    private Long id;
    private Boolean esAdministrador;
    private List<IdentidadContribuyenteOutputDto> identidades;
}
