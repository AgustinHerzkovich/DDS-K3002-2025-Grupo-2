package aplicacion.domain.dimensiones;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class DimensionTiempo {
    private Long idTiempo;
    private Integer anio;
    private Integer mes;
    private Integer dia;
    private Integer hora;
}
