package domain.dto;

import lombok.Getter;

@Getter
public class SolicitudDTO {
    private Long solicitanteId;
    private String hechoId;
    private String motivo;
}
