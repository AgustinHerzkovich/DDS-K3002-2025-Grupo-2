package domain.dashboardDTOs.usuarios;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContribuyenteDTO {
    private String contribuyenteId;
    private boolean esAdministrador;
}