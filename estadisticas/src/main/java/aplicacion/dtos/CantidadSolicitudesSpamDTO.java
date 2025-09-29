package aplicacion.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
public class CantidadSolicitudesSpamDTO {
    private Long cantidadSolicitudesSpam;
    private Long cantidadSolicitudesTotales;
}
