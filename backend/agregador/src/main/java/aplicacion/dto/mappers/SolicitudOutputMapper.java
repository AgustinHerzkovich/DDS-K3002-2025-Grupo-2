package aplicacion.dto.mappers;

import aplicacion.domain.solicitudes.SolicitudEliminacion;
import aplicacion.dto.output.SolicitudOutputDto;
import org.springframework.stereotype.Component;

@Component
public class SolicitudOutputMapper implements Mapper<SolicitudEliminacion, SolicitudOutputDto>{
    private final EstadoSolicitudOutputMapper estadoSolicitudOutputMapper;

    public SolicitudOutputMapper(EstadoSolicitudOutputMapper estadoSolicitudOutputMapper) {
        this.estadoSolicitudOutputMapper = estadoSolicitudOutputMapper;
    }

    public SolicitudOutputDto map(SolicitudEliminacion solicitudEliminacion) {
        return new SolicitudOutputDto(
                solicitudEliminacion.getHecho().getId(),
                solicitudEliminacion.getMotivo(),
                solicitudEliminacion.getId(),
                estadoSolicitudOutputMapper.map(solicitudEliminacion.getEstado()),
                solicitudEliminacion.getFechaSubida()
        );
    }
}
