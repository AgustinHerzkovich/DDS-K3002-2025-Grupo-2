package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import aplicacion.dto.input.IdentidadContribuyenteInputDTO;

@Getter
public class ContribuyenteInputDTO {
    private Boolean esAdministrador;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private IdentidadContribuyenteInputDTO identidad;
}
