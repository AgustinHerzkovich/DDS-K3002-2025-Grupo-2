package domain.mappers;

import domain.dto.SolicitudDTO;
import domain.hechos.Hecho;
import domain.solicitudes.SolicitudEliminacion;
import domain.usuarios.Contribuyente;

public class SolicitudMapper {
    public SolicitudEliminacion map(SolicitudDTO solicitudDto, Contribuyente solicitante, Hecho hecho) {
        return new SolicitudEliminacion(solicitante, hecho, solicitudDto.getMotivo());
    }
}