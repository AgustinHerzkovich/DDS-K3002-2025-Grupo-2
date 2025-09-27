package aplicacion.dtos;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProvinciaConMasHechosDTO {
    private Long provinciaId;
    private String nombreProvincia;
    private String pais;
    private Long cantidadHechos;

}
