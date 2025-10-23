package aplicacion.dto.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class HechoMapaOutputDto { // Esto es para no cargar los hechos con todos sus datos en la preview del mapa
    private String id;
    private String titulo;
    private UbicacionOutputDto ubicacion;
}
