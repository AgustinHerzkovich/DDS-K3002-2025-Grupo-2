package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.output.HechoOutputDto;
import org.springframework.stereotype.Component;

@Component
public class HechoOutputMapper implements Mapper<Hecho, HechoOutputDto>{
    private final CategoriaOutputMapper categoriaOutputMapper;
    private final UbicacionOutputMapper ubicacionOutputMapper;
    private final MultimediaOutputMapper multimediaOutputMapper;
    private final ContribuyenteOutputMapper contribuyenteOutputMapper;

    public HechoOutputMapper(CategoriaOutputMapper categoriaOutputMapper, UbicacionOutputMapper ubicacionOutputMapper, MultimediaOutputMapper multimediaOutputMapper, ContribuyenteOutputMapper contribuyenteOutputMapper) {
        this.categoriaOutputMapper = categoriaOutputMapper;
        this.ubicacionOutputMapper = ubicacionOutputMapper;
        this.multimediaOutputMapper = multimediaOutputMapper;
        this.contribuyenteOutputMapper = contribuyenteOutputMapper;
    }
    public HechoOutputDto map(Hecho hecho) {
        return new HechoOutputDto(
                hecho.getId(),
                hecho.getTitulo(),
                hecho.getDescripcion(),
                categoriaOutputMapper.map(hecho.getCategoria()),
                ubicacionOutputMapper.map(hecho.getUbicacion()),
                hecho.getFechaAcontecimiento(),
                hecho.getFechaCarga(),
                hecho.getFechaUltimaModificacion(),
                hecho.getOrigen(),
                hecho.getContenidoTexto(),
                hecho.getContenidoMultimedia() != null ? hecho.getContenidoMultimedia().stream().map(multimediaOutputMapper::map).toList() : null,
                hecho.getAnonimato(),
                hecho.getAutor() != null ? contribuyenteOutputMapper.map(hecho.getAutor()) : null
        );
    }
}
