package domain.mappers;

import domain.dto.SolicitudDTO;
import domain.hechos.Hecho;
import domain.solicitudes.SolicitudEliminacion;

public class SolicitudMapper {
    public SolicitudEliminacion map(SolicitudDTO solicitudDto, Hecho hecho) {
        return new SolicitudEliminacion(solicitudDto.getSolicitante(),
                                                                  hecho,
                                                                  solicitudDto.getMotivo());
    }
}