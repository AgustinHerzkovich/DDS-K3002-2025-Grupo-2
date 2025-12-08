package aplicacion.dto.output;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContribuyenteOutputDto {
    private String id;
    private Boolean esAdministrador;
    private IdentidadContribuyenteOutputDto identidad;
    private String mail;
}


