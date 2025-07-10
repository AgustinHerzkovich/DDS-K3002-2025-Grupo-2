package domain.mappers;

import domain.dto.HechoInEstaticaDTO;
import domain.hechos.Hecho;
import domain.hechos.Origen;

import java.util.ArrayList;

// La fecha de carga del hecho en metamapa no conserva la fecha de carga de la fuente, sino que es la fecha de carga del hecho en metamapa.
public class HechoInEstaticaDTOToHecho implements Mapper<HechoInEstaticaDTO, Hecho> {
    public Hecho map(HechoInEstaticaDTO hechoDto) {
        Hecho hecho = new Hecho(hechoDto.getTitulo(),
                                hechoDto.getDescripcion(),
                                hechoDto.getCategoria(),
                                hechoDto.getUbicacion(),
                                hechoDto.getFechaAcontecimiento(),
                                Origen.DATASET,
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
