package aplicacion.services.mappers;

import aplicacion.services.dto.HechoInEstaticaDTO;
import aplicacion.domain.hechos.Hecho;

import java.util.ArrayList;

// La fecha de carga del hecho en metamapa no conserva la fecha de carga de la fuente, sino que es la fecha de carga del hecho en metamapa.
public class HechoInEstaticaDTOToHecho implements Mapper<HechoInEstaticaDTO, Hecho> {
    public Hecho map(HechoInEstaticaDTO hechoDto) {
        Hecho hecho = new Hecho(hechoDto.getTitulo(),
                                hechoDto.getDescripcion(),
                                hechoDto.getCategoria(),
                                hechoDto.getUbicacion(),
                                hechoDto.getFechaAcontecimiento(),
                                hechoDto.getOrigen(),
                                "",
                                new ArrayList<>(),
                                true,
                                null
                                );
        //hecho.setVisible(hechoDto.getVisible());
        //hecho.setEtiquetas(hechoDto.getEtiquetas());
        return hecho;
    }
}
