package domain.dashboardDTOs.solicitudes;

import domain.dashboardDTOs.usuarios.ContribuyenteSolicitudDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SolicitudDTO {
    private ContribuyenteSolicitudDTO solicitante;
    private String hechoId;
    private String motivo;}

