package domain.dto;

import domain.usuarios.Contribuyente;
import lombok.Getter;

@Getter
public class SolicitudDTO {
    private Contribuyente solicitante;
    private String hechoId;
    private String motivo;
}
