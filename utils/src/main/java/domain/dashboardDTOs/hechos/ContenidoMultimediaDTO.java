package domain.dashboardDTOs.hechos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContenidoMultimediaDTO {
    private String tipo;
    private String formato;
    private int tamanio;
    private String resolucion;
    private int duracion;
}