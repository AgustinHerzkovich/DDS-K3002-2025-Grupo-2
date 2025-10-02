package aplicacion.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({ "nombreCategoria", "cantidadHechos"})
public class CategoriaConMasHechosDTO {
    @JsonIgnore
    private Long categoriaId;
    private String nombreCategoria;
    private Long cantidadHechos;

    public CategoriaConMasHechosDTO(Long categoria_id, String nombre_categoria, Long cantidad_hechos) {
        this.categoriaId = categoria_id;
        this.nombreCategoria = nombre_categoria;
        this.cantidadHechos = cantidad_hechos;
    }
}
