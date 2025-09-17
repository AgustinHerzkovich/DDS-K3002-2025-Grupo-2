package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.domain.hechos.Hecho;
import org.springframework.stereotype.Component;

@Component
public class HechoInputMapper {
    private final CategoriaInputMapper categoriaInputMapper;
    private final UbicacionInputMapper ubicacionInputMapper;
    private final MultimediaInputMapper multimediaInputMapper;

    public HechoInputMapper(CategoriaInputMapper categoriaInputMapper, UbicacionInputMapper ubicacionInputMapper, MultimediaInputMapper multimediaInputMapper) {
        this.categoriaInputMapper = categoriaInputMapper;
        this.ubicacionInputMapper = ubicacionInputMapper;
        this.multimediaInputMapper = multimediaInputMapper;
    }

    public Hecho map(HechoInputDto hechoInputDto, Contribuyente autor) {
        return new Hecho(hechoInputDto.getTitulo(),
                hechoInputDto.getDescripcion(),
                categoriaInputMapper.map(hechoInputDto.getCategoria()),
                ubicacionInputMapper.map(hechoInputDto.getUbicacion()),
                hechoInputDto.getFechaAcontecimiento(),
                hechoInputDto.getOrigen(),
                hechoInputDto.getContenidoTexto(),
                hechoInputDto.getContenidoMultimedia().stream().map(multimediaInputMapper::map).toList(),
                hechoInputDto.getAnonimato(),
                autor);
    }
}