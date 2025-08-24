package domain.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UbicacionDTO {
    private double latitud;
    private double longitud;
}