package aplicacion.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProvinciaConMasHechosDeColeccionDTO {
    private Long provincia_id;
    private String nombre_provincia;
    private String nombre_pais;
    private Long cantidad_hechos;
}
