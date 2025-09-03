package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.HechoInputDTO;
import org.springframework.stereotype.Component;

@Component
public class HechoInputMapper {
    public static Hecho map(HechoInputDTO hechoInputDTO){
        return new Hecho(
                hechoInputDTO.getTitulo(),
                hechoInputDTO.getDescripcion(),
                hechoInputDTO.getCategoria(),
                hechoInputDTO.getUbicacion(),
                hechoInputDTO.getFechaAcontecimiento(),
                hechoInputDTO.getOrigen(),
                hechoInputDTO.getContenidoTexto(),
                hechoInputDTO.getContenidoMultimedia(),
                hechoInputDTO.getAnonimato(),
                hechoInputDTO.getAutor()
        );
    }
}
