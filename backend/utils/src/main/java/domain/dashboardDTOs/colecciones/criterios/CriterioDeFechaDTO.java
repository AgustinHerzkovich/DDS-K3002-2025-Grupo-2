package domain.dashboardDTOs.colecciones.criterios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriterioDeFechaDTO extends CriterioDePertenenciaDTO {
    private String tipo = "fecha";
    private LocalDateTime fechaInicial;
    private LocalDateTime fechaFinal;
    public String toString(){
        return "{Criterio de fecha, "+fechaInicial+", "+fechaFinal+"}";
    }
}
