package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ContribuyenteInputDto {
    private Boolean esAdministrador;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private IdentidadContribuyenteInputDto identidad;
    private String mail;
}
