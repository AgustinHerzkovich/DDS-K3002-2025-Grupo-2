package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.output.HechoOutputDto;
import org.springframework.stereotype.Component;

import java.util.List;

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
                hecho.getCategoria() != null ? categoriaOutputMapper.map(hecho.getCategoria()): null,
                hecho.getUbicacion() != null ? ubicacionOutputMapper.map(hecho.getUbicacion()): null,
                hecho.getFechaAcontecimiento(),
                hecho.getFechaCarga(),
                hecho.getFechaUltimaModificacion(),
                hecho.getOrigen(),
                hecho.getContenidoTexto(),
                hecho.getContenidoMultimedia() != null ? hecho.getContenidoMultimedia().stream().map(multimediaOutputMapper::map).toList() : null,
                hecho.getAnonimato(),
                hecho.getAutor() != null ? contribuyenteOutputMapper.map(hecho.getAutor()) : null,
                hecho.getEtiquetas().stream().map(EtiquetaOutputMapper::map).toList()
        );
    }
}
