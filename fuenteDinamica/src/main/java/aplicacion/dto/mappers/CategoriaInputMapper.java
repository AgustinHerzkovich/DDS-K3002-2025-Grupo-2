package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Categoria;
import aplicacion.dto.input.CategoriaInputDto;
import org.springframework.stereotype.Component;

@Component
public class CategoriaInputMapper implements Mapper<CategoriaInputDto, Categoria> {
    public Categoria map(CategoriaInputDto categoriaInputDto) {
        return new Categoria(categoriaInputDto.getNombre());
    }
}
