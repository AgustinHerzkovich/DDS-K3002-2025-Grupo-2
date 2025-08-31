package aplicacion.dto.mappers;

import aplicacion.dto.input.HechoInputDto;
import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.usuarios.IdentidadContribuyente;
import org.springframework.stereotype.Component;

@Component
public class HechoInputMapper {
    public Hecho map(HechoInputDto hechoInputDto, IdentidadContribuyente autor) {
        return new Hecho(hechoInputDto.getTitulo(),
                hechoInputDto.getDescripcion(),
                hechoInputDto.getCategoria(),
                hechoInputDto.getUbicacion(),
                hechoInputDto.getFechaAcontecimiento(),
                hechoInputDto.getOrigen(),
                hechoInputDto.getContenidoTexto(),
                hechoInputDto.getContenidoMultimedia(),
                hechoInputDto.getAnonimato(),
                autor);
    }
}