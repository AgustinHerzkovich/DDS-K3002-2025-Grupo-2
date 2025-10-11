package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Categoria;
import aplicacion.dto.output.CategoriaOutputDto;
import org.springframework.stereotype.Component;

@Component
public class CategoriaOutputMapper implements Mapper<Categoria, CategoriaOutputDto> {
    public CategoriaOutputDto map(Categoria categoria) {
        return new CategoriaOutputDto(
                categoria.getId(),
                categoria.getNombre()
        );
    }
}
