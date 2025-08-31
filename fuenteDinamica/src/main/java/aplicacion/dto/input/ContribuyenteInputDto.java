package aplicacion.dto;

import lombok.Getter;

@Getter
public class ContribuyenteDto {
    private Boolean esAdministrador;
    private IdentidadContribuyenteDto identidad;
}
