package domain.dashboardDTOs.hechos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UbicacionDTO {
    private double latitud;
    private double longitud;
    public String toString(){
        return "("+latitud+"º, "+longitud+"º)";
    }
}