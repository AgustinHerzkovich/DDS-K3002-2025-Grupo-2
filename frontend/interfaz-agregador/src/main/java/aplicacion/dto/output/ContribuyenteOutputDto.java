package aplicacion.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContribuyenteOutputDto {
    private Long id;
    private Boolean esAdministrador;
    private IdentidadContribuyenteOutputDto identidad;
    private String mail;
}


