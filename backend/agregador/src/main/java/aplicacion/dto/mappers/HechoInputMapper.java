package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.HechoInputDto;
import org.springframework.stereotype.Component;

@Component
public class HechoInputMapper implements Mapper<HechoInputDto, Hecho>{
    private final ContribuyenteInputMapper contribuyenteInputMapper;
    private final MultimediaInputMapper multimediaInputMapper;
    private final CategoriaInputMapper categoriaInputMapper;
    private final UbicacionInputMapper ubicacionInputMapper;


    public HechoInputMapper(ContribuyenteInputMapper contribuyenteInputMapper, MultimediaInputMapper multimediaInputMapper, CategoriaInputMapper categoriaInputMapper, UbicacionInputMapper ubicacionInputMapper) {
        this.contribuyenteInputMapper = contribuyenteInputMapper;
        this.multimediaInputMapper = multimediaInputMapper;
        this.categoriaInputMapper = categoriaInputMapper;
        this.ubicacionInputMapper = ubicacionInputMapper;
    }

    public Hecho map(HechoInputDto hechoInputDto){
        return new Hecho(
                hechoInputDto.getTitulo(),
                hechoInputDto.getDescripcion(),
                categoriaInputMapper.map(hechoInputDto.getCategoria()),
                ubicacionInputMapper.map(hechoInputDto.getUbicacion()),
                hechoInputDto.getFechaAcontecimiento(),
                hechoInputDto.getOrigen(),
                hechoInputDto.getContenidoTexto() != null ? hechoInputDto.getContenidoTexto() : "",
                hechoInputDto.getContenidoMultimedia() != null ? hechoInputDto.getContenidoMultimedia().stream().map(multimediaInputMapper::map).toList() : null,
                hechoInputDto.getAnonimato(),
                hechoInputDto.getAutor() != null ? this.contribuyenteInputMapper.map(hechoInputDto.getAutor()) : null
        );
    }
}
