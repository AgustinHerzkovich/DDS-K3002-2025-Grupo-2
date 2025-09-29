package domain.dtos.estadisticasDTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CantidadSolicitudesSpamDTO {
    private Long cantidadSolicitudesSpam;
    private Long cantidadSolicitudesTotales;
    public String toString(){
        return String.format("""
                Cantidad de solicitudes de spam: %d
                Cantidad de solicitudes totales: %d
                Tasa de SPAM: %1.2f
                """, cantidadSolicitudesSpam, cantidadSolicitudesTotales, (float) cantidadSolicitudesSpam/cantidadSolicitudesTotales);
    }
}
