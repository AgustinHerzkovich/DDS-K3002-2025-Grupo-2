package aplicacion.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"tipoDeSolicitud", "cantidadSolicitudes" })
public class CantidadSolicitudesPorTipo{
    private String tipoDeSolicitud;
    private Long cantidadSolicitudes;

    public CantidadSolicitudesPorTipo(CantidadSolicitudesPorTipoInterface cantidadSolicitudesPorTipoInterface) {
        tipoDeSolicitud = cantidadSolicitudesPorTipoInterface.getTipoDeSolicitud();
        cantidadSolicitudes = cantidadSolicitudesPorTipoInterface.getCantidadSolicitudes();
    }
}
