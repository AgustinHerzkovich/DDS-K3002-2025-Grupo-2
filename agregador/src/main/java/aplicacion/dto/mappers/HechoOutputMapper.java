package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.output.HechoOutputDTO;
import org.springframework.stereotype.Component;

@Component
public class HechoOutputMapper {
    public HechoOutputDTO map(Hecho hecho) {
        return new HechoOutputDTO(
                hecho.getId(),
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
                hecho.getAutor()
        );
    }
}
