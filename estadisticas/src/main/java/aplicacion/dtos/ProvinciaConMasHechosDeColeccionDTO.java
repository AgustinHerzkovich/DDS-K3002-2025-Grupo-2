package aplicacion.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonPropertyOrder({ "coleccion_id", "nombre_pais", "nombre_provincia", "cantidad_hechos" })
public class ProvinciaConMasHechosDeColeccionDTO {
    @JsonIgnore
    private Long provincia_id;
    private String nombre_provincia;
    private String nombre_pais;
    private Long cantidad_hechos;
    private String coleccion_id;
}
