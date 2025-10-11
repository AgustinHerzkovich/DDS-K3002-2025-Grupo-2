package domain.dashboardDTOs.colecciones.criterios;

import domain.dashboardDTOs.hechos.UbicacionDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriterioDeDistanciaDTO extends CriterioDePertenenciaDTO {
    private String tipo = "distancia";
    private UbicacionDTO ubicacionbase;
    private Double distancia;
    public String toString(){
        return "{Criterio de distancia, "+ubicacionbase+", "+distancia+"m}";
    }
}