package domain.mappers;

import domain.dto.HechoInEstaticaDTO;
import domain.hechos.Hecho;

public class HechoInEstaticaDTOToHecho implements Mapper<HechoInEstaticaDTO, Hecho> {
    public Hecho map(HechoInEstaticaDTO hechoDto) {
        Hecho hecho = new Hecho();
        hecho.setTitulo(hechoDto.getTitulo());
        hecho.setDescripcion(hechoDto.getDescripcion());
        hecho.setCategoria(hechoDto.getCategoria());
        hecho.setUbicacion(hechoDto.getUbicacion());
        hecho.setFechaAcontecimiento(hechoDto.getFechaAcontecimiento());
        hecho.setOrigen(hechoDto.getOrigen());
        hecho.setVisible(hechoDto.getVisible());
        hecho.setEtiquetas(hechoDto.getEtiquetas());
        return hecho;
    }
}
