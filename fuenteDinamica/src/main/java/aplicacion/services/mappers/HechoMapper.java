package aplicacion.services.mappers;

import aplicacion.services.dto.HechoDTO;
import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.usuarios.IdentidadContribuyente;

public class HechoMapper {
    public Hecho map(HechoDTO hechoDto, IdentidadContribuyente autor) {
        return new Hecho(hechoDto.getTitulo(),
                hechoDto.getDescripcion(),
                hechoDto.getCategoria(),
                hechoDto.getUbicacion(),
                hechoDto.getFechaAcontecimiento(),
                hechoDto.getContenidoTexto(),
                hechoDto.getContenidoMultimedia(),
                hechoDto.getAnonimato(),
                autor);
    }
}