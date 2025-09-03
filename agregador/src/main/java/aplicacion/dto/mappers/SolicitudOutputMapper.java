package aplicacion.dto.mappers;

import aplicacion.domain.solicitudes.SolicitudEliminacion;
import aplicacion.dto.output.SolicitudOutputDTO;
import org.springframework.stereotype.Component;

@Component
public class SolicitudOutputMapper {
    public SolicitudOutputDTO map(SolicitudEliminacion solicitudEliminacion) {
        return new SolicitudOutputDTO(
                solicitudEliminacion.getHecho().getId(),
                solicitudEliminacion.getMotivo(),
                solicitudEliminacion.getId(),
                solicitudEliminacion.getEstado(),
                solicitudEliminacion.getFechaSubida()
        );
    }
}
