package aplicacion.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProvinciaConMasHechosDTO {
    private Long provinciaId;
    private String nombreProvincia;
    private String pais;
    private Long cantidadHechos;

}
