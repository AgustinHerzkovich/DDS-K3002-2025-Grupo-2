package aplicacion.dto.mappers;

import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.domain.hechos.Hecho;
import org.springframework.stereotype.Component;

@Component
public class HechoInputMapper {
    public Hecho map(HechoInputDto hechoInputDto, Contribuyente autor) {
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