package aplicacion.dto.mappers;

import aplicacion.domain.solicitudes.SolicitudEliminacion;
import aplicacion.dto.output.SolicitudOutputDto;
import org.springframework.stereotype.Component;

@Component
public class SolicitudOutputMapper {
    public SolicitudOutputDto map(SolicitudEliminacion solicitudEliminacion) {
        return new SolicitudOutputDto(
                solicitudEliminacion.getHecho().getId(),
                solicitudEliminacion.getMotivo(),
                solicitudEliminacion.getId(),
                solicitudEliminacion.getEstado(),
                solicitudEliminacion.getFechaSubida()
        );
    }
}
