package domain.mappers;

import domain.dto.HechoDTO;
import domain.hechos.Hecho;
import domain.usuarios.IdentidadContribuyente;

public class HechoMapper {
    public Hecho map(HechoDTO hechoDto, IdentidadContribuyente autor) {
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