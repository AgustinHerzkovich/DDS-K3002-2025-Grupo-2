package aplicacion.services.dto;

import domain.usuarios.Contribuyente;
import lombok.Getter;

@Getter
public class SolicitudDTO {
    private Integer solicitanteId;
    private String hechoId;
    private String motivo;
}
