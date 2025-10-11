package domain.dashboardDTOs.hechos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaDTO {
    private String nombre;
    public String toString(){
        return "{Categoria: "+nombre+"}";
    }
}