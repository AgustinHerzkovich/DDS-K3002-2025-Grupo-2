package aplicacion.dto.mappers;

import aplicacion.domain.hechos.Hecho;
import aplicacion.domain.usuarios.Contribuyente;
import aplicacion.dto.input.HechoInputDto;
import aplicacion.dto.input.HechoReporteInputDto;
import org.springframework.stereotype.Component;

@Component
public class HechoInputMapper implements Mapper<HechoInputDto, Hecho>{
    private final ContribuyenteInputMapper contribuyenteInputMapper;
    private final MultimediaInputMapper multimediaInputMapper;
    private final CategoriaInputMapper categoriaInputMapper;
    private final UbicacionInputMapper ubicacionInputMapper;


    public HechoInputMapper(ContribuyenteInputMapper contribuyenteInputMapper, MultimediaInputMapper multimediaInputMapper, CategoriaInputMapper categoriaInputMapper, UbicacionInputMapper ubicacionInputMapper) {
        this.contribuyenteInputMapper = contribuyenteInputMapper;
        this.multimediaInputMapper = multimediaInputMapper;
        this.categoriaInputMapper = categoriaInputMapper;
        this.ubicacionInputMapper = ubicacionInputMapper;
    }

    public Hecho map(HechoInputDto hechoInputDto){
        return new Hecho(
                hechoInputDto.getId(), // Si es null, JPA autogenerará el UUID
                hechoInputDto.getTitulo(),
                hechoInputDto.getDescripcion(),
                categoriaInputMapper.map(hechoInputDto.getCategoria()),
                ubicacionInputMapper.map(hechoInputDto.getUbicacion()),
                hechoInputDto.getFechaAcontecimiento(),
                hechoInputDto.getOrigen(),
                hechoInputDto.getContenidoTexto() != null ? hechoInputDto.getContenidoTexto() : "",
                hechoInputDto.getContenidoMultimedia() != null ? hechoInputDto.getContenidoMultimedia().stream().map(multimediaInputMapper::map).toList() : null,
                hechoInputDto.getAnonimato(),
                hechoInputDto.getAutor() != null ? this.contribuyenteInputMapper.map(hechoInputDto.getAutor()) : null
        );
    }

    public Hecho mapReporte(HechoReporteInputDto hechoReporteInputDto, Contribuyente contribuyente){
        return new Hecho(
                null, // Los reportes siempre generan un nuevo UUID
                hechoReporteInputDto.getTitulo(),
                hechoReporteInputDto.getDescripcion(),
                categoriaInputMapper.map(hechoReporteInputDto.getCategoria()),
                ubicacionInputMapper.map(hechoReporteInputDto.getUbicacion()),
                hechoReporteInputDto.getFechaAcontecimiento(),
                hechoReporteInputDto.getOrigen(),
                hechoReporteInputDto.getContenidoTexto() != null ? hechoReporteInputDto.getContenidoTexto() : "",
                hechoReporteInputDto.getContenidoMultimedia() != null ? hechoReporteInputDto.getContenidoMultimedia().stream().map(multimediaInputMapper::map).toList() : null,
                hechoReporteInputDto.getAnonimato(),
                hechoReporteInputDto.getAutor() != null ? contribuyente : null
        );
    }
}
