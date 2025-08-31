package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.output.HechoOutputDto;
import org.springframework.stereotype.Component;

@Component
public class HechoOutputMapper {
    private final IdentidadContribuyenteOutputMapper identidadContribuyenteOutputMapper;

    public HechoOutputMapper(IdentidadContribuyenteOutputMapper identidadContribuyenteOutputMapper) {
        this.identidadContribuyenteOutputMapper = identidadContribuyenteOutputMapper;
    }

    public HechoOutputDto map(Hecho hecho) {
        return new HechoOutputDto(hecho.getId(),
                hecho.getTitulo(),
                hecho.getDescripcion(),
                hecho.getCategoria(),
                hecho.getUbicacion(),
                hecho.getFechaAcontecimiento(),
                hecho.getFechaCarga(),
                hecho.getFechaUltimaModificacion(),
                hecho.getOrigen(),
                hecho.getContenidoTexto(),
                hecho.getContenidoMultimedia(),
                hecho.getAnonimato(),
                identidadContribuyenteOutputMapper.map(hecho.getAutor())
                );
    }
}
