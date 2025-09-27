package aplicacion.dtos;

public class CategoriaConMasHechosDTO {
    private Long categoriaId;
    private String nombreCategoria;
    private Long cantidadHechos;

    public CategoriaConMasHechosDTO(Long categoria_id, String nombre_categoria, Long cantidad_hechos) {
        this.categoriaId = categoria_id;
        this.nombreCategoria = nombre_categoria;
        this.cantidadHechos = cantidad_hechos;
    }
}
