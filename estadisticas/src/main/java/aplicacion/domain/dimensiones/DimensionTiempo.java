package aplicacion.domain.dimensiones;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class DimensionTiempo {
    @Id
    private Long idTiempo;
    private Integer anio;
    private Integer mes;
    private Integer dia;
    private Integer hora;
}
