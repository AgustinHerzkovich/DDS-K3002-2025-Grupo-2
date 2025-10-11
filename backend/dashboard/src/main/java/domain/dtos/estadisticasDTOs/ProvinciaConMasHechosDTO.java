package domain.dtos.estadisticasDTOs;


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
    private String categoria;
    public String toString(){
        return String.format("%s - %s  -  %s  -  %d hechos", categoria, nombreProvincia, pais, cantidadHechos);
    }
}
