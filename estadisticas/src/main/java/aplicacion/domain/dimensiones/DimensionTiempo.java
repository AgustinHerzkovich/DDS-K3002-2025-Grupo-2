package aplicacion.domain.dimensiones;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
public class DimensionTiempo {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idTiempo;
    private Integer anio;
    private Integer mes;
    private Integer dia;
    private Integer hora;
    public DimensionTiempo(LocalDateTime fechaAcontecimiento) {
        this.anio = fechaAcontecimiento.getYear();
        this.mes = fechaAcontecimiento.getMonthValue();
        this.dia = fechaAcontecimiento.getDayOfMonth();
        this.hora = fechaAcontecimiento.getHour();
    }

    public DimensionTiempo() {

    }
}
