package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.output.HechoOutputDto;
import org.springframework.stereotype.Component;

@Component
public class HechoOutputMapper {
    private final ContribuyenteOutputMapper contribuyenteOutputMapper;

    public HechoOutputMapper(ContribuyenteOutputMapper contribuyenteOutputMapper) {
        this.contribuyenteOutputMapper = contribuyenteOutputMapper;
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
                hecho.getAutor() != null ? contribuyenteOutputMapper.map(hecho.getAutor()) : null
                );
    }
}
