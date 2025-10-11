package domain.dashboardDTOs.fuentes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuenteIdDTO {
    private String tipo;
    private int idExterno;

}
