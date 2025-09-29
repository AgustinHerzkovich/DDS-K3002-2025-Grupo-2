package domain.dtos.estadisticasDTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaConMasHechosDTO {
    private Long categoriaId;
    private String nombreCategoria;
    private Long cantidadHechos;

    public CategoriaConMasHechosDTO(Long categoria_id, String nombre_categoria, Long cantidad_hechos) {
        this.categoriaId = categoria_id;
        this.nombreCategoria = nombre_categoria;
        this.cantidadHechos = cantidad_hechos;
    }

    public String toString(){
        return String.format("%s   -   %d hechos", nombreCategoria, cantidadHechos);
    }
}
