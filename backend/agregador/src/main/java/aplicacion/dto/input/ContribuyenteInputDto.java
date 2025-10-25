package aplicacion.dto.input;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContribuyenteInputDto {
    private Boolean esAdministrador;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private IdentidadContribuyenteInputDto identidad;
    private String mail;
}
