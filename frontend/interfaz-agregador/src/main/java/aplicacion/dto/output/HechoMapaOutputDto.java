package aplicacion.dto.output;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Solo usar campos marcados con @EqualsAndHashCode.Include
public class HechoMapaOutputDto { // Esto es para no cargar los hechos con todos sus datos en la preview del mapa
    @EqualsAndHashCode.Include // Dos hechos son iguales si tienen el mismo id
    private String id;
    private String titulo;
    private UbicacionOutputDto ubicacion;
    private LocalDateTime fechaCarga;
    private String direccion;
}
