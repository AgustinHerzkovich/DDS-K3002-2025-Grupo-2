package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Hecho;
import aplicacion.dto.input.HechoInputDto;
import org.springframework.stereotype.Component;

@Component
public class HechoInputMapper {
    private final ContribuyenteInputMapper contribuyenteInputMapper;
    private final MultimediaInputMapper multimediaInputMapper;

    public HechoInputMapper(ContribuyenteInputMapper contribuyenteInputMapper, MultimediaInputMapper multimediaInputMapper) {
        this.contribuyenteInputMapper = contribuyenteInputMapper;
        this.multimediaInputMapper = multimediaInputMapper;
    }

    public Hecho map(HechoInputDto hechoInputDTO){
        return new Hecho(
                hechoInputDTO.getTitulo(),
                hechoInputDTO.getDescripcion(),
                hechoInputDTO.getCategoria(),
                hechoInputDTO.getUbicacion(),
                hechoInputDTO.getFechaAcontecimiento(),
                hechoInputDTO.getOrigen(),
                hechoInputDTO.getContenidoTexto(),
                hechoInputDTO.getContenidoMultimedia() != null ? hechoInputDTO.getContenidoMultimedia().stream().map(multimediaInputMapper::map).toList() : null,
                hechoInputDTO.getAnonimato(),
                hechoInputDTO.getAutor() != null ? this.contribuyenteInputMapper.map(hechoInputDTO.getAutor()) : null
        );
    }
}
