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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("""
            Cantidad de solicitudes de spam: %d
            Cantidad de solicitudes totales: %d
            """, cantidadSolicitudesSpam, cantidadSolicitudesTotales));

        if (cantidadSolicitudesTotales != null && cantidadSolicitudesTotales != 0) {
            float tasaSpam = (float) cantidadSolicitudesSpam / cantidadSolicitudesTotales;
            sb.append(String.format("Tasa de SPAM: %1.2f", tasaSpam));
        }

        return sb.toString();
    }
}
