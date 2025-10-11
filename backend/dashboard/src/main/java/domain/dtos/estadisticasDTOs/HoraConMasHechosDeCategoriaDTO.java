package domain.dtos.estadisticasDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HoraConMasHechosDeCategoriaDTO {
    Integer horaConMasHechosDeCategoria;
    Long cantidadDeHechos;
    private String categoria;

    public String toString(){
        return String.format("%s  -  %d:00   -   %d",categoria , horaConMasHechosDeCategoria, cantidadDeHechos);
    }
}
