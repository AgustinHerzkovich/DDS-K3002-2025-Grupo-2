package aplicacion.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonPropertyOrder({"cantidadSolicitudesSpam", "cantidadSolicitudesTotales" })
public class CantidadSolicitudesSpamDTO {
    private Long cantidadSolicitudesSpam;
    private Long cantidadSolicitudesTotales;
}
