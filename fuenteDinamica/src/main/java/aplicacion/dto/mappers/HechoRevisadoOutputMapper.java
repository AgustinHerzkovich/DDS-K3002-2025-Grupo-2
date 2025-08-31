package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.output.HechoOutputDto;
import aplicacion.dto.output.HechoRevisadoOutputDto;
import org.springframework.stereotype.Component;

@Component
public class HechoRevisadoOutputMapper {
    private final IdentidadContribuyenteOutputMapper identidadContribuyenteOutputMapper;

    public HechoRevisadoOutputMapper(IdentidadContribuyenteOutputMapper identidadContribuyenteOutputMapper) {
        this.identidadContribuyenteOutputMapper = identidadContribuyenteOutputMapper;
    }

    public HechoRevisadoOutputDto map(Hecho hecho) {
        return new HechoRevisadoOutputDto(hecho.getId(),
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
                identidadContribuyenteOutputMapper.map(hecho.getAutor()),
                hecho.getEstadoRevision(),
                hecho.getSugerencia()
        );
    }
}
