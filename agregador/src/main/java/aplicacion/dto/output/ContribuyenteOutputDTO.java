package aplicacion.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ContribuyenteOutputDTO {
    private Long id;
    private Boolean esAdministrador;
    private List<IdentidadContribuyenteOutputDTO> identidades;
}
