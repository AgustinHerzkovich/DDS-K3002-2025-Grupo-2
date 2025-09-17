package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.output.HechoRevisadoOutputDto;
import org.springframework.stereotype.Component;

@Component
public class HechoRevisadoOutputMapper implements Mapper<Hecho, HechoRevisadoOutputDto> {
    private final CategoriaOutputMapper categoriaOutputMapper;
    private final UbicacionOutputMapper ubicacionOutputMapper;
    private final MultimediaOutputMapper multimediaOutputMapper;
    private final ContribuyenteOutputMapper contribuyenteOutputMapper;

    public HechoRevisadoOutputMapper(CategoriaOutputMapper categoriaOutputMapper, UbicacionOutputMapper ubicacionOutputMapper, MultimediaOutputMapper multimediaOutputMapper, ContribuyenteOutputMapper contribuyenteOutputMapper) {
        this.categoriaOutputMapper = categoriaOutputMapper;
        this.ubicacionOutputMapper = ubicacionOutputMapper;
        this.multimediaOutputMapper = multimediaOutputMapper;
        this.contribuyenteOutputMapper = contribuyenteOutputMapper;
    }

    public HechoRevisadoOutputDto map(Hecho hecho) {
        return new HechoRevisadoOutputDto(hecho.getId(),
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
                hecho.getAutor() != null ? contribuyenteOutputMapper.map(hecho.getAutor()) : null,
                hecho.getEstadoRevision(),
                hecho.getSugerencia()
        );
    }
}
