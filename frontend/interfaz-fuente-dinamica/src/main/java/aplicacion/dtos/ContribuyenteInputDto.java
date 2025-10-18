package aplicacion.dtos;

import aplicacion.dtos.IdentidadContribuyenteInputDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContribuyenteInputDto {
    private Boolean esAdministrador;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private IdentidadContribuyenteInputDto identidad;
    private String mail;
}
