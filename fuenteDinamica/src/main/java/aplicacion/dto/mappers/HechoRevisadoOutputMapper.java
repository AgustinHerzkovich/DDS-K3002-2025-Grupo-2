package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.output.HechoRevisadoOutputDto;
import org.springframework.stereotype.Component;

@Component
public class HechoRevisadoOutputMapper {
    private final ContribuyenteOutputMapper contribuyenteOutputMapper;

    public HechoRevisadoOutputMapper(ContribuyenteOutputMapper contribuyenteOutputMapper) {
        this.contribuyenteOutputMapper = contribuyenteOutputMapper;
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
                hecho.getAutor() != null ? contribuyenteOutputMapper.map(hecho.getAutor()) : null,
                hecho.getEstadoRevision(),
                hecho.getSugerencia()
        );
    }
}
