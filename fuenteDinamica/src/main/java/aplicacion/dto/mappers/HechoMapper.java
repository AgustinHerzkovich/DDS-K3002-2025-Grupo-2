package aplicacion.services.mappers;

import aplicacion.dto.HechoDto;
import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import org.springframework.stereotype.Component;

@Component
public class HechoMapper {
    public Hecho map(HechoDto hechoDto, IdentidadContribuyente autor) {
        return new Hecho(hechoDto.getTitulo(),
                hechoDto.getDescripcion(),
                hechoDto.getCategoria(),
                hechoDto.getUbicacion(),
                hechoDto.getFechaAcontecimiento(),
                hechoDto.getOrigen(),
                hechoDto.getContenidoTexto(),
                hechoDto.getContenidoMultimedia(),
                hechoDto.getAnonimato(),
                autor);
    }
}