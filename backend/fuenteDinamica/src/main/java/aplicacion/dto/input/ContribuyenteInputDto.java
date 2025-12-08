package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContribuyenteInputDto {
    private String id;
    private Boolean esAdministrador;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private IdentidadContribuyenteInputDto identidad;
    @Size(max = 255, message = "El mail no puede tener más de 255 caracteres")
    private String mail;
}
