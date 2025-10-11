package aplicacion.dto.mappers;

import aplicacion.domain.solicitudes.*;
import aplicacion.dto.output.EstadoSolicitudOutputDto;
import org.springframework.stereotype.Component;

@Component
public class EstadoSolicitudOutputMapper implements Mapper<EstadoSolicitud, EstadoSolicitudOutputDto> {
    public EstadoSolicitudOutputDto map(EstadoSolicitud estadoSolicitud) {
        String estado;
        switch(estadoSolicitud) {
            case EstadoSolicitudAceptada aceptada -> estado = "Aceptada";
            case EstadoSolicitudRechazada rechazada -> estado = "Rechazada";
            case EstadoSolicitudPendiente pendiente -> estado = "Pendiente";
            case EstadoSolicitudPrescripta prescripta -> estado = "Prescripta";
            case EstadoSolicitudSpam spam -> estado = "Spam";
            default -> throw new IllegalArgumentException("Estado de solicitud desconocido: " + estadoSolicitud.getClass().getName());
        }

        return new EstadoSolicitudOutputDto(estado);
    }
}
