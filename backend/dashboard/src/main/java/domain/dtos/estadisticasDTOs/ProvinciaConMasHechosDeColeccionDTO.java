package domain.dtos.estadisticasDTOs;

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
    private String coleccion_id;
    public String toString(){
        return String.format("%s  -  %s   -   %s   -   %d", coleccion_id, nombre_provincia, nombre_pais, cantidad_hechos);
    }
}
